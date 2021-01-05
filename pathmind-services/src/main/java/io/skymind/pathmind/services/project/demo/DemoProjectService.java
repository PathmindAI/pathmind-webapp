package io.skymind.pathmind.services.project.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.experiment.ExperimentService;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.ProjectType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoProjectService {

    private final ExperimentService experimentService;
    private final ProjectDAO projectDAO;
    private final ExperimentDAO experimentDAO;

    public Experiment createExperiment(ExperimentManifest manifest, long userId) throws Exception {

        Path tmp = Files.createTempFile("pm-demo", "model");
        Request.Get(manifest.getModelUrl()).execute().saveContent(tmp.toFile());
        ModelBytes bytes = ModelBytes.of(Files.readAllBytes(tmp));

        Experiment experiment = experimentService.createExperimentFromModelBytes(bytes, () -> {
            List<Project> projects = projectDAO.getProjectsForUser(userId, ProjectType.DEMO);
            if (projects.isEmpty()) {
                final LocalDateTime now = LocalDateTime.now();
                Project newProject = new Project();
                newProject.setProjectType(ProjectType.DEMO);
                newProject.setName("Demo Projects by Pathmind");
                newProject.setPathmindUserId(userId);
                newProject.setDateCreated(now);
                newProject.setLastActivityDate(now);
                long newProjectId = projectDAO.createNewProject(newProject);
                newProject.setId(newProjectId);
                log.info("created demo project {}", newProjectId);
                return newProject;
            } else {
                return projects.get(0);
            }
        });

        experiment.setRewardFunction(manifest.getRewardFunction());
        experimentDAO.updateRewardFunction(experiment);

        // TODO: goals ...

        return experiment;

    }

}
