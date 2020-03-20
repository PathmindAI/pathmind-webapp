package io.skymind.pathmind.webapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;


public class UploadUtils {
	
	private static String MODEL = "model.jar";
	private static String[] WHITE_LIST = {"model.jar", "database/db.script", "database/db.properties", "database/db.data"};
	
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
	
	/**
	 * The zip file should only contain model.jar and database folder,
	 * If database folder exists, it should contain db.properties and db.script
	 * otherwise, it shouldn't contain a database folder
	 * Also, if the required files are under a specific folder, these files are moved to root folder
	 */
	public static byte[] ensureZipFileStructure(byte[] data) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		
		String rootFolder = "";
		
		File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());
        try {
            FileUtils.writeByteArrayToFile(tempFile, data);
            try (ZipFile zipFile = new ZipFile(tempFile)) {
            	Enumeration<?> enu = zipFile.entries();
            	while (enu.hasMoreElements()) {
            		ZipEntry zipEntry = (ZipEntry) enu.nextElement();
            		Path objPath = Paths.get(zipEntry.getName());
                    Path filename = objPath.getFileName();
            		if (filename.toString().equalsIgnoreCase(MODEL) && objPath.getParent() != null) {
            			rootFolder = objPath.getParent().toString() + objPath.getFileSystem().getSeparator();
            		}
            	}
            	enu = zipFile.entries();
            	while (enu.hasMoreElements()) {
            		ZipEntry zipEntry = (ZipEntry) enu.nextElement();
            		for (String dbfile: Arrays.asList(WHITE_LIST)) {
            			if (zipEntry.getName().length() > rootFolder.length()) {
            				String filename = zipEntry.getName().substring(rootFolder.length());
            				if (filename.equalsIgnoreCase(dbfile)) {
            					ZipEntry entry = new ZipEntry(dbfile);
            					zos.putNextEntry(entry);
            					zos.write(readZipEntryContent(zipFile, zipEntry));
            				}
            			}
					}
            	}
            }catch (Exception e) {
            	e.printStackTrace();
			}
        } finally {
            tempFile.delete();
        }
        zos.close();
		return baos.toByteArray();
	}

	private static byte[] readZipEntryContent(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
		return zipFile.getInputStream(zipEntry).readAllBytes();
	}
	
	
}
