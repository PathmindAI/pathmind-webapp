package io.skymind.pathmind.services.experiment;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
import io.skymind.pathmind.services.model.analyze.ModelFileVerifier;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.Hyperparams;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperimentService {


    private final ModelDAO modelDAO;

    private final ModelService modelService;

    private final RewardVariableDAO rewardVariableDAO;

    private final ObservationDAO observationDAO;

    private final ModelFileVerifier modelFileVerifier;

    private final ProjectFileCheckService projectFileCheckService;

    public Experiment createExperimentFromModelBytes(ModelBytes modelBytes, Supplier<Project> projectSupplier) throws Exception {
        return createExperimentFromModelBytes(modelBytes, new NoOpStatusUpdaterImpl(), projectSupplier);
    }


    public Experiment createExperimentFromModelBytes(ModelBytes modelBytes,
                                                     StatusUpdater<AnylogicFileCheckResult> status, // todo: get rid of status updater
                                                     Supplier<Project> projectSupplier) throws Exception {
        Model model = new Model();
        byte[] bytes = modelFileVerifier.assureModelBytes(modelBytes).getBytes();
        model.setFile(bytes);

        projectFileCheckService.checkFile(status, model).get(); // here we need to wait
        if (StringUtils.isNoneEmpty(status.getError())) {
            throw new ModelCheckException(status.getError());
        }
        AnylogicFileCheckResult result = status.getResult();
        if (result == null) {
            throw new ModelCheckException("No validation result");
        }

        List<RewardVariable> rewardVariables = new ArrayList<>();
        List<Observation> observationList = new ArrayList<>();

        Hyperparams alResult = result.getParams();
        rewardVariables = ModelUtils.convertToRewardVariables(model.getId(), alResult.getRewardVariableNames(), alResult.getRewardVariableTypes());
        observationList = ModelUtils.convertToObservations(alResult.getObservationNames(), alResult.getObservationTypes());
        model.setNumberOfObservations(alResult.getNumObservation());
        model.setRewardVariablesCount(rewardVariables.size());
        model.setModelType(ModelType.fromName(alResult.getModelType()).getValue());
        model.setNumberOfAgents(alResult.getNumberOfAgents());

        Project project = projectSupplier.get();



        modelService.addDraftModelToProject(model, project.getId(), "");
        log.info("created model {}", model.getId());
        RewardVariablesUtils.copyGoalsFromPreviousModel(rewardVariableDAO, modelDAO, model.getProjectId(), model.getId(), rewardVariables);
        rewardVariableDAO.updateModelAndRewardVariables(model, rewardVariables);
        observationDAO.updateModelObservations(model.getId(), observationList);

        Experiment experiment = modelService.resumeModelCreation(model, "");
        return experiment;
    }

}
