package io.skymind.pathmind.services.project;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
public class AnylogicFileCheckResult implements FileCheckResult {
    private boolean fileCheckComplete;
    private boolean correctFileType;
    private boolean modelJarFilePresent;
    private List<String> definedHelpers = new ArrayList<>();
    private int numObservation;
    private String rewardVariableFunction;
    private List<String> rewardVariableNames;
    private List<String> rewardVariableTypes;
    private List<String> observationNames;
    private List<String> observationTypes;
    private String modelType;
    private int numberOfAgents;

    @Override
    public boolean isFileCheckSuccessful() {
        boolean isAllSuccessful = isCorrectFileType() && isModelJarFilePresent() && isHelperPresent() && isHelperUnique();
        if (!isAllSuccessful) {
            log.info("Correct File Type: {}, Model Jar Present: {}, Helper Present: {}, Helper Unique: {}",
                isCorrectFileType(), isModelJarFilePresent(), isHelperPresent(), isHelperUnique());
        }
        return isAllSuccessful;
    }

    @Override
    public boolean isHelperPresent() {
        if (this.definedHelpers.size() > 0) {
            return true;
        } else {
            return false;
        }
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
}
