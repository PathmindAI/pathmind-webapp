package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.data.db.Tables.MODEL;
import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;

import java.time.LocalDateTime;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import io.skymind.pathmind.db.repositories.ModelRepository;

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
    
    public int getModelCount(long projectId) {
		return ctx.selectCount()
				.from(MODEL)
				.where(MODEL.PROJECT_ID.eq(projectId))
				.fetchOne(0, int.class);
	}
    
    @Transactional
	public long addModelToProject(Model model, long projectId) {
		final ModelRecord mod = MODEL.newRecord();
		mod.attach(ctx.configuration());
		
		String name = Integer.toString(getModelCount(projectId) + 1);

		mod.setName(name);
		mod.setDateCreated(LocalDateTime.now());
		mod.setLastActivityDate(mod.getDateCreated());
		mod.setProjectId(projectId);
		mod.setNumberOfPossibleActions(model.getNumberOfPossibleActions());
		mod.setNumberOfObservations(model.getNumberOfObservations());
		mod.setGetObservationForRewardFunction(model.getGetObservationForRewardFunction());
		mod.setFile(model.getFile());
		mod.store();


		final ExperimentRecord ex = EXPERIMENT.newRecord();
		ex.attach(ctx.configuration());
		ex.setDateCreated(mod.getDateCreated());
		ex.setModelId(mod.getId());
		ex.setName("1");
		ex.setRewardFunction("");
		ex.store();

		return ex.getId();
	}
}
