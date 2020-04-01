package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.constants.GuideStep;
import io.skymind.pathmind.shared.data.Project;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public long createNewProject(Project project)
	{
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			long projectId = ProjectRepository.insertProject(transactionCtx, project, dateCreated);
			GuideRepository.insertGuideStep(transactionCtx, projectId, GuideStep.Overview);
			return projectId;
		});
	}

	public void archive(long projectId, boolean isArchive) {
		ProjectRepository.archive(ctx, projectId, isArchive);
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
