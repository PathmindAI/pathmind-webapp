package io.skymind.pathmind.services.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.skymind.pathmind.utils.FileUtils;
public class AnylogicFileChecker implements FileChecker {
    private static final Logger log = LogManager.getLogger(AnylogicFileChecker.class);
    private static final int BUFFER = 1024;
    String uuid = UUID.randomUUID().toString();


    @Override
    public FileCheckResult performFileCheck(File file) {

        AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();

        //To check the file exist and does the server have permission to read
        if (file.exists() && file.isFile() && file.canRead() && file.canExecute()) {
            log.info("File exists and it is readable:");
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

        File unZippedJar =null;
        boolean isJarExist = false;
        try {
            zipfile = new ZipFile(file);
            anylogicFileCheckResult.setCorrectFileType(true);
           try {
                ZipFile zipFile = new ZipFile(zippedFile);
                Enumeration<?> enu = zipFile.entries();
                List<String> fileNameList= new ArrayList<>();
                while (enu.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                    String name = zipEntry.getName();
                    log.info(zipEntry.getName());
                    fileNameList.add(zipEntry.getName());

                    if(zipEntry.getName().toLowerCase().indexOf(searchFileName) != -1){
                        unZippedJar= unzipFile(zippedFile,searchFileName);
                        log.error("modelJar PATH file : " + unZippedJar.getAbsolutePath());
                        checkJarFile(unZippedJar,anylogicFileCheckResult);
                        isJarExist=true;
                    }
                }
                anylogicFileCheckResult.setZipContentFileNames(fileNameList);
                zipFile.close();
                if(isJarExist) anylogicFileCheckResult.setModelJarFilePresent(true);

                checkHelpers(unZippedJar,anylogicFileCheckResult);

            } catch (IOException e) {
                e.printStackTrace();
            }


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
    private void checkJarFile(File unZippedJar, AnylogicFileCheckResult anylogicFileCheckResult) {
        // To Check if the Jar file is a valid
        ZipFile jarFile = null;

        try {
            jarFile = new ZipFile(unZippedJar);
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
    //Used static value for file path for unjarred model
    private void checkHelpers(File file, AnylogicFileCheckResult anylogicFileCheckResult) {
        List<String> listOfFiles = FileUtils.listFiles("/tmp/af2edc57-7255-4cd0-85bc-ec1cd59eee6712494912324244030621/model");
        ClassPrinter cp = new ClassPrinter();
        List<String> listOfHelpers = cp.byteParser(listOfFiles);
        System.out.println(listOfHelpers);
        anylogicFileCheckResult.setDefinedHelpers(listOfHelpers);

    }


    private File unzipFile(File zippedFile,String searchFileName) throws IOException {
        File jarTempDir = null;
          try {
            ZipFile zipFile = new ZipFile(zippedFile);
            Enumeration<?> enu = zipFile.entries();

              while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                log.info("name: %-20s | size: %6d | compressed size: %6d\n",
                        name, size, compressedSize);

                if(zipEntry.getName().toLowerCase().indexOf(searchFileName) != -1){

                   jarTempDir = new File( Files.createTempDirectory(uuid).toString() );
                    if (!jarTempDir.exists()) {
                        jarTempDir.mkdir();
                    }
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    jarTempDir = new File(jarTempDir + "/" + searchFileName);
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
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarTempDir;
    }
}

