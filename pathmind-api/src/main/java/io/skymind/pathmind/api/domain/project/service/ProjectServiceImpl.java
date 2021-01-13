package io.skymind.pathmind.api.domain.project.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.skymind.pathmind.api.domain.project.dto.ProjectVO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;

    public List<ProjectVO> getProjects(Long userId) {
        Objects.requireNonNull(userId, "userId is required");
        log.debug("loading projects for user {}", userId);
        Collection<Project> projects = CollectionUtils.emptyIfNull(projectDAO.getProjectsForUser(userId));
        log.debug("loaded {} projects for user {}", projects.size(), userId);
        return projects.stream().map(map).collect(Collectors.toList());
    }

    private static final Function<Project, ProjectVO> map = project -> {
        ProjectVO projectVO = ProjectVO.builder()
                .id(project.getId())
                .name(project.getName())
                .isArchived(project.isArchived())
                .dateCreated(project.getDateCreated())
                .lastActivityDate(project.getLastActivityDate())
                .build();
        log.debug("mapped {} to {}", project, projectVO);
        return projectVO;
    };

}
