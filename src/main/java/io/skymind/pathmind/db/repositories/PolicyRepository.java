package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.Policy;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PolicyRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Policy> getPoliciesForUser(long userId) {
        // TODO -> Implement
        return null;
    }

//    public List<Model> getRunsForUser(long userId) {
//        return dslContext
//                .select(RUN.asterisk())
//                .select(MODEL.NAME)
//			    .from(RUN)
//				.where(MODEL.PROJECT_ID.eq(projectId))
//				.fetchInto(Model.class);
//    }

//    public long getProjectIdForModel(long modelId) {
//    	return dslContext
//				.select(MODEL.PROJECT_ID)
//				.from(MODEL)
//				.where(MODEL.ID.eq(modelId))
//				.fetchOneInto(Long.class);
//	}
}
