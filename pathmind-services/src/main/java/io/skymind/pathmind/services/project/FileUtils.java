package io.skymind.pathmind.services.project;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {

    /*To list all the .class file from given file path*/
    public static List<String> listFiles(String filePath) {
        Path path = Paths.get(filePath);
        boolean isDir = Files.isDirectory(path);
        List<String> result = new ArrayList<>();

        if (isDir) {
            try (Stream<Path> walk = Files.walk(Paths.get(filePath))) {
                result = walk.map(x -> x.toString())
                        .filter(f -> f.endsWith(".class")).collect(Collectors.toList());
            } catch (IOException e) {
                log.error("error while filter class files", e);
            }
        } else {
            log.error("Invalid input file path");
        }
        return result;
    }

    /*To detect file type and check whether the file type is zip or not, then return either true or false*/
    public static boolean detectDocType(InputStream stream) throws IOException {
        Tika tika = new Tika();
        return tika.detect(stream).equals("application/zip");

    }

    /**
     * To convert a file path with / to File.separator based on the OS.
     */
    public static String getSystemIndependentFilePath(String filepath) {
        return filepath.replaceAll("/", Matcher.quoteReplacement(File.separator));
    }
}
