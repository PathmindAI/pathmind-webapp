package io.skymind.pathmind.api.domain.project.service;

import java.util.List;

import io.skymind.pathmind.api.domain.project.dto.ProjectVO;

public interface ProjectService {

    List<ProjectVO> getProjects(Long userId);

}
