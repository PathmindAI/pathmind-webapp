package io.skymind.pathmind.services.model.analyze;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModelFileVerifier {

    public static final String MODEL = "model.jar";
    public static final List<String> ALLOW_LIST = List.of("database/db.script", "database/db.properties", "database/db.data", "cache/giscache", "cache/giscache.p", "cache/giscache.t");
    public static final Predicate<String> XLS_MATCH = Pattern.compile("^.*\\.xls[x]?", Pattern.CASE_INSENSITIVE).asMatchPredicate();
    public static final Predicate<String> TXT_MATCH = Pattern.compile("^.*\\.txt?", Pattern.CASE_INSENSITIVE).asMatchPredicate();
    public static final Predicate<String> XSD_MATCH = Pattern.compile("^.*\\.xsd?", Pattern.CASE_INSENSITIVE).asMatchPredicate();
    public static final Predicate<String> XML_MATCH = Pattern.compile("^.*\\.xml?", Pattern.CASE_INSENSITIVE).asMatchPredicate();

    public static List<Predicate<String>> matchList;

    static {

        matchList = List.of(XLS_MATCH, TXT_MATCH, XSD_MATCH, XML_MATCH);
    }


    public ModelBytes assureModelBytes(ModelBytes modelBytes) {
        try {
            byte[] modifiedBytes = ensureZipFileStructure(modelBytes.getBytes());
            return ModelBytes.of(modifiedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
            // TODO:  return ModelBytes.error(e.getMessage());
        }
    }

    /**
     * The zip file should only contain model.jar and database folder,
     * If database folder exists, it should contain db.properties and db.script
     * otherwise, it shouldn't contain a database folder
     * Also, if the required files are under a specific folder, these files are moved to root folder
     */
    protected static byte[] ensureZipFileStructure(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        String rootFolder = "";

        File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());
        try {
            FileUtils.writeByteArrayToFile(tempFile, data);
            try (ZipFile zipFile = new ZipFile(tempFile)) {
                Enumeration<? extends ZipEntry> enu = zipFile.entries();
                while (enu.hasMoreElements()) {
                    ZipEntry zipEntry = enu.nextElement();
                    Path objPath = Paths.get(zipEntry.getName());
                    Path filename = objPath.getFileName();
                    if (filename.toString().equalsIgnoreCase(MODEL) && objPath.getParent() != null) {
                        rootFolder = objPath.getParent().toString() + objPath.getFileSystem().getSeparator();
                    }
                }
                enu = zipFile.entries();
                while (enu.hasMoreElements()) {
                    ZipEntry zipEntry = enu.nextElement();
                    if (zipEntry.getName().length() > rootFolder.length()) {
                        String filename = zipEntry.getName().substring(rootFolder.length());
                        if (isAllowed(filename)) {
                            ZipEntry entry = new ZipEntry(filename);
                            zos.putNextEntry(entry);
                            byte[] entryBytes = zipFile.getInputStream(zipEntry).readAllBytes();
                            zos.write(entryBytes);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        } finally {
            FileUtils.deleteQuietly(tempFile);
        }
        zos.close();
        return baos.toByteArray();
    }

    private static boolean isAllowed(String filename) {
        return ALLOW_LIST.contains(filename) || ModelUtils.isModelFile(filename) || checkMatchList(filename);
    }

    private static boolean checkMatchList(String filename) {
        return matchList.stream().filter(m -> m.test(filename)).findFirst().isPresent();
    }


}
