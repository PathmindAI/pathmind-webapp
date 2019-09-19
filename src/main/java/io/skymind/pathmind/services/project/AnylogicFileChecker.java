package io.skymind.pathmind.services.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.skymind.pathmind.utils.FileUtils;
import org.springframework.util.FileSystemUtils;

public class AnylogicFileChecker implements FileChecker {
    private static final Logger log = LogManager.getLogger(AnylogicFileChecker.class);
    private static final int BUFFER = 1024;
    private String uuid = UUID.randomUUID().toString();
    private File unZippedJar;
    private File jarTempDir = null;
    private String searchFileName = "model.jar";
    private Path tempPath = null;

    @Override
    public FileCheckResult performFileCheck(File file) {

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
                } else {
                    log.error("model.jar does not exist");
                }
            } else {
                log.error("File does not exist or no read permission");
            }
        } catch (Exception e) {
            log.error("Exception in checking jar file " + e);
        } finally {
            anylogicFileCheckResult.setFileCheckComplete(true);

            deleteTempDirectory();

        }
        anylogicFileCheckResult.setFileCheckComplete(true);
        return anylogicFileCheckResult;
    }


    private File checkZipFile(File file, AnylogicFileCheckResult anylogicFileCheckResult) {

        log.info("{} :- CheckZip File Started", uuid);

        // To Check if the Zip file is a valid
        ZipFile zipFile = null;
        File zippedFile = file;

        File unZippedJar = null;
        boolean isJarExist = false;
        try {
            zipFile = new ZipFile(file);
            anylogicFileCheckResult.setCorrectFileType(true);
            Enumeration<?> enu = zipFile.entries();
            List<String> fileNameList = new ArrayList<>();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                String name = zipEntry.getName();
                log.info(zipEntry.getName());
                fileNameList.add(zipEntry.getName());
                if (zipEntry.getName().toLowerCase().indexOf(searchFileName) != -1) {
                    unZippedJar = unzipFile(zippedFile, searchFileName);
                    log.info("modelJar PATH file : " + unZippedJar.getAbsolutePath());
                }
            }
            anylogicFileCheckResult.setZipContentFileNames(fileNameList);
        } catch (ZipException ioe) {
            log.error("Error opening zip file" + ioe);
            anylogicFileCheckResult.setCorrectFileType(false);
        } catch (IOException e) {
            log.error("Error opening zip file" + e);
            anylogicFileCheckResult.setCorrectFileType(false);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                    zipFile = null;
                }
            } catch (IOException ioe) {
                log.error("Error opening zip file" + ioe);
            }
            log.info("{} :- CheckZip File Completed", uuid);
        }
        return unZippedJar;
    }


    // To Check if the model.jar is a valid
    private void checkJarFile(File unZippedJar, AnylogicFileCheckResult anylogicFileCheckResult) {

        log.info("{} :- checkJarFile Started", uuid);
        // To Check if the Jar file is a valid
        ZipFile jarFile = null;

        try {
            jarFile = new ZipFile(unZippedJar);
            log.info("jarFile Name : " + jarFile.getName());
            anylogicFileCheckResult.setModelJarFilePresent(true);
        } catch (ZipException ioe) {
            log.error("Error opening jar file" + ioe);
            anylogicFileCheckResult.setModelJarFilePresent(false);
        } catch (IOException e) {
            log.error("Error opening jar file" + e);
            anylogicFileCheckResult.setModelJarFilePresent(false);
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                    jarFile = null;
                }
            } catch (IOException ioe) {
                log.error("Error opening jar file" + ioe);
            }
        }
        log.info("{} :- checkJarFile Completed", uuid);
    }

    // To check the existence of pathmind helpers check
    //Used static value for file path for unjarred model
    private void checkHelpers(File file, AnylogicFileCheckResult anylogicFileCheckResult) {
        File unJarred = null;
        try {
            log.info("{} :- checkHelpers Started", uuid);
            unJarred = extractArchive(file);
            List<String> listOfFiles = FileUtils.listFiles(unJarred.toString());
            ClassPrinter cp = new ClassPrinter();
            List<String> listOfHelpers = cp.byteParser(listOfFiles);
            anylogicFileCheckResult.setDefinedHelpers(listOfHelpers);
            log.info("{} :- checkHelpers Completed", uuid);
        } catch (IOException ioe) {
            log.error("Error unJarred jar file" + ioe);
        }

    }


    private File unzipFile(File zippedFile, String searchFileName) throws IOException {
        log.info("{} :- unzipFile Started", uuid);
        try {
            ZipFile zipFile = new ZipFile(zippedFile);
            Enumeration<?> enu = zipFile.entries();

            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                log.info("name:- {} | size:- {} | compressed size:- {}\n",
                        name, size, compressedSize);

                if (zipEntry.getName().toLowerCase().indexOf(searchFileName) != -1) {
                    tempPath = Files.createTempDirectory(uuid);
                    jarTempDir = new File(String.valueOf(tempPath));
                    if (!jarTempDir.exists()) {
                        jarTempDir.mkdir();
                    }
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    jarTempDir = new File(jarTempDir + "/" + searchFileName);
                    FileOutputStream fos = new FileOutputStream(jarTempDir);
                    log.info("temporary directory path----:" + jarTempDir);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = inputStream.read(bytes)) >= 0) {
                        fos.write(bytes, 0, length);
                    }
                    inputStream.close();
                    fos.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("{} :- unzipFile Completed", uuid);
        return jarTempDir;
    }

    private File extractArchive(File archiveFile) throws IOException {
        File destDir = new File(archiveFile.getParent());
        try {
            JarFile jar = new JarFile(archiveFile);
            Enumeration enumEntries = jar.entries();
            File fileDir = null;
            while (enumEntries.hasMoreElements()) {
                JarEntry file = (JarEntry) enumEntries.nextElement();
                fileDir = new File(destDir + File.separator + file.getName());
                if (!fileDir.exists()) {
                    fileDir.getParentFile().mkdirs();
                    fileDir = new File(destDir, file.getName());
                }
                if (file.isDirectory()) {
                    continue;
                }
                InputStream is = jar.getInputStream(file);
                FileOutputStream fos = new FileOutputStream(fileDir);
                while (is.available() > 0) {
                    fos.write(is.read());
                }
                fos.close();
                is.close();
                fileDir = null;

            }
            jar.close();
        } catch (IOException e) {
            e.printStackTrace();
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

