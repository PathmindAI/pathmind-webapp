package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.Model;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.Tables.MODEL;

@Repository
public class ModelRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Model> getModelsForProject(long projectId) {
        return dslContext
				.selectFrom(MODEL)
				.where(MODEL.PROJECT_ID.eq(projectId))
				.fetchInto(Model.class);
    }

    public long getProjectIdForModel(long modelId) {
    	return dslContext
				.select(MODEL.PROJECT_ID)
				.from(MODEL)
				.where(MODEL.ID.eq(modelId))
				.fetchOneInto(Long.class);
	}

	public void archive(long modelId, boolean isArchive) {
    	dslContext.update(MODEL)
				.set(MODEL.ARCHIVED, isArchive)
				.where(MODEL.ID.eq(modelId))
				.execute();
	}

	protected long insertModel(Model model) {
    	long modelId = dslContext
				.insertInto(MODEL)
				.set(MODEL.NAME, model.getName())
				.set(MODEL.DATE_CREATED, model.getDateCreated())
				.set(MODEL.LAST_ACTIVITY_DATE, model.getLastActivityDate())
				.set(MODEL.NUMBER_OF_OBSERVATIONS, model.getNumberOfObservations())
				.set(MODEL.NUMBER_OF_POSSIBLE_ACTIONS, model.getNumberOfPossibleActions())
				.set(MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION, model.getGetObservationForRewardFunction())
				.returning(MODEL.ID)
				.fetchOne()
				.getValue(MODEL.ID);
    	model.setId(modelId);
    	return modelId;
	}
}
