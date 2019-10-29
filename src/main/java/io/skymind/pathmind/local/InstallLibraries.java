package io.skymind.pathmind.local;

import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.cloud.rescale.api.RescaleRestApiClient;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.FileReference;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.RescaleFile;
import io.skymind.pathmind.services.training.versions.RescaleFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class InstallLibraries {
    private static final Logger log = LoggerFactory.getLogger(InstallLibraries.class);

    private final RescaleFileManager rescaleFileManager = RescaleFileManager.getInstance();
    private final RescaleRestApiClient client;

    private final static String LOCAL_LIBRARY_PATH = System.getProperty("user.home") + File.separator + ".pathmind";

    public InstallLibraries(RescaleRestApiClient client) {
        this.client = client;

        File libraryPath = new File(LOCAL_LIBRARY_PATH);

        if  (!libraryPath.exists()) {
            log.info("created local lib path " + libraryPath.getAbsolutePath());
            libraryPath.mkdirs();
        }
    }

    public static String getLocalLibraryPath() {
        return LOCAL_LIBRARY_PATH;
    }

    public void CheckLibraries(ExecutionEnvironment executionEnvironment) {
        log.info("check local libraries");
        List<FileReference> files = rescaleFileManager.getFiles(executionEnvironment.getAnylogicVersion());
        files.addAll(rescaleFileManager.getFiles(executionEnvironment.getRllibVersion()));
        files.addAll(rescaleFileManager.getFiles(executionEnvironment.getPathmindHelperVersion()));

        files.parallelStream()
                .filter(fileReference -> !fileReference.getId().equals("LZAENb"))  // won't download conda
                .forEach(fileReference -> {
                    RescaleFile file = client.fileMeta(fileReference.getId());
                    File localFile = new File(LOCAL_LIBRARY_PATH, file.getName());

                    if (!localFile.exists()) {
                        log.info(localFile.getName() + " not exist. It will be downloaded from rescale sever");
                        downloadFileToLocal(file.getId(), localFile);
                    } else if (!file.getMd5().equals(getMD5(localFile))) {
                        log.info(localFile.getName() + " checksum is not equal. It will be downloaded from rescale sever");
                        localFile.delete();
                        downloadFileToLocal(file.getId(), localFile);
                    } else {
                        log.info(localFile.getName() + " exist.");
                        return;
                    }
                });

    }

    private void downloadFileToLocal(String fileId, File localFile) {
        byte[] contents = client.fileContents(fileId);

        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            fos.write(contents);
            extract(localFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getMD5(File localFile) {
        try (FileInputStream fis = new FileInputStream(localFile)) {
            return DigestUtils.md5DigestAsHex(fis);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private void extract(File localFile) throws IOException {
        // todo below code should be replaced to programmatic code
        if (localFile.getName().endsWith(".zip")) {
            Runtime.getRuntime().exec("unzip " + localFile.getAbsolutePath() + " -d " + localFile.getParent());
        } else if (localFile.getName().endsWith(".tar.gz")) {
            Runtime.getRuntime().exec("tar -C " + localFile.getParent() + " -xzf " + localFile.getAbsolutePath());
        }
    }

}
