package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.db.utils.GridSortOrder;
import io.skymind.pathmind.shared.data.Project;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDAO {
    private final DSLContext ctx;

    ProjectDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public long createNewProject(Project project) {
        return ctx.transactionResult(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);
            LocalDateTime dateCreated = LocalDateTime.now();
            return ProjectRepository.insertProject(transactionCtx, project, dateCreated);
        });
    }

    public List<Project> getFilteredProjectsForUser(long userId, boolean isArchived, int offset, int limit, List<GridSortOrder> sortOrders) {
        String sortBy = sortOrders.size() > 0 ? sortOrders.get(0).getPropertyName() : "";
        boolean isDesc = sortOrders.size() > 0 ? sortOrders.get(0).isDescending() : false;
        return ProjectRepository.getFilteredProjectsForUser(ctx, 
                    userId,
                    isArchived,
                    offset,
                    limit,
                    sortBy,
                    isDesc);
    }

    public List<Project> getProjectsForUser(long userId) {
        return ProjectRepository.getProjectsForUser(ctx, userId);
    }

    public Optional<Project> getProjectIfAllowed(long projectId, long userId) {
        return ProjectRepository.getProjectIfAllowed(ctx, projectId, userId);
    }

    public int countFilteredProjects(long userId, boolean isArchived) {
        return ProjectRepository.getFilteredProjectCount(ctx, userId, isArchived);
    }

    public int countProjects(long userId) {
        return ProjectRepository.getProjectCount(ctx, userId);
    }

    public void archive(long projectId, boolean isArchive) {
        ProjectRepository.update(ctx, new ProjectUpdateRequest(projectId).isArchived(isArchive));
    }

    public void updateProjectName(long projectId, String projectName) {
        ProjectRepository.update(ctx, new ProjectUpdateRequest(projectId).name(projectName));
    }

    public void updateUserNotes(long projectId, String userNotes) {
        ProjectRepository.update(ctx, new ProjectUpdateRequest(projectId).userNotes(userNotes));
    }

    public void updateLastActivityDate(long projectId, LocalDateTime activityDate) {
        ProjectRepository.update(ctx, new ProjectUpdateRequest(projectId).lastActivityDate(activityDate));
    }

}
