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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;

/*To validate the model.jar uploaded by the user*/
@Slf4j
public class AnylogicFileChecker implements FileChecker {

    private String uuid = UUID.randomUUID().toString();
    private File jarTempDir = null;

    @Override
    public FileCheckResult performFileCheck(StatusUpdater statusUpdater, File file) {
        log.info("{} :- performFileCheck Started", uuid);
        List<File> unZippedJars;
        AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();
        anylogicFileCheckResult.setFileCheckComplete(false);

        try {
            //To check the file exist and does the server have permission to read
            if (file.exists() && file.isFile() && file.canRead()) {
                log.info("Uploaded file exists and it is readable");
                //To check a Zip file and if it is a valid file extract it in to the temporary folder
                unZippedJars = checkZipFile(file, anylogicFileCheckResult);
                statusUpdater.updateStatus(0.10);

                if (unZippedJars != null && unZippedJars.size() > 0) {
                    //Passing unzipped jar to check whether it is valid or not
                    checkJarFile(unZippedJars, anylogicFileCheckResult);
                    statusUpdater.updateStatus(0.30);

                    if (anylogicFileCheckResult.isModelJarFilePresent()) {
                        //Check for PathmindHelper class instance in uploaded model.jar
                        checkHelpers(unZippedJars, anylogicFileCheckResult);
                        statusUpdater.updateStatus(0.50);

                        if (anylogicFileCheckResult.isHelperPresent()) {
                            statusUpdater.updateStatus(0.90);
                        } else {
                            log.error("model.jar does not have PathmindHelper class");
                            statusUpdater.updateError("model.jar does not have PathmindHelper class");
                        }
                    }
                } else {
                    if (anylogicFileCheckResult.isCorrectFileType()) {
                        log.error("model.jar does not exist");
                        statusUpdater.updateError("model.jar does not exist");
                    } else {
                        log.error("Uploaded file could not be unzipped.");
                        statusUpdater.updateError("Uploaded file could not be unzipped.");
                    }
                }
            } else {
                log.error("Uploaded file does not exist or no read permission");
                statusUpdater.updateError("Uploaded file does not exist or no read permission");
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
    List<File> checkZipFile(File file, AnylogicFileCheckResult anylogicFileCheckResult) throws IOException {
        log.info("{} :- CheckZip File Started", uuid);
        List<File> unZippedJars = new ArrayList<>();
        boolean isValidZip;

        try (InputStream iStream = new FileInputStream(file)) {
            isValidZip = FileUtils.detectDocType(iStream);
        }
        if (isValidZip) {
            try (ZipFile zipFile = new ZipFile(file)) {
                anylogicFileCheckResult.setCorrectFileType(true);
                Enumeration<?> enu = zipFile.entries();
                List<String> fileNameList = new ArrayList<>();

                while (enu.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                    log.info("Content of Zip file : {} ", zipEntry.getName());
                    fileNameList.add(zipEntry.getName());

                    Path objPath = Paths.get(zipEntry.getName());
                    Path modelFileName = objPath.getFileName();

                    if (ModelUtils.isModelFile(zipEntry.getName())) {
                        unZippedJars.add(unzipFile(file, modelFileName.toString()));
                        log.debug("unzipped jar path {} :-", objPath);
                    }
                }
                anylogicFileCheckResult.setZipContentFileNames(fileNameList);
            } catch (ZipException ioe) {
                log.error("Invalid input file format :", ioe);
            }
        } else {
            log.error("Invalid input file format :");
        }

        log.info("{} :- CheckZip File Completed", uuid);
        return unZippedJars;
    }

    // To Check if the model.jar is a valid
    void checkJarFile(List<File> unZippedJars, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkJarFile Started", uuid);
        AtomicBoolean isModelJarFilePresent = new AtomicBoolean(false);

        unZippedJars.stream().forEach(unZippedJar -> {
            try (ZipFile jarFile = new ZipFile(unZippedJar)) {
                isModelJarFilePresent.set(true);
            } catch (ZipException ioe) {
                log.error("Error opening jar file", ioe);
            } catch (IOException e) {
                log.error("Error opening jar file", e);
            }
        });

        anylogicFileCheckResult.setModelJarFilePresent(isModelJarFilePresent.get());
        log.info("{} :- checkJarFile Completed", uuid);
    }

    // To check the existence of pathmind helpers check
    void checkHelpers(List<File> unZippedJars, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkHelpers Started", uuid);

        List<String> listOfHelpers = new ArrayList<>();

        unZippedJars.stream().forEach(unZippedJar -> {
            try {
                File unJarred = extractArchive(unZippedJar);
                List<String> listOfFiles = FileUtils.listFiles(unJarred.toString());
                ByteCodeAnalyzer byteCodeAnalyzer = new ByteCodeAnalyzer();
                listOfHelpers.addAll(byteCodeAnalyzer.byteParser(listOfFiles));
            } catch (IOException ioe) {
                log.error("Error unJarred jar file", ioe);
            }
        });

        anylogicFileCheckResult.setDefinedHelpers(listOfHelpers);

        log.info("{} :- checkHelpers Completed", uuid);
    }

    /* To check whether model.jar exist in zipped file or not, if exist it will create temp directory and extract files and return dir to its calling function */
    private File unzipFile(File zippedFile, String searchFileName) {
        log.info("{} :- unzipFile Started", uuid);
        try (ZipFile zipFile = new ZipFile(zippedFile)) {
            Enumeration<?> enu = zipFile.entries();

            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                log.debug("name:- {} | size:- {} | compressed size:- {}\n",
                        name, size, compressedSize);

                Path objPath = Paths.get(name);
                Path modelFileName = objPath.getFileName();

                if (modelFileName.toString().toLowerCase().equalsIgnoreCase(searchFileName)) {
                    Path tempPath = Files.createTempDirectory(uuid);
                    jarTempDir = new File(String.valueOf(tempPath));

                    if (!jarTempDir.exists()) {
                        jarTempDir.mkdir();
                    }

                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    jarTempDir = new File(jarTempDir + "/" + searchFileName);
                    log.debug("unzipped jar path {} :-", jarTempDir);

                    FileOutputStream fos = new FileOutputStream(jarTempDir);
                    byte[] bytes = new byte[1024];
                    int length;

                    while ((length = inputStream.read(bytes)) >= 0) {
                        fos.write(bytes, 0, length);
                    }
                    inputStream.close();
                    fos.close();
                }
            }
        } catch (IOException e) {
            log.error("error during unzipping file :", e);
        }
        log.info("{} :- unzipFile Completed", uuid);
        return jarTempDir;
    }

    /*To extract the archive file (mode.jar) contents inside unzipped temp directory*/
    private File extractArchive(File archiveFile) {
        log.info("{} :- extractArchive Started", uuid);
        File destDir = new File(archiveFile.getParent());

        try (JarFile jar = new JarFile(archiveFile)) {
            Enumeration enumEntries = jar.entries();

            while (enumEntries.hasMoreElements()) {
                JarEntry file = (JarEntry) enumEntries.nextElement();
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