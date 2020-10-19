package io.skymind.pathmind.api.domain.project.service;

import io.skymind.pathmind.api.domain.project.dto.ProjectVO;

import java.util.List;

public interface ProjectService {

    List<ProjectVO> getProjects(Long userId);

}
