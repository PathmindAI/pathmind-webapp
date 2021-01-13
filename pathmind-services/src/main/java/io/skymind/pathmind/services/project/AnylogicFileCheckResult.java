package io.skymind.pathmind.services.project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;


@Slf4j
@Getter
@Setter
@ToString
public class AnylogicFileCheckResult implements FileCheckResult<Hyperparams> {

    private boolean fileCheckComplete;
    private boolean correctFileType;
    private boolean modelJarFilePresent;
    private List<String> definedHelpers = new ArrayList<>();
    private Hyperparams params;
    private List<AnyLogicModelInfo> modelInfos = new ArrayList<>();
    private String rlPlatform;

    @Override
    public boolean isFileCheckSuccessful() {
        boolean isAllSuccessful = isCorrectFileType() && isModelJarFilePresent() && isHelperPresent() && isHelperUnique() && getPriorityModelInfo() != null && isValidRLPlatform();
        if (!isAllSuccessful) {
            log.info("Correct File Type: {}, Model Jar Present: {}, Helper Present: {}, Helper Unique: {}, Helper: {}, Models: {}, RL Platform: {}",
                    isCorrectFileType(), isModelJarFilePresent(), isHelperPresent(), isHelperUnique(), definedHelpers, modelInfos, rlPlatform);
        }
        return isAllSuccessful;
    }

    @Override
    public boolean isHelperPresent() {
        return !CollectionUtils.isEmpty(definedHelpers);
    }

    @Override
    public boolean isHelperUnique() {
        if (this.definedHelpers.size() == 1) {
            return true;
        } else {
            log.info("Helper classes is not unique : {}", this.definedHelpers);
            return false;
        }
    }

    @Override
    public boolean isValidRLPlatform() {
        if (rlPlatform == null) {
            // if there's no information of `ReinforcementLearningPlatform`
            // it's Simulation model
            return true;
        } else if (rlPlatform.endsWith("PathmindLearningPlatform")) {
            // if `ReinforcementLearningPlatform` set to PM platform
            // it's valid RLExperiment model
            return true;
        } else {
            // unless '`ReinforcementLearningPlatform`' is set to PM platform
            // it's for another platform such as MS
            return false;
        }
    }

    public AnyLogicModelInfo getPriorityModelInfo() {
        AnyLogicModelInfo priorityModelInfo;
        if (modelInfos.size() > 1) {
            // Simulation has a higher priority than RLExperiment when they exist together
            List<AnyLogicModelInfo> simulations = modelInfos.stream()
                    .filter(m -> m.getExperimentType().equals(AnyLogicModelInfo.ExperimentType.Simulation))
                    .collect(Collectors.toList());

            // if there are more than two simulations, the simulation that has "Simulation" is higher priority
            if (simulations.size() > 1) {
                priorityModelInfo = simulations.stream()
                        .filter(m -> m.getExperimentClass().endsWith("/Simulation"))
                        .findFirst().orElse(null);
            } else {
                priorityModelInfo = simulations.get(0);
            }
        } else {
            return modelInfos.get(0);
        }

        return priorityModelInfo;
    }
}
