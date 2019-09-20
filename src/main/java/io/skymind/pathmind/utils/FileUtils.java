package io.skymind.pathmind.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    private static final Logger log = LogManager.getLogger(FileUtils.class);
    public static List<String> listFiles(String filePath){
        List<String> result = new ArrayList<String>();
        try (Stream<Path> walk = Files.walk(Paths.get(filePath))) {
            result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".class")).collect(Collectors.toList());
            walk.close();
        } catch (IOException e) {
            log.error("error while filter class files", e);
        }
        return result;
    }
}
