package io.skymind.pathmind.shared.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ZipUtils {

    @FunctionalInterface
    public interface ZipStreamEntryConsumeFunction<T> {
        T apply(ZipInputStream zipInputStream, ZipEntry zipEntry) throws IOException;
    }

    public static <T> T processZipEntryInFile(byte[] zipFile, Predicate<String> entryNameCriteria, ZipStreamEntryConsumeFunction<T> consumeEntry) {
        T result = null;
        try (ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipFile))) {
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry()) != null && Objects.isNull(result)) {
                if (entryNameCriteria.test(entry.getName())) {
                    result = consumeEntry.apply(zipStream, entry);
                }
                zipStream.closeEntry();
            }
        } catch (IOException e) {
            log.error("Not able to process entry", e);
        }
        return result;
    }

    public static ZipStreamEntryConsumeFunction<byte[]> entryContentExtractor() {
        return (zipInputStream, zipEntry) -> {
            byte[] modelJar = null;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int len = 0;
                byte[] buffer = new byte[2048];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                modelJar = out.toByteArray();
            }
            return modelJar;
        };
    }

}
