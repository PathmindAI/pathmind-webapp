package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.Model;
import io.skymind.pathmind.data.db.tables.records.ProjectRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.data.db.Tables.MODEL;
import static io.skymind.pathmind.data.db.Tables.PROJECT;

@Repository
class ProjectSQL
{
	protected static List<Project> getProjectsForUser(DSLContext ctx, long userId) {
        return ctx
				.selectFrom(PROJECT)
				.where(PROJECT.PATHMIND_USER_ID.eq(userId))
				.fetchInto(Project.class);
    }

	protected static Project getProject(DSLContext ctx, long projectId) {
    	return ctx
				.selectFrom(PROJECT)
				.where(PROJECT.ID.eq(projectId))
				.fetchOneInto(Project.class);
	}

	protected static void archive(DSLContext ctx, long projectId, boolean isArchive) {
    	ctx.update(PROJECT)
				.set(PROJECT.ARCHIVED, isArchive)
				.where(PROJECT.ID.eq(projectId))
				.execute();
	}

	protected static long insertProject(DSLContext ctx, Project project, LocalDateTime dateCreated) {
		final ProjectRecord savedProject = PROJECT.newRecord();
		savedProject.attach(ctx.configuration());
		savedProject.setName(project.getName());
		savedProject.setDateCreated(dateCreated);
		savedProject.setLastActivityDate(savedProject.getDateCreated());
		savedProject.setPathmindUserId(project.getPathmindUserId());
		savedProject.store();
		return savedProject.key().get(PROJECT.ID);
	}
}
