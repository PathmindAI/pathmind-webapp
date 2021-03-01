package io.skymind.pathmind.api.domain;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import io.skymind.pathmind.api.conf.security.PathmindApiUser;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.experiment.ExperimentService;
import io.skymind.pathmind.services.experiment.ModelCheckException;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
import io.skymind.pathmind.services.project.rest.dto.AnalyzeRequestDTO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
public class ModelUploadController {

    private final UriComponentsBuilder experimentUriBuilder;

    public ModelUploadController(@Value("${pm.api.webapp.url}") String webappDomainUrl) {
        experimentUriBuilder = UriComponentsBuilder.fromHttpUrl(webappDomainUrl);
    }

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private ExperimentService experimentService;


    /*
    create new project and upload model:
    curl -i -XPOST -H "X-PM-API-TOKEN: 1d83d812-f79a-497c-a437-ec78957d294a" -F 'file=@/Users/malex/Downloads/PathmindAPIUsage.zip' http://localhost:8081/al/upload

    upload model to existing project:
    curl -i -XPOST -H "X-PM-API-TOKEN: 42461663-29c4-44a3-9b2e-39563c7b2a4a" -F 'file=@/Users/malex/Downloads/CoffeeShopPathmindDemoExported.zip' -F 'projectId=40303' http://localhost:8081/al/upload
     */
    @PostMapping("/al/upload")
    public ResponseEntity<?> handleALFileUpload(@RequestParam("file") MultipartFile file,
                                                @RequestParam(value = "projectId", required = false) Long projectId,
                                                @AuthenticationPrincipal PathmindApiUser pmUser) {
        return handleFileUpload(file, projectId, pmUser, AnalyzeRequestDTO.ModelType.ANY_LOGIC, null);
    }


    /*
    upload model to existing project:
    curl -i -XPOST -H "X-PM-API-TOKEN: af26c5e1-8838-4c41-b490-e5dc7de3aeef" -F 'file=@/home/kepricon/Downloads/tests.zip' -F 'projectId=496' -F 'env=tests.factory.environments.FactoryEnv' http://localhost:8081/py/upload
     */
    @PostMapping("/py/upload")
    public ResponseEntity<?> handlePYFileUpload(@RequestParam("file") MultipartFile file,
                                                @RequestParam("env") String environment,
                                                @RequestParam(value = "projectId", required = false) Long projectId,
                                                @AuthenticationPrincipal PathmindApiUser pmUser) {
        return handleFileUpload(file, projectId, pmUser, AnalyzeRequestDTO.ModelType.PYTHON, environment);
    }

    private ResponseEntity<?> handleFileUpload(MultipartFile file, Long projectId, PathmindApiUser pmUser,
                                               AnalyzeRequestDTO.ModelType type, String environment) {
        UriComponentsBuilder builder = experimentUriBuilder.cloneBuilder();
        log.debug("saving file {}", file.getOriginalFilename());
        try {
            Path tempFile = Files.createTempFile("pm-upload", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());
            log.debug("saved file {} to temp location {}", file.getOriginalFilename(), tempFile);

            Project prj = null;
            if (projectId != null) {
                prj = projectDAO.getProjectIfAllowed(projectId, pmUser.getUserId())
                    .orElseThrow(() -> {
                        log.error("project {} does not belong to user {}", projectId, pmUser.getUserId());
                        throw new AccessDeniedException("project does not belong to user");
                    });
            }
            final Optional<Project> project = Optional.ofNullable(prj);

            Supplier<Project> projectSupplier =  () -> project.orElseGet(() -> {
                final LocalDateTime now = LocalDateTime.now();
                Project newProject = new Project();
                String simpleType = type.equals(AnalyzeRequestDTO.ModelType.ANY_LOGIC) ? "AL" : "PY";
                newProject.setName(simpleType + "-Upload-" + DateTimeFormatter.ISO_DATE_TIME.format(now));
                newProject.setPathmindUserId(pmUser.getUserId());
                long newProjectId = projectDAO.createNewProject(newProject);
                newProject.setId(newProjectId);
                log.info("created project {}", newProjectId);
                return newProject;
            });

            ModelBytes modelBytes = ModelBytes.of(Files.readAllBytes(tempFile.toAbsolutePath()));
            Experiment experiment = experimentService.createExperimentFromModelBytes(modelBytes, projectSupplier, type, environment);

            Long experimentId = experiment.getId();
            log.info("created experiment {}", experimentId);
            URI experimentUri = builder
                .path("newExperiment").path("/{experimentId}")
                .buildAndExpand(Map.of("experimentId", experimentId))
                .toUri();

            return ResponseEntity.status(HttpStatus.CREATED).location(experimentUri).build();
        } catch (Exception e) {
            log.error("failed to get file from {}", type, e);
            builder.path("uploadModelError");
            if (e instanceof ModelCheckException) {
                builder.path("/"+StringUtils.trimToEmpty(e.getMessage()));
            }
            String errorMessage = StringUtils.trimToEmpty(e.getMessage());
            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, builder.toUriString()).body(errorMessage);
        }
    }

}