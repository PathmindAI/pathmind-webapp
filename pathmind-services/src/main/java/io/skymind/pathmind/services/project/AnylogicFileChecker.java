package io.skymind.pathmind.services.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.project.meta.PathmindMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/*To validate the model.jar uploaded by the user*/
@Slf4j
public class AnylogicFileChecker implements FileChecker {

    private String uuid = UUID.randomUUID().toString();
    private File tempDir = null;

    private byte[] buffer = new byte[1024];

    private ObjectMapper objectMapper;

    public AnylogicFileChecker(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        try {
            tempDir = Files.createTempDirectory("pathmind" + uuid).toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileCheckResult performFileCheck(StatusUpdater statusUpdater, File file) {
        log.info("{} :- performFileCheck Started", uuid);

        AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();
        try {
            //To check the file(model.zip) exist and does the server have permission to read
            if (file.exists() && file.isFile() && file.canRead()) {
                log.info("Uploaded file exists and it is readable");
                //To check a Zip file and if it is a valid file extract it in to the temporary folder
                File unZipped = checkZipFile(file, anylogicFileCheckResult);
                statusUpdater.updateStatus(0.10);

                if (anylogicFileCheckResult.isCorrectFileType() && unZipped != null) {
                    //Passing unzipped path to check whether model.jar exist and it is valid or not
                    File modelJarFile = checkJarFile(unZipped, anylogicFileCheckResult);
                    checkMeta(unZipped, anylogicFileCheckResult);
                    statusUpdater.updateStatus(0.30);

                    if (anylogicFileCheckResult.isModelJarFilePresent() && modelJarFile != null) {
                        //Check for PathmindHelper class instance in uploaded model.jar
                        checkHelpers(modelJarFile, anylogicFileCheckResult);
                        statusUpdater.updateStatus(0.50);

                        if (anylogicFileCheckResult.isHelperPresent()) {
                            statusUpdater.updateStatus(0.90);
                        } else {
                            log.error("model.jar does not having PathmindHelper class");
                            statusUpdater.updateError("model.jar does not having PathmindHelper class");
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
            if (tempDir != null) deleteTempDirectory();
        }
        log.info("{} :- performFileCheck Completed", uuid);
        return anylogicFileCheckResult;
    }

    /* To check whether zip file is valid or not, if valid it returns unzipped temp directory */
    File checkZipFile(File file, AnylogicFileCheckResult anylogicFileCheckResult) throws IOException {
        log.info("{} :- checkZipFile File Started", uuid);

        File unzippedFile = null;

        try (InputStream is = new FileInputStream(file); ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            boolean isValidZip = FileUtils.detectDocType(is);

            if (isValidZip) {
                anylogicFileCheckResult.setCorrectFileType(isValidZip);
                unzippedFile = tempDir;
                List<String> fileNameList = new ArrayList<>();

                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    log.info("Content of Zip file : {} ", zipEntry.getName());
                    fileNameList.add(zipEntry.getName());

                    File newFile = new File(unzippedFile, zipEntry.getName());
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        log.debug("extracted {} :-", newFile.getAbsolutePath());
                        zipEntry = zis.getNextEntry();
                    }
                }

                anylogicFileCheckResult.setZipContentFileNames(fileNameList);
            } else {
                log.error("Invalid input file format :");
            }
        }

        log.info("{} :- checkZipFile File Completed", uuid);
        return unzippedFile;
    }

    // To check if pathmind_meta.json exist
    void checkMeta(File unZipped, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkMeta Started", uuid);

        Optional<File> metaJson = Arrays.stream(unZipped.listFiles())
                .filter(file -> file.getName().equalsIgnoreCase("pathmind_meta.json"))
                .findAny();

        if (metaJson.isPresent()) {
            try (FileInputStream fis = new FileInputStream(metaJson.get())) {
                PathmindMeta pathmindMeta = objectMapper.readValue(fis, PathmindMeta.class);
                anylogicFileCheckResult.setPathmindMeta(pathmindMeta);
            } catch (Exception e) {
                log.error("Error deserialize meta file", e);
                anylogicFileCheckResult.setPathmindMeta(null);
            }
        }

        log.info("{} :- checkMeta Completed", uuid);
    }

    // To Check if the model.jar exist and is a valid
    // if valid it returns model.jar file
    File checkJarFile(File unZipped, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkJarFile Started", uuid);

        File modelJarFile = null;

        System.out.println("kepricondebug : " + unZipped);

        Optional<File> modelJar = Arrays.stream(unZipped.listFiles())
                .filter(file -> file.getName().equalsIgnoreCase("model.jar"))
                .findAny();

        if (modelJar.isPresent()) {
            modelJarFile = modelJar.get();
            try (ZipFile jarFile = new ZipFile(modelJarFile)) {
                anylogicFileCheckResult.setModelJarFilePresent(true);
            } catch (Exception e) {
                log.error("Error opening jar file", e);
                anylogicFileCheckResult.setModelJarFilePresent(false);
            }
        }
        log.info("{} :- checkJarFile Completed", uuid);
        return modelJarFile;
    }

    // To check the existence of pathmind helpers check
    void checkHelpers(File jarFile, AnylogicFileCheckResult anylogicFileCheckResult) {
        log.info("{} :- checkHelpers Started", uuid);
        try {
            File unJarred = extractArchive(jarFile);
            List<String> listOfFiles = FileUtils.listFiles(unJarred.toString());

            ByteCodeAnalyzer byteCodeAnalyzer = new ByteCodeAnalyzer();

            List<String> listOfHelpers = byteCodeAnalyzer.byteParser(listOfFiles);

            anylogicFileCheckResult.setDefinedHelpers(listOfHelpers);
        } catch (IOException ioe) {
            log.error("Error unJarred jar file", ioe);
        }
        log.info("{} :- checkHelpers Completed", uuid);
    }


    /*To extract the archive file (model.jar) contents inside unzipped temp directory*/
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
        //Delete files recursively
        boolean result = FileSystemUtils.deleteRecursively(tempDir);
        if (!result) {
            log.error("error in folder delete");
        }
    }
}