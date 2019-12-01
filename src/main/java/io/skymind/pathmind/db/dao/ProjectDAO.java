package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import io.skymind.pathmind.data.db.tables.records.ProjectRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.data.db.Tables.*;

@Repository
public class ProjectDAO
{
	private final DSLContext ctx;

	ProjectDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Returns the experimentId
	 * @return The experimentId
	 */
	@Transactional
	public long setupNewProject(Project project, Model model)
	{
		return ctx.transactionResult(configuration ->
		{
			DSLContext transactionCtx = DSL.using(configuration);
			LocalDateTime dateCreated = LocalDateTime.now();
			long projectId = ProjectSQL.insertProject(transactionCtx, project, dateCreated);
			long modelId = ModelSQL.insertModel(transactionCtx, model, "1", dateCreated, projectId);
			return ExperimentSQL.insertExperiment(transactionCtx, modelId, dateCreated);
		});
	}

	public void archive(long projectId, boolean isArchive) {
		ProjectSQL.archive(ctx, projectId, isArchive);
	}

	public Project getProject(long projectId) {
		return ProjectSQL.getProject(ctx, projectId);
	}

	public List<Project> getProjectsForUser(long userId) {
		return ProjectSQL.getProjectsForUser(ctx, userId);
	}

}
