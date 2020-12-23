package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.ProjectType;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDAO {
    private final DSLContext ctx;

    ProjectDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Returns the projectId
     *
     * @return The projectId
     */
    public long createNewProject(Project project) {
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

    public void updateUserNotes(long projectId, String userNotes) {
        ProjectRepository.updateUserNotes(ctx, projectId, userNotes);
    }

    public List<Project> getProjectsForUser(long userId) {
        return getProjectsForUser(userId, null);
    }

    public Optional<Project> getProjectIfAllowed(long projectId, long userId) {
        return ProjectRepository.getProjectIfAllowed(ctx, projectId, userId);
    }

    public List<Project> getProjectsForUser(long userId, ProjectType projectType) {
        return ProjectRepository.getProjectsForUser(ctx, userId, projectType);
    }

}
