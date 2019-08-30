package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.RewardFunction;
import io.skymind.pathmind.security.SecurityUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.skymind.pathmind.data.db.tables.Project.PROJECT;

@Repository
public class ProjectRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Project> getProjectsForUser(long userId) {
        return dslContext
				.selectFrom(PROJECT)
				.where(PROJECT.USER_ID.eq(userId))
				.fetchInto(Project.class);
    }

    // Convenience method so we don't always need to pass in the userId
    public List<Project> getProjectsForUser() {
        return dslContext
				.selectFrom(PROJECT)
				.where(PROJECT.USER_ID.eq(SecurityUtils.getUser().getId()))
				.fetchInto(Project.class);
    }

    public Project getProject(long projectId) {
    	return dslContext
				.selectFrom(PROJECT)
				.where(PROJECT.ID.eq(projectId))
				.fetchOneInto(Project.class);
	}

	public Project getProjectForExperiment(long experimentId) {
		return dslContext
				.select()
				.from(PROJECT)
					.leftJoin(Experiment.EXPERIMENT)
					.on(PROJECT.ID.eq(Experiment.EXPERIMENT.PROJECT_ID))
				.where(Experiment.EXPERIMENT.ID.eq(experimentId))
				.fetchOneInto(Project.class);
	}

	public Project getProjectForRewardFunction(long rewardFunctionId) {
		return dslContext
				.select(PROJECT.asterisk())
				.from(PROJECT)
					.leftJoin(Experiment.EXPERIMENT)
					.on(PROJECT.ID.eq(Experiment.EXPERIMENT.PROJECT_ID))
					.leftJoin(RewardFunction.REWARD_FUNCTION)
					.on(Experiment.EXPERIMENT.ID.eq(RewardFunction.REWARD_FUNCTION.EXPERIMENT_ID))
				.where(RewardFunction.REWARD_FUNCTION.ID.eq(rewardFunctionId))
				.fetchOneInto(Project.class);
	}

	public long insertProject(Project project) {
    	return dslContext
				.insertInto(PROJECT)
				.set(PROJECT.NAME, project.getName())
				.set(PROJECT.DATE_CREATED, LocalDate.now())
				.set(PROJECT.USER_ID, SecurityUtils.getUser().getId())
				.returning(PROJECT.ID)
				.fetchOne()
				.getValue(PROJECT.ID);
	}
}
