package io.skymind.pathmind.services.project;

import java.util.List;

public interface FileCheckResult {

    public boolean isFileCheckComplete();

    public boolean isFileCheckSuccessful();

    public boolean isCorrectFileType();

    public boolean isModelJarFilePresent();

    public boolean isHelperPresent();

    public boolean isHelperUnique();

    public List<String> getZipContentFileNames();

    public List<String> getDefinedHelpers();

    public void setCorrectFileType(boolean correctFileType);

    public void setModelJarFilePresent(boolean modelJarFilePresent);

    public void setZipContentFileNames(List<String> zipContentFileNames);

    public void setDefinedHelpers(List<String> definedHelpers);

    public void setFileCheckComplete(boolean fileCheckComplete);
}
