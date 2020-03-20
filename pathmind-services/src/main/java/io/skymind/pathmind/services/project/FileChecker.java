package io.skymind.pathmind.services.project;

import java.io.File;
import java.io.IOException;

public interface FileChecker {

    public FileCheckResult performFileCheck(StatusUpdater statusUpdater, File file) throws IOException;

}
