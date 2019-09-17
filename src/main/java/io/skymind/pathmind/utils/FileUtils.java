package io.skymind.pathmind.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static List<String> listFiles(String filePath){
        List<String> result = new ArrayList<String>();
        try (Stream<Path> walk = Files.walk(Paths.get(filePath))) {

            result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".class")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static void loadJar () {
//        try {
//            JarFile jarFile = new JarFile("/home/local/JMANDIGITAL/radhakrishnan/Downloads/CoffeeShopAnylogic Exported/model.jar");
//            Enumeration enumeration = jarFile.entries();
//            System.out.println(jarFile);
//            System.out.println(jarFile.entries());
//            System.out.println(enumeration);
//            while (enumeration.hasMoreElements()) {
//                process(enumeration.nextElement());
//            }
//        } catch (IOException e) {
//                e.printStackTrace();
//        }
//    }
//
//    private static void process(Object obj) {
//        JarEntry entry = (JarEntry)obj;
//        String name = entry.getName();
//        long size = entry.getSize();
//        long compressedSize = entry.getCompressedSize();
//        //System.out.println(                name + "\t" + size + "\t" + compressedSize);
//    }
}
