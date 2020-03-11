package io.skymind.pathmind.services.project;

import java.util.List;

public interface FileCheckResult {

    boolean isFileCheckComplete();

    boolean isFileCheckSuccessful();

    boolean isCorrectFileType();

    boolean isModelJarFilePresent();

    boolean isHelperPresent();

    boolean isHelperUnique();

    List<String> getZipContentFileNames();

    List<String> getDefinedHelpers();

    void setCorrectFileType(boolean correctFileType);

    void setModelJarFilePresent(boolean modelJarFilePresent);

    void setZipContentFileNames(List<String> zipContentFileNames);

    void setDefinedHelpers(List<String> definedHelpers);

    void setFileCheckComplete(boolean fileCheckComplete);
}
