package io.skymind.pathmind.ui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.Command;

import elemental.json.Json;

/**
 * Basically <code>vaadin-upload</code> component, but some changes are done to enable selecting folders for uploading:
 * - <code>webkitdirectory</code> and <code>mozdirectory</code> attributes are added to input element
 * - Added <code>addAllFilesUploadedListener</code> method, that is triggered after all files are uploaded
 * - Added MultiFileMemoryBufferWithFileStructure as default receiver, which works with file path, instead of filename
 * - The filter is done in client side, see model-upload-filter.js and black-list.js for details
 * 
 * Additionally,
 * - webkitdirectory attribute might not work in all browsers (https://github.com/SkymindIO/pathmind-webapp/issues/628)
 * - currently custom libraries are identified by making a string comparison with the black-list, this method can be improved 
 * (https://github.com/SkymindIO/pathmind-webapp/issues/629) 
 */

@JavaScript("/src/upload/model-upload-filter.js")
public class PathmindModelUploader extends Upload {
	
	private int numOfFilesUploaded = 0;
	
	private List<Command> allFilesCompletedListeners = new ArrayList<>();
	
	
	public PathmindModelUploader() {
		super();
		setReceiver(new MultiFileMemoryBufferWithFileStructure());
		setupFolderUpload();
		addUploadStartListener(this::uploadStarted);
		addUploadErrorListener(evt -> {
			numOfFilesUploaded--;
			if (numOfFilesUploaded == 0) {
				triggerAllFilesCompletedListeners();
			}
		});
		addUploadSuccessListener(evt -> {
			numOfFilesUploaded--;
			if (numOfFilesUploaded == 0) {
				triggerAllFilesCompletedListeners();
			}
		});
	}
	
	private void uploadStarted(UploadStartEvent<Upload> evt) {
		numOfFilesUploaded++;
		String filePath = evt.getDetailFile().getString("filePath");
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		MultiFileMemoryBufferWithFileStructure.class.cast(getReceiver()).addFilePath(filePath, fileName);
	}

	public void addAllFilesUploadedListener(Command command) {
		allFilesCompletedListeners.add(command);
	}
	
	private void triggerAllFilesCompletedListeners() {
		getElement().setPropertyJson("files", Json.createArray());
		allFilesCompletedListeners.forEach(listener -> listener.execute());
	}

	public void setupFolderUpload() {
		// Currently it's not possible to drop a folder
		setDropAllowed(false);
		getElement().executeJs("$0.$.fileInput.webkitdirectory = true");
		getElement().executeJs("$0.$.fileInput.mozdirectory = true");
		getElement().executeJs("window.Pathmind.ModelUploader.addClientSideFiltering($0)");
	}
	
	class MultiFileMemoryBufferWithFileStructure extends MultiFileMemoryBuffer {
		private Map<String, String> filePathMap = new HashMap<>();
		
		private void addFilePath(String filePath, String fileName) {
			filePathMap.put(filePath, fileName);
		}
		
	    /**
	     * Get the file paths in memory for this buffer.
	     *
	     * @return files in memory
	     */
	    public Set<String> getFiles() {
	        return filePathMap.keySet();
	    }

	    /**
	     * Get the output stream for file.
	     *
	     * @param fileName
	     *            name of file to get stream for
	     * @return file output stream or empty stream if no file found
	     */
	    public ByteArrayOutputStream getOutputBuffer(String filePath) {
	    	if (filePathMap.containsKey(filePath)) {
	    		return (ByteArrayOutputStream) getFileData(filePathMap.get(filePath)).getOutputBuffer();
	    	}
	        return new ByteArrayOutputStream();
	    }

	    /**
	     * Get the input stream for file with filePath.
	     *
	     * @param filename
	     *            name of file to get input stream for
	     * @return input stream for file or empty stream if file not found
	     */
	    public InputStream getInputStream(String filePath) {
	    	if (filePathMap.containsKey(filePath)) {
	            return new ByteArrayInputStream(getOutputBuffer(filePath).toByteArray());
	        }
	        return new ByteArrayInputStream(new byte[0]);
	    }
	}
}
