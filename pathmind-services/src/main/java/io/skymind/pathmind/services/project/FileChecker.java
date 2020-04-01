package io.skymind.pathmind.services.project;

import java.io.File;
import java.io.IOException;

public interface FileChecker {

    /**
     * performs file check whether it is valid zip file, it has proper contents
     *
     * @param statusUpdater
     * @param file
     * @return
     * @throws IOException
     */
    FileCheckResult performFileCheck(StatusUpdater statusUpdater, File file) throws IOException;

}
