package io.skymind.pathmind.services.project;

import io.skymind.pathmind.services.project.meta.PathmindMeta;

import java.util.List;

public class AnylogicFileCheckResult implements FileCheckResult {

    private boolean fileCheckComplete = false;
    private boolean correctFileType = false;
    private boolean modelJarFilePresent = false;
    private List<String> zipContentFileNames;
    private List<String> definedHelpers;
    private int numAction;
    private int numObservation;
    private String rewardVariableFunction;
    private int rewardVariablesCount;
    private PathmindMeta pathmindMeta;

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
        return isCorrectFileType() && isModelJarFilePresent() && isHelperPresent() && isHelperUnique();
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

    public int getNumAction() {
        return numAction;
    }

    public void setNumAction(int numAction) {
        this.numAction = numAction;
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

    public int getRewardVariablesCount() {
        return rewardVariablesCount;
    }

    public void setRewardVariablesCount(int rewardVariablesCount) {
        this.rewardVariablesCount = rewardVariablesCount;
    }

    public PathmindMeta getPathmindMeta() {
        return pathmindMeta;
    }

    public void setPathmindMeta(PathmindMeta pathmindMeta) {
        this.pathmindMeta = pathmindMeta;
    }
}
