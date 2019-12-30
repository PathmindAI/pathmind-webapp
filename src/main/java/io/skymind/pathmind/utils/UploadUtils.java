package io.skymind.pathmind.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

public class UploadUtils {

	public static byte[] createZipFileFromBuffer(MultiFileMemoryBuffer buffer) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		List<String> folders = new ArrayList<>();
		for (String filePath : buffer.getFiles()) {
			String folder = parseFolderName(filePath);
			if (folder != null && !folders.contains(folder)) {
				zos.putNextEntry(new ZipEntry(folder));
				folders.add(folder);
			}
			ZipEntry entry = new ZipEntry(filePath);
			zos.putNextEntry(entry);
			zos.write(buffer.getInputStream(filePath).readAllBytes());
			zos.closeEntry();
		}
		zos.close();
		return baos.toByteArray();
	}
	
	private static String parseFolderName(String filePath) {
		if (filePath.indexOf("/") > 0) {
			return filePath.substring(0, filePath.indexOf("/"));
		} else {
			return null;
		}
	}
}
