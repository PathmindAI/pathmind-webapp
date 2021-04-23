package io.skymind.pathmind.services.project;

import java.util.List;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectGridService {

    private final ProjectDAO projectDAO;

    public List<Project> getFilteredProjectsForUser(long userId, boolean isArchived, int offset, int limit) {
        return projectDAO.getFilteredProjectsForUser(userId, isArchived, offset, limit);
    }

    public int countFilteredProjects(long userId, boolean isArchived) {
        return projectDAO.countFilteredProjects(userId, isArchived);
    }

    public int countProjects(long userId) {
        return projectDAO.countProjects(userId);
    }
}
