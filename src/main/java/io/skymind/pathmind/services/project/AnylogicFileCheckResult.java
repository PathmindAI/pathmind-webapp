package io.skymind.pathmind.services.project;

import java.util.List;

public class AnylogicFileCheckResult implements FileCheckResult{

    private boolean fileCheckComplete;
    private boolean correctFileType;
    private boolean modelJarFilePresent;
    private List<String> zipContentFileNames;
    private List<String> definedHelpers;

    @Override
    public boolean isFileCheckComplete() {

        return fileCheckComplete;
    }

    @Override
    public boolean isFileCheckSuccessful() {
        return false;
    }

    @Override
    public boolean isCorrectFileType() {
        return this.correctFileType;
    }

    @Override
    public boolean isModelJarFilePresent() {
        return this.modelJarFilePresent;
    }

    @Override
    public boolean isHelperPresent() {
        if(this.definedHelpers.size()>0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isHelperUnique() {
        if(this.definedHelpers.size()==1){
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
    public List<String> getDefinedHelpers() {
        return this.definedHelpers;
    }

    @Override
    public void setCorrectFileType(boolean correctFileType) { this.correctFileType = correctFileType;

    }

    @Override
    public void setModelJarFilePresent(boolean modelJarFilePresent) {this.modelJarFilePresent = modelJarFilePresent;}

    @Override
    public void setZipContentFileNames(List<String> zipContentFileNames) { this.zipContentFileNames = zipContentFileNames; }

    @Override
    public void setDefinedHelpers(List<String> definedHelpers) { this.definedHelpers = definedHelpers; }


}
