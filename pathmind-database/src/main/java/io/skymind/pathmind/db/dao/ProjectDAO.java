package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Project;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectDAO
{
	private final DSLContext ctx;

	ProjectDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Returns the projectId
	 * @return The projectId
	 */
	public long createNewProject(Project project)
	{
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			return ProjectRepository.insertProject(transactionCtx, project, dateCreated);
		});
	}

	public void archive(long projectId, boolean isArchive) {
		ProjectRepository.archive(ctx, projectId, isArchive);
	}
	
	public void updateProjectName(long projectId, String projectName) {
		ProjectRepository.updateProjectName(ctx, projectId, projectName);
	}

	public Optional<Project> getProject(long projectId) {
		return Optional.ofNullable(ProjectRepository.getProject(ctx, projectId));
	}

	public void updateUserNotes(long projectId, String userNotes) {
		ProjectRepository.updateUserNotes(ctx, projectId, userNotes);
	}

	public List<Project> getProjectsForUser(long userId) {
		return ProjectRepository.getProjectsForUser(ctx, userId);
	}

}
