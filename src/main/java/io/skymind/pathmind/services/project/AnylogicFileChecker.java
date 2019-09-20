package io.skymind.pathmind.services.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.skymind.pathmind.utils.FileUtils;
import org.springframework.util.FileSystemUtils;

public class AnylogicFileChecker implements FileChecker {
    private static final Logger log = LogManager.getLogger(AnylogicFileChecker.class);
    private String uuid = UUID.randomUUID().toString();
    private File jarTempDir = null;

    @Override
    public FileCheckResult performFileCheck(File file) {
        log.info("{} :- performFileCheck Started", uuid);
        File unZippedJar;
        AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();
        anylogicFileCheckResult.setFileCheckComplete(false);
        try {
            //To check the file exist and does the server have permission to read
            if (file.exists() && file.isFile() && file.canRead() && file.canExecute()) {
                log.info("File exists and it is readable:");
                unZippedJar = checkZipFile(file, anylogicFileCheckResult);
                if (unZippedJar != null) {
                    checkJarFile(unZippedJar, anylogicFileCheckResult);
                    if (anylogicFileCheckResult.isModelJarFilePresent())
                        checkHelpers(unZippedJar, anylogicFileCheckResult);
                }
                if (unZippedJar == null && anylogicFileCheckResult.isCorrectFileType()) {
                    log.error("model.jar does not exist");
                }
            } else {
                log.error("File does not exist or no read permission");
            }
        } catch (Exception e) {
            log.error("Exception in checking jar file ", e);
        } finally {
            anylogicFileCheckResult.setFileCheckComplete(true);
            if (jarTempDir != null) deleteTempDirectory();
        }
        log.info("{} :- performFileCheck Completed", uuid);
        return anylogicFileCheckResult;
    }

    private File checkZipFile(File file, AnylogicFileCheckResult anylogicFileCheckResult) throws IOException {
        log.info("{} :- CheckZip File Started", uuid);
        String searchFileName = "model.jar";
        // To Check if the Zip file is a valid
        File unZippedJar = null;
        try (ZipFile zipFile = new ZipFile(file)) {
            anylogicFileCheckResult.setCorrectFileType(true);
            Enumeration<?> enu = zipFile.entries();
            List<String> fileNameList = new ArrayList<>();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                log.info(zipEntry.getName());
                fileNameList.add(zipEntry.getName());
                if (zipEntry.getName().toLowerCase().indexOf(searchFileName) != -1) {
                    unZippedJar = unzipFile(file, searchFileName);
                    log.debug("unzipped jar path {} :-", unZippedJar.getAbsolutePath());
                }
            }
            anylogicFileCheckResult.setZipContentFileNames(fileNameList);
        } catch (ZipException ioe) {
            log.error("Invalid zip file :", ioe);
        }
        log.info("{} :- CheckZip File Completed", uuid);
        return unZippedJar;
    }


    // To Check if the model.jar is a valid
    private void checkJarFile(File unZippedJar, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkJarFile Started", uuid);
        // To Check if the Jar file is a valid
        try (ZipFile jarFile = new ZipFile(unZippedJar)) {
            anylogicFileCheckResult.setModelJarFilePresent(true);
        } catch (ZipException ioe) {
            log.error("Error opening jar file", ioe);
            anylogicFileCheckResult.setModelJarFilePresent(false);
        } catch (IOException e) {
            log.error("Error opening jar file", e);
            anylogicFileCheckResult.setModelJarFilePresent(false);
        }
        log.info("{} :- checkJarFile Completed", uuid);
    }

    // To check the existence of pathmind helpers check
    //Used static value for file path for unjarred model
    private void checkHelpers(File file, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkHelpers Started", uuid);
        try {
            File unJarred = extractArchive(file);
            List<String> listOfFiles = FileUtils.listFiles(unJarred.toString());
            ByteCodeAnalyzer byteCodeAnalyzer = new ByteCodeAnalyzer();
            List<String> listOfHelpers = byteCodeAnalyzer.byteParser(listOfFiles);
            anylogicFileCheckResult.setDefinedHelpers(listOfHelpers);
        } catch (IOException ioe) {
            log.error("Error unJarred jar file", ioe);
        }
        log.info("{} :- checkHelpers Completed", uuid);
    }


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
                if (zipEntry.getName().toLowerCase().indexOf(searchFileName) != -1) {
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

    private File extractArchive(File archiveFile) {
        log.info("{} :- extractArchive file Started", uuid);
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

