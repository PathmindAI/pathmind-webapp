package io.skymind.pathmind.services.project.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.experiment.ExperimentService;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Project;
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

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

        Path tmp = Files.createTempFile("pm-demo", "model");
        Request.Get(manifest.getModelUrl()).execute().saveContent(tmp.toFile());
        ModelBytes bytes = ModelBytes.of(Files.readAllBytes(tmp));

        Experiment experiment = experimentService.createExperimentFromModelBytes(bytes, () -> {
            final LocalDateTime now = LocalDateTime.now();
            Project newProject = new Project();
            newProject.setName(MessageFormat.format("Demo: {0} ({1})", manifest.getName(), now.format(format)));
            newProject.setPathmindUserId(userId);
            newProject.setDateCreated(now);
            newProject.setLastActivityDate(now);
            long newProjectId = projectDAO.createNewProject(newProject);
            newProject.setId(newProjectId);
            log.info("created demo project {}", newProjectId);
            return newProject;
        });

        experiment.setRewardFunction(manifest.getRewardFunction());
        experimentDAO.updateRewardFunction(experiment);

        // TODO: goals ...

        return experiment;

    }

}
