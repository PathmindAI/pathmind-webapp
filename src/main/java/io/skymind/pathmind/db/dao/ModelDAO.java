package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.db.repositories.ModelRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.data.db.Tables.MODEL;

@Repository
public class ModelDAO extends ModelRepository
{

    private final DSLContext ctx;

    public ModelDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public byte[] getModelFile(long id) {
        return ctx.select(MODEL.FILE).from(MODEL).where(MODEL.ID.eq(id)).fetchOne(MODEL.FILE);
    }

    /**
     * Note: This doesn't return a *FULL* model. Only the fields that seems relevant at the moment.
     * @param modelId
     * @return Model - beware, not all fields are initialized
     */
    public Model getModel(long modelId) {
        return ctx.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
                .from(MODEL)
                .where(MODEL.ID.eq(modelId))
                .fetchOneInto(Model.class);
    }
}
