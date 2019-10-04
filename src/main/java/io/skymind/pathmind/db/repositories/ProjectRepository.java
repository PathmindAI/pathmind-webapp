package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.Model;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.Tables.MODEL;
import static io.skymind.pathmind.data.db.Tables.PROJECT;


@Repository
public class ProjectRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Project> getProjectsForUser(long userId) {
        return dslContext
				.selectFrom(PROJECT)
				.where(PROJECT.PATHMIND_USER_ID.eq(userId))
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
				.select(PROJECT.asterisk())
				.from(PROJECT)
					.leftJoin(Model.MODEL)
						.on(PROJECT.ID.eq(Model.MODEL.PROJECT_ID))
					.leftJoin(Experiment.EXPERIMENT)
						.on(MODEL.ID.eq(Experiment.EXPERIMENT.MODEL_ID))
				.where(Experiment.EXPERIMENT.ID.eq(experimentId))
				.fetchOneInto(Project.class);
	}

	public void archive(long projectId, boolean isArchive) {
    	dslContext.update(PROJECT)
				.set(PROJECT.ARCHIVED, isArchive)
				.where(PROJECT.ID.eq(projectId))
				.execute();
	}

	protected long insertProject(Project project) {
    	// TODO -> Needs to be re-implemented and adjusted for the new data models.
    	long projectId = dslContext
				.insertInto(PROJECT)
				.set(PROJECT.NAME, project.getName())
				.set(PROJECT.DATE_CREATED, project.getDateCreated())
				.set(PROJECT.LAST_ACTIVITY_DATE, project.getLastActivityDate())
				.set(PROJECT.PATHMIND_USER_ID, project.getPathmindUserId())
				.returning(PROJECT.ID)
				.fetchOne()
				.getValue(PROJECT.ID);
    	project.setId(projectId);
    	return projectId;
	}
}
