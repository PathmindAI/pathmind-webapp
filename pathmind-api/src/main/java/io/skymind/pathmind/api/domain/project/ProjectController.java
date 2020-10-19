package io.skymind.pathmind.api.domain.project;

import io.skymind.pathmind.api.domain.project.dto.ProjectVO;
import io.skymind.pathmind.api.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(path = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProjectVO> all(Principal principal) { // TODO: spring security
        Long userId = Long.parseLong(principal.getName());
        return projectService.getProjects(userId);
    }

}
