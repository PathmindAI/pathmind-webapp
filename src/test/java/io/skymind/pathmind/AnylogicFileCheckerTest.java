package io.skymind.pathmind;

import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.AnylogicFileChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class AnylogicFileCheckerTest {
    private static final Logger log = LogManager.getLogger(AnylogicFileChecker.class);
    String uuid = UUID.randomUUID().toString();

    @Test
    private void checkZipFile(File file, AnylogicFileCheckResult anylogicFileCheckResult) {

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
                        //checkJarFile(unZippedJar,anylogicFileCheckResult);
                        isJarExist=true;
                    }
                }
                anylogicFileCheckResult.setZipContentFileNames(fileNameList);
                zipFile.close();
                if(isJarExist) anylogicFileCheckResult.setModelJarFilePresent(true);

                //checkHelpers(unZippedJar,anylogicFileCheckResult);

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
    @Test
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
