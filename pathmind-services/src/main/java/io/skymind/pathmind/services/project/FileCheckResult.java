package io.skymind.pathmind.services.project;

import java.util.List;

public interface FileCheckResult<T> {

    boolean isFileCheckComplete();

    boolean isFileCheckSuccessful();

    boolean isCorrectFileType();

    boolean isModelJarFilePresent();

    boolean isHelperPresent();

    boolean isHelperUnique();

    List<String> getDefinedHelpers();

    void setCorrectFileType(boolean correctFileType);

    void setModelJarFilePresent(boolean modelJarFilePresent);

    void setDefinedHelpers(List<String> definedHelpers);

    void setFileCheckComplete(boolean fileCheckComplete);

    boolean isValidRLPlatform();

    T getParams();

    void setParams(T params);
}
