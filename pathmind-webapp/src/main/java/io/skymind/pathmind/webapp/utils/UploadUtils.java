package io.skymind.pathmind.webapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import io.skymind.pathmind.shared.utils.ModelUtils;
import org.apache.commons.io.FileUtils;

public class UploadUtils {

    private static String MODEL = "model.jar";
    private static String[] ALLOW_LIST = {"database/db.script", "database/db.properties", "database/db.data", "cache/giscache", "cache/giscache.p", "cache/giscache.t"};
    private static final Predicate<String> XLS_MATCH = Pattern.compile("^.*\\.xls[x]?", Pattern.CASE_INSENSITIVE).asMatchPredicate();

    public static byte[] createZipFileFromBuffer(MultiFileMemoryBuffer buffer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        for (String filePath : buffer.getFiles()) {
            ZipEntry entry = new ZipEntry(filePath);
            zos.putNextEntry(entry);
            zos.write(buffer.getInputStream(filePath).readAllBytes());
            zos.closeEntry();
        }
        zos.close();
        return baos.toByteArray();
    }

}
