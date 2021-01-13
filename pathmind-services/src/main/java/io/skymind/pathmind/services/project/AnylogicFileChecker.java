package io.skymind.pathmind.services.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.CollectionUtils;

/*To validate the model.jar uploaded by the user*/
@Slf4j
public class AnylogicFileChecker implements FileChecker<Hyperparams> {

    private final String uuid = UUID.randomUUID().toString();
    private final File jarTempDir;

    public AnylogicFileChecker() throws IOException {
        Path tempPath = Files.createTempDirectory("pm-upload_" + uuid);
        jarTempDir = new File(String.valueOf(tempPath));
    }

    @Override
    public FileCheckResult<Hyperparams> performFileCheck(StatusUpdater statusUpdater, File file) {
        log.info("{} :- performFileCheck Started", uuid);

        AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();
        anylogicFileCheckResult.setFileCheckComplete(false);

        try {
            //1. To check the file exist and does the server have permission to read
            if (!(file.exists() && file.isFile() && file.canRead())) {
                log.error("Uploaded file does not exist or no read permission");
                statusUpdater.updateError("Uploaded file does not exist or no read permission");
                return anylogicFileCheckResult;
            }
            log.info("Uploaded file exists and it is readable");

            // 2. check if file a zip
            ZipFile zipFile;
            try (InputStream iStream = new FileInputStream(file)) {
                boolean isValidZip = FileUtils.detectDocType(iStream);
                if (!isValidZip) {
                    throw new ZipException("Rejected by detectDocType");
                }
                zipFile = new ZipFile(file);
            } catch (ZipException zex) {
                log.error("Invalid file format: expected zip");
                statusUpdater.updateError("Invalid file format: expected zip");
                return anylogicFileCheckResult;
            }
            anylogicFileCheckResult.setCorrectFileType(true);

            // 3. extract zipped jar files which look like model
            List<File> unZippedJars = extractModelFiles(zipFile);
            statusUpdater.updateStatus(0.10);

            // 4. check if model.jar exists
            if (CollectionUtils.isEmpty(unZippedJars) || !containsAnyValidZipFile(unZippedJars)) {
                log.error("model.jar does not exist");
                statusUpdater.updateError("model.jar does not exist");
                return anylogicFileCheckResult;
            }
            anylogicFileCheckResult.setModelJarFilePresent(true);
            statusUpdater.updateStatus(0.30);

            // 5. Check for PathmindHelper class instance in uploaded model.jar
            this.populateHelpersToResult(unZippedJars, anylogicFileCheckResult); // TODO: get rid anylogicFileCheckResult as an parameter
//            anylogicFileCheckResult.setDefinedHelpers(helpers);
            statusUpdater.updateStatus(0.50);

            if (!anylogicFileCheckResult.isHelperPresent()) {
                log.error("model.jar does not have PathmindHelper class");
                statusUpdater.updateError("model.jar does not have PathmindHelper class");
            }

            statusUpdater.updateStatus(0.90);

        } catch (Exception e) {
            log.error("Exception in checking jar file ", e);
            statusUpdater.updateError("Exception in checking jar file: " + e.getMessage());
        } finally {
            cleanup();
            anylogicFileCheckResult.setFileCheckComplete(true);
        }
        log.info("{} :- performFileCheck Completed", uuid);
        return anylogicFileCheckResult;
    }

    @Override
    public void cleanup() {
        org.apache.commons.io.FileUtils.deleteQuietly(jarTempDir);
    }

    List<File> extractModelFiles(ZipFile zipFile) throws IOException {
        log.info("{} :- CheckZip File Started", uuid);
        List<File> unZippedJars = new ArrayList<>();

        Enumeration<? extends ZipEntry> enu = zipFile.entries();

        while (enu.hasMoreElements()) {
            ZipEntry zipEntry = enu.nextElement();
            log.info("Content of Zip file : {} ", zipEntry.getName());
            log.debug("name:- {} | size:- {} | compressed size:- {}", zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize());

            Path objPath = Paths.get(zipEntry.getName());
            Path modelFileName = objPath.getFileName();

            if (ModelUtils.isModelFile(zipEntry.getName())) {
                // TODO: move to small function like extractSingleEntry
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                File extractedFile = new File(jarTempDir + "/" + modelFileName.toString());
                log.debug("unzipped jar path {}:", extractedFile);

                FileOutputStream fos = new FileOutputStream(extractedFile);
                byte[] bytes = new byte[1024];
                int length;

                while ((length = inputStream.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
                inputStream.close();
                fos.close();

                log.debug("unzipped jar path {}", extractedFile.getAbsolutePath());
                unZippedJars.add(extractedFile);
            }
        }

        log.info("{} :- CheckZip File Completed", uuid);
        return unZippedJars;
    }

    // To Check if the model.jar is a valid
    boolean containsAnyValidZipFile(List<File> unZippedJars) {
        log.info("{} :- checkJarFile", uuid);
        return unZippedJars.stream().map(unZippedJar -> {
            try {
                new ZipFile(unZippedJar);
                return true;
            } catch (IOException ioe) {
                return false;
            }
        }).anyMatch(BooleanUtils::isTrue);
    }

    // To check the existence of pathmind helpers check
    void populateHelpersToResult(List<File> unZippedJars, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkHelpers Started", uuid);

        unZippedJars.forEach(unZippedJar -> {
            try {
                File unJarred = unpackJar(unZippedJar);
                List<String> listOfFiles = FileUtils.listFiles(unJarred.toString());
                ByteCodeAnalyzer byteCodeAnalyzer = new ByteCodeAnalyzer(listOfFiles);
                byteCodeAnalyzer.byteParser();
                anylogicFileCheckResult.getDefinedHelpers().addAll(byteCodeAnalyzer.getPathmindHelperClasses());
                anylogicFileCheckResult.getModelInfos().addAll(byteCodeAnalyzer.getModels());
                anylogicFileCheckResult.setRlPlatform(byteCodeAnalyzer.getRlPlatform());
            } catch (IOException ioe) {
                log.error("Error unJarred jar file", ioe);
            }
        });

        log.info("{} :- checkHelpers Completed", uuid);
    }

    /*To extract the archive file (mode.jar) contents inside unzipped temp directory*/
    private File unpackJar(File archiveFile) {
        log.info("{} :- extractArchive Started", uuid);
        File destDir = new File(archiveFile.getParent());

        try (JarFile jar = new JarFile(archiveFile)) {
            Enumeration<JarEntry> enumEntries = jar.entries();

            while (enumEntries.hasMoreElements()) {
                JarEntry file = enumEntries.nextElement();
                File extractedFile = new File(destDir + File.separator + file.getName());

                if (!extractedFile.exists()) {
                    extractedFile.getParentFile().mkdirs();
                    extractedFile = new File(destDir, file.getName());
                }
                if (file.isDirectory()) {
                    continue;
                }
                try (InputStream inputStream = jar.getInputStream(file);
                     FileOutputStream fileOutputStream = new FileOutputStream(extractedFile)) {
                    while (inputStream.available() > 0) {
                        fileOutputStream.write(inputStream.read());
                    }
                } catch (Exception e) {
                    log.error("error while read/write jar files content", e);
                }
            }
        } catch (IOException e) {
            log.error("error while extract jar files", e);
        }
        return destDir;
    }

}