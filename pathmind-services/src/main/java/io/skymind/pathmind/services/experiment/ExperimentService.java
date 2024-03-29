package io.skymind.pathmind.services.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.SimulationParameterDAO;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
import io.skymind.pathmind.services.model.analyze.ModelFileVerifier;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.Hyperparams;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.StatusUpdater;
import io.skymind.pathmind.services.project.rest.dto.AnalyzeRequestDTO;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.constants.ObservationDataType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.shared.utils.ObservationUtils;
import io.skymind.pathmind.shared.utils.SimulationParameterUtils;
import io.skymind.pathmind.shared.utils.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.shared.utils.ZipUtils.entryContentExtractor;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperimentService {

    private final ModelDAO modelDAO;

    private final ExperimentDAO experimentDAO;

    private final ModelService modelService;

    private final RewardVariableDAO rewardVariableDAO;

    private final ObservationDAO observationDAO;

    private final SimulationParameterDAO simulationParameterDAO;

    private final ModelFileVerifier modelFileVerifier;

    private final ProjectFileCheckService projectFileCheckService;

    public Experiment createExperimentFromModelBytes(ModelBytes modelBytes, Supplier<Project> projectSupplier) throws Exception {
        return createExperimentFromModelBytes(
                modelBytes, new NoOpStatusUpdaterImpl(), projectSupplier, AnalyzeRequestDTO.ModelType.ANY_LOGIC,
                null, null, null, false
        );
    }

    public Experiment createExperimentFromModelBytes(
            ModelBytes modelBytes, Supplier<Project> projectSupplier, AnalyzeRequestDTO.ModelType type,
            String environment, String obsSelection, String rewFctName,
            boolean deployPolicyServerOnSuccess
    ) throws Exception {
        return createExperimentFromModelBytes(modelBytes, new NoOpStatusUpdaterImpl(), projectSupplier, type, environment,
                obsSelection, rewFctName, deployPolicyServerOnSuccess);
    }

    public Experiment createExperimentFromModelBytes(
            ModelBytes modelBytes, StatusUpdater<AnylogicFileCheckResult> status, // todo: get rid of status updater
            Supplier<Project> projectSupplier, AnalyzeRequestDTO.ModelType type, String environment,
            String obsSelection, String rewFctName,boolean deployPolicyServerOnSuccess
    ) throws Exception {
        Model model = new Model();

        switch (type) {
            case ANY_LOGIC: {
                byte[] bytes = modelFileVerifier.assureModelBytes(modelBytes).getBytes();
                model.setFile(bytes);

                projectFileCheckService.checkFile(status, model, type).get(); // here we need to wait
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

                // this is for policy server to support action masking model
                Observation actionMasking = null;
                if (alResult.isActionMask()) {
                    actionMasking = new Observation();
                    actionMasking.setVariable(Observation.ACTION_MASKING);
                    actionMasking.setDataTypeEnum(ObservationDataType.BOOLEAN_ARRAY);
                    actionMasking.setArrayIndex(0);
                    actionMasking.setMaxItems(alResult.getNumAction());
                }

                rewardVariables = ModelUtils.convertToRewardVariables(model.getId(), alResult.getRewardVariableNames(), alResult.getRewardVariableTypes());
                observationList = ModelUtils.convertToObservations(alResult.getObservationNames(), alResult.getObservationTypes(), actionMasking);
                model.setNumberOfObservations(alResult.getNumObservation());
                model.setRewardVariablesCount(rewardVariables.size());
                model.setModelType(ModelType.fromName(alResult.getModelType()).getValue());
                model.setNumberOfAgents(alResult.getNumberOfAgents());
                model.setActionmask(alResult.isActionMask());

                modelService.addDraftModelToProject(model, projectSupplier.get().getId(), "");
                log.info("created model {}", model.getId());
                RewardVariablesUtils.copyGoalsFromPreviousModel(rewardVariableDAO, modelDAO, model.getProjectId(), model.getId(), rewardVariables);
                rewardVariableDAO.updateModelAndRewardVariables(model, rewardVariables);
                observationDAO.updateModelObservations(model.getId(), observationList);

                List<SimulationParameter> simulationParameterList = SimulationParameterUtils.makeValidSimulationParameter(model.getId(), null, alResult.getSimulationParams());
                simulationParameterDAO.insertSimulationParameters(simulationParameterList);
                break;
            }
            case PYTHON: {
                model.setFile(modelBytes.getBytes());
                String reqId = "project_" + model.getProjectId();
                File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());
                FileUtils.writeByteArrayToFile(tempFile, model.getFile());
                HyperparametersDTO analysisResult = projectFileCheckService.getClient().analyze(tempFile, type, reqId, environment);
                if (StringUtils.isNotEmpty(analysisResult.getFailedSteps())) {
                    throw new ModelCheckException(analysisResult.getFailedSteps());
                }
                ModelType modelType = ModelType.fromName(analysisResult.getMode());
                model.setModelType(modelType.getValue());

                final List<Observation> obss = new ArrayList<>();
                if (ModelType.isPathmindModel(modelType)) {
                    try {
                        byte[] obsYaml = ZipUtils.processZipEntryInFile(
                                modelBytes.getBytes(), s -> s.endsWith("obs.yaml"),
                                entryContentExtractor()
                        );
                        obss.addAll(ObservationUtils.fromYaml(new String(obsYaml)));
                        model.setNumberOfObservations(obss.size());
                    } catch (Exception e) {
                        obss.clear();
                        log.error("Failed to extract observations for PM Model", e);
                    }
                }
                model.setPackageName(String.join(";", environment, obsSelection, rewFctName));

                modelService.addDraftModelToProject(model, projectSupplier.get().getId(), "");
                log.info("created model {}", model.getId());
                if (obss.size() > 0) {
                    observationDAO.updateModelObservations(model.getId(), obss);
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown type " + type);
            }
        }

        Experiment experiment = modelService.resumeModelCreation(model, "");
        if (deployPolicyServerOnSuccess && type == AnalyzeRequestDTO.ModelType.PYTHON) {
            experimentDAO.setDeployPolicyOnSuccess(experiment.getId(), true);
            experiment.setDeployPolicyOnSuccess(true);
        }
        return experiment;
    }

}
