package io.skymind.pathmind.services.project;

import java.util.List;

public interface FileCheckResult<T> {

    boolean isFileCheckComplete();

    void setFileCheckComplete(boolean fileCheckComplete);

    boolean isFileCheckSuccessful();

    boolean isCorrectFileType();

    void setCorrectFileType(boolean correctFileType);

    boolean isModelJarFilePresent();

    void setModelJarFilePresent(boolean modelJarFilePresent);

    boolean isHelperPresent();

    boolean isHelperUnique();

    List<String> getDefinedHelpers();

    void setDefinedHelpers(List<String> definedHelpers);

    boolean isValidRLPlatform();

    T getParams();

    void setParams(T params);
}
