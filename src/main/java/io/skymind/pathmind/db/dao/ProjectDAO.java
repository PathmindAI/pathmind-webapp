package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.skymind.pathmind.data.Project;

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
	@Transactional
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

	public Project getProject(long projectId) {
		return ProjectRepository.getProject(ctx, projectId);
	}

	public List<Project> getProjectsForUser(long userId) {
		return ProjectRepository.getProjectsForUser(ctx, userId);
	}

}
