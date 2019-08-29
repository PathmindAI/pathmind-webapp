package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Project;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RewardfunctionRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Project> getRewardFunctionForExperiment(int experimentId) {
    	return null;
//        return dslContext
//            .selectFrom(PROJECT)
//            .where(PROJECT.USER_ID.eq(userId))
//            .fetchInto(Project.class);
    }
}
