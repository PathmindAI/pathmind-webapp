package io.skymind.pathmind.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    private static final Logger log = LogManager.getLogger(FileUtils.class);
    public static List<String> listFiles(String filePath) {
        Path path = Paths.get(filePath);
        boolean isDir = Files.isDirectory(path);
        List<String> result = new ArrayList<String>();
        if(isDir){
            try (Stream<Path> walk = Files.walk(Paths.get(filePath))) {
                result = walk.map(x -> x.toString())
                        .filter(f -> f.endsWith(".class")).collect(Collectors.toList());
                walk.close();
            } catch (IOException e) {
                log.error("error while filter class files", e);
            }
        } else {
            log.error("Invalid input file path");
        }
        return result;
    }

    public static boolean detectDocType(InputStream stream)
            throws IOException {
        boolean isZip = false;
        Tika tika = new Tika();
        String mediaType = tika.detect(stream);
        if(mediaType =="application/zip")
            isZip=true;
        return isZip;
    }
}
