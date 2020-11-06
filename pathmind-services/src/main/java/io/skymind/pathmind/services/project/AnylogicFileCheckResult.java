package io.skymind.pathmind.services.project;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AnylogicFileCheckResult implements FileCheckResult {

    private boolean fileCheckComplete;
    private boolean correctFileType;
    private boolean modelJarFilePresent;
    private List<String> zipContentFileNames;
    private List<String> definedHelpers = new ArrayList<>();
    private int numObservation;
    private String rewardVariableFunction;
    private List<String> rewardVariables;
    private List<String> observationNames;
    private String modelType;
    private int numberOfAgents;

    @Override
    public boolean isFileCheckComplete() {
        return fileCheckComplete;
    }

    @Override
    public void setFileCheckComplete(boolean fileCheckComplete) {
        this.fileCheckComplete = fileCheckComplete;
    }

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
    public boolean isCorrectFileType() {
        return this.correctFileType;
    }

    @Override
    public void setCorrectFileType(boolean correctFileType) {
        this.correctFileType = correctFileType;

    }

    @Override
    public boolean isModelJarFilePresent() {
        return this.modelJarFilePresent;
    }

    @Override
    public void setModelJarFilePresent(boolean modelJarFilePresent) {
        this.modelJarFilePresent = modelJarFilePresent;
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

    @Override
    public List<String> getZipContentFileNames() {
        return this.zipContentFileNames;
    }

    @Override
    public void setZipContentFileNames(List<String> zipContentFileNames) {
        this.zipContentFileNames = zipContentFileNames;
    }

    @Override
    public List<String> getDefinedHelpers() {
        return this.definedHelpers;
    }

    @Override
    public void setDefinedHelpers(List<String> definedHelpers) {
        this.definedHelpers = definedHelpers;
    }

    public int getNumObservation() {
        return numObservation;
    }

    public void setNumObservation(int numObservation) {
        this.numObservation = numObservation;
    }

    public String getRewardVariableFunction() {
        return rewardVariableFunction;
    }

    public void setRewardVariableFunction(String rewardVariableFunction) {
        this.rewardVariableFunction = rewardVariableFunction;
    }

    public List<String> getRewardVariables() {
        return rewardVariables;
    }

    public void setRewardVariables(List<String> rewardVariables) {
        this.rewardVariables = rewardVariables;
    }

    public List<String> getObservationNames() {
        return observationNames;
    }

    public void setObservationNames(List<String> observationNames) {
        this.observationNames = observationNames;
    }

    public String getModelType() {
        return this.modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }
}
