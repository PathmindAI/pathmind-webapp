package io.skymind.pathmind.db.repositories;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RunRepository
{
    @Autowired
    private DSLContext dslContext;

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
