package io.skymind.pathmind.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

public class UploadUtils {

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
