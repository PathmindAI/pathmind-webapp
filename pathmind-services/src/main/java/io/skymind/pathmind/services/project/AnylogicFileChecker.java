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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;

/*To validate the model.jar uploaded by the user*/
@Slf4j
public class AnylogicFileChecker implements FileChecker {

    private String uuid = UUID.randomUUID().toString();
    private File jarTempDir = null;

    @Override
    public FileCheckResult performFileCheck(StatusUpdater statusUpdater, File file) {
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

            // 1. check if file a zip
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
            List<File> unZippedJars = checkZipFile(zipFile, anylogicFileCheckResult);
            statusUpdater.updateStatus(0.10);

            // 4. check if model.jar exists
            if (CollectionUtils.isEmpty(unZippedJars) || !containsAnyValidZipFile(unZippedJars)) {
                log.error("model.jar does not exist");
                statusUpdater.updateError("model.jar does not exist");
                return anylogicFileCheckResult;
            }

            anylogicFileCheckResult.setModelJarFilePresent(true);
            statusUpdater.updateStatus(0.30);

            //Check for PathmindHelper class instance in uploaded model.jar
            List<String> helpers = checkHelpers(unZippedJars);
            anylogicFileCheckResult.setDefinedHelpers(helpers);
            statusUpdater.updateStatus(0.50);

            if (anylogicFileCheckResult.isHelperPresent()) {
                statusUpdater.updateStatus(0.90);
            } else {
                log.error("model.jar does not have PathmindHelper class");
                statusUpdater.updateError("model.jar does not have PathmindHelper class");
            }

        } catch (Exception e) {
            log.error("Exception in checking jar file ", e);
            statusUpdater.updateError("Exception in checking jar file: " + e.getMessage());
        } finally {
            anylogicFileCheckResult.setFileCheckComplete(true);
            if (jarTempDir != null) {
                deleteTempDirectory();
            }
        }
        log.info("{} :- performFileCheck Completed", uuid);
        return anylogicFileCheckResult;
    }

    /* To check whether zip file is valid or not, if valid it returns unzipped temp directory */
    List<File> checkZipFile(ZipFile zipFile, AnylogicFileCheckResult anylogicFileCheckResult) throws IOException {
        log.info("{} :- CheckZip File Started", uuid);
        List<File> unZippedJars = new ArrayList<>();

        Path tempPath = Files.createTempDirectory(uuid);
        jarTempDir = new File(String.valueOf(tempPath));


        Enumeration<?> enu = zipFile.entries();
        List<String> fileNameList = new ArrayList<>();

        while (enu.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enu.nextElement();
            log.info("Content of Zip file : {} ", zipEntry.getName());
            log.debug("name:- {} | size:- {} | compressed size:- {}", zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize());
            fileNameList.add(zipEntry.getName());

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
        anylogicFileCheckResult.setZipContentFileNames(fileNameList);

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
    List<String> checkHelpers(List<File> unZippedJars) {
        log.info("{} :- checkHelpers Started", uuid);
        return unZippedJars.stream().flatMap(unZippedJar -> {
            try {
                File unJarred = extractArchive(unZippedJar);
                List<String> listOfFiles = FileUtils.listFiles(unJarred.toString());
                ByteCodeAnalyzer byteCodeAnalyzer = new ByteCodeAnalyzer();
                return byteCodeAnalyzer.byteParser(listOfFiles).stream();
            } catch (IOException ioe) {
                log.error("Error unJarred jar file", ioe);
                return Stream.empty();
            }
        }).collect(Collectors.toList());
    }

    /*To extract the archive file (mode.jar) contents inside unzipped temp directory*/
    private File extractArchive(File archiveFile) {
        log.info("{} :- extractArchive Started", uuid);
        File destDir = new File(archiveFile.getParent());

        try (JarFile jar = new JarFile(archiveFile)) {
            Enumeration<JarEntry> enumEntries = jar.entries();

            while (enumEntries.hasMoreElements()) {
                JarEntry file = enumEntries.nextElement();
                File fileDir = new File(destDir + File.separator + file.getName());

                if (!fileDir.exists()) {
                    fileDir.getParentFile().mkdirs();
                    fileDir = new File(destDir, file.getName());
                }
                if (file.isDirectory()) {
                    continue;
                }
                try (InputStream inputStream = jar.getInputStream(file);
                     FileOutputStream fileOutputStream = new FileOutputStream(fileDir)) {
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

    /*To delete the unzipped temp directory*/
    private void deleteTempDirectory() {
        //get parent folder of model.jar
        File file = new File(jarTempDir.getParent());
        //Delete files recursively
        boolean result = FileSystemUtils.deleteRecursively(file);
        if (!result) {
            log.error("error in folder delete");
        }
    }
}