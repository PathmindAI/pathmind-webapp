package io.skymind.pathmind.services.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class AnylogicFileChecker implements FileChecker {
    private static final Logger log = LogManager.getLogger(AnylogicFileChecker.class);
    private static final int BUFFER = 1024;


    @Override
    public FileCheckResult performFileCheck(File file) {

        AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();

        //To check the file exist and does the server have permission to read
        if (file.exists() && file.isFile() && file.canRead() && file.canExecute()) {
            log.info("File exists and it is readable");
        } else {
            log.info("File does not exist or no read permission");
        }
        checkZipFile(file, anylogicFileCheckResult);

        return anylogicFileCheckResult;
    }


    private void checkZipFile(File file, AnylogicFileCheckResult anylogicFileCheckResult) {

        // To Check if the Zip file is a valid
        ZipFile zipfile = null;
        String searchFileName = "model.jar";
        File zippedFile = file;
        File outputFolder = new File("C:/fileunzip/");
        File unZippedFolderDetails;
        boolean isJarExist = false;
        try {
            zipfile = new ZipFile(file);
            anylogicFileCheckResult.setCorrectFileType(true);
            unZippedFolderDetails = unzipFile(zippedFile, outputFolder);
            log.info("Unzipped path file contents :" + unZippedFolderDetails.getAbsolutePath());
            checkJarFile(unZippedFolderDetails, anylogicFileCheckResult);
            //anylogicFileCheckResult.setZipContentFileNames(fileNameList);

        } catch (ZipException ioe) {
            log.error("Error opening zip file" + ioe);
            anylogicFileCheckResult.setCorrectFileType(false);
        } catch (IOException e) {
            log.error("Error opening zip file" + e);
            anylogicFileCheckResult.setCorrectFileType(false);
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException ioe) {
                log.error("Error opening zip file" + ioe);
            }
        }

    }


    // To Check if the model.jar is a valid
    private void checkJarFile(File modelJar, AnylogicFileCheckResult anylogicFileCheckResult) {
        // To Check if the Jar file is a valid
        ZipFile jarFile = null;

        try {
            jarFile = new ZipFile(modelJar);
            log.error("jarFile Name : " + jarFile.getName());
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
    }

    // To check the existence of pathmind helpers check
    private void checkHelpers(File file, AnylogicFileCheckResult anylogicFileCheckResult) {

    }


    private File unzipFile(File zippedFile, File outputFolder) throws IOException {
          try {
            ZipFile zipFile = new ZipFile(zippedFile);
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n",
                        name, size, compressedSize);

                File file = new File(name);
                if (name.endsWith("/")) {
                    file.mkdirs();
                    continue;
                }

                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }

                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
                is.close();
                fos.close();

            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zippedFile;
    }
}

