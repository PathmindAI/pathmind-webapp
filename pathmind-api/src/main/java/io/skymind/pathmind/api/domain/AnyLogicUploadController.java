package io.skymind.pathmind.api.domain;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.skymind.pathmind.api.conf.security.PathmindApiUser;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.FileCheckResult;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.Getter;
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

import static io.skymind.pathmind.services.project.ProjectFileCheckService.INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS;
import static io.skymind.pathmind.shared.utils.UploadUtils.ensureZipFileStructure;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class AnyLogicUploadController {

    private final String webappDomainUrl;

    @Getter
    private final String modelCheckFailedHelpUrl;

    public AnyLogicUploadController(@Value("${pm.api.webapp.url}") String webappDomainUrl,
                                    @Value("${pm.api.model-check-failed-help.url}") String modelCheckFailedHelpUrl) {
        this.webappDomainUrl = webappDomainUrl;
        this.modelCheckFailedHelpUrl = modelCheckFailedHelpUrl;
    }

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    ModelDAO modelDAO;

    @Autowired
    ModelService modelService;

    @Autowired
    private ProjectFileCheckService projectFileCheckService;

    @Autowired
    RewardVariableDAO rewardVariableDAO;

    @Autowired
    ObservationDAO observationDAO;

    /*
    create new project and upload model:
    curl -i -XPOST -H "X-PM-API-TOKEN: 1d83d812-f79a-497c-a437-ec78957d294a" -F 'file=@/Users/malex/Downloads/PathmindAPIUsage.zip' http://localhost:8081/al/upload

    upload model to existing project:
    curl -i -XPOST -H "X-PM-API-TOKEN: 42461663-29c4-44a3-9b2e-39563c7b2a4a" -F 'file=@/Users/malex/Downloads/CoffeeShopPathmindDemoExported.zip' -F 'projectId=40303' http://localhost:8081/al/upload
     */
    @PostMapping("/al/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
                                              @RequestParam(value = "projectId", required = false) Long projectId,
                                              @AuthenticationPrincipal PathmindApiUser pmUser) {

        log.debug("saving file {}", file.getOriginalFilename());
        try {
            Path tempFile = Files.createTempFile("pm-al-upload", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());
            log.debug("saved file {} to temp location {}", file.getOriginalFilename(), tempFile);

            Project project = null;
            if (projectId != null) {
                project = projectDAO.getProjectIfAllowed(projectId, pmUser.getUserId())
                        .orElseThrow(() -> {
                            log.error("project {} does not belong to user {}", projectId, pmUser.getUserId());
                            throw new AccessDeniedException("project does not belong to user");
                        });
            }

            Model model = new Model();
            model.setFile(ensureZipFileStructure(Files.readAllBytes(tempFile.toAbsolutePath())));
            StatusUpdaterImpl status = new StatusUpdaterImpl();
            projectFileCheckService.checkFile(status, model).get(); // here we need to wait
            if (StringUtils.isNoneEmpty(status.getError())) {
                throw new IllegalStateException(status.getError());
            }
            FileCheckResult result = status.getResult();
            if (result == null) {
                throw new IllegalStateException("no validation result");
            }

            List<RewardVariable> rewardVariables = new ArrayList<>();
            List<Observation> observationList = new ArrayList<>();

            AnylogicFileCheckResult alResult = AnylogicFileCheckResult.class.cast(result);
            rewardVariables = ModelUtils.convertToRewardVariables(model.getId(), alResult.getRewardVariableNames(), alResult.getRewardVariableTypes());
            observationList = ModelUtils.convertToObservations(alResult.getObservationNames(), alResult.getObservationTypes());
            model.setNumberOfObservations(alResult.getNumObservation());
            model.setRewardVariablesCount(rewardVariables.size());
            model.setModelType(ModelType.fromName(alResult.getModelType()).getValue());
            model.setNumberOfAgents(alResult.getNumberOfAgents());

            if (project == null) {
                final LocalDateTime now = LocalDateTime.now();
                Project newProject = new Project();
                newProject.setName("AL-Upload-" + DateTimeFormatter.ISO_DATE_TIME.format(now));
                newProject.setPathmindUserId(pmUser.getUserId());
                long newProjectId = projectDAO.createNewProject(newProject);
                newProject.setId(newProjectId);
                log.info("created project {}", newProjectId);
                project = newProject;
            }

            modelService.addDraftModelToProject(model, project.getId(), "");
            log.info("created model {}", model.getId());
            RewardVariablesUtils.copyGoalsFromPreviousModel(rewardVariableDAO, modelDAO, model.getProjectId(), model.getId(), rewardVariables);
            rewardVariableDAO.updateModelAndRewardVariables(model, rewardVariables);
            observationDAO.updateModelObservations(model.getId(), observationList);

            Experiment experiment = modelService.resumeModelCreation(model, "");
            Long experimentId = experiment.getId();
            log.info("created experiment {}", experimentId);
            URI experimentUri = UriComponentsBuilder.fromHttpUrl(webappDomainUrl)
                    .path("newExperiment").path("/{experimentId}")
                    .buildAndExpand(Map.of("experimentId", experimentId))
                    .toUri();

            return ResponseEntity.status(HttpStatus.CREATED).location(experimentUri).build();
        } catch (Exception e) {
            log.error("failed to get file from AL", e);
            String location = modelCheckFailedHelpUrl;
            String errorMessage = StringUtils.trimToEmpty(e.getMessage());
//            if (errorMessage.startsWith(INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS)) {
//                errorMessage = INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS;
//                location = projectFileCheckService.getConvertModelsToSupportLatestVersionURL();
//            }
            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).body(errorMessage);
        }

    }

    @Getter
    public static class StatusUpdaterImpl implements StatusUpdater {

        private String error;
        private FileCheckResult result;

        @Override
        public void updateStatus(double percentage) {
            // ~ no op
        }

        @Override
        public void updateError(String error) {
            this.error = error;
        }

        @Override
        public void fileSuccessfullyVerified(FileCheckResult result) {
            this.result = result;
        }
    }

}
