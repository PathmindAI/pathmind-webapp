package io.skymind.pathmind.ui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.server.Command;

import elemental.json.Json;

/**
 * Basically <code>vaadin-upload</code> component, but some changes are done to enable selecting folders for uploading:
 * - <code>webkitdirectory</code> and <code>mozdirectory</code> attributes are added to input element
 * - Added <code>addAllFilesUploadedListener</code> method, that is triggered after all files are uploaded
 * - Added MultiFileMemoryBufferWithFileStructure as default receiver, which works with file path, instead of filename
 * - The filter is done in client side, see model-upload-filter.js for details
 * 
 * PathmindModelUploader works in two modes: Folder upload and zip file upload,
 * In constructor, default mode is set, then a client-side check is performed for folder upload support by browser, 
 * and finally <code>isFolderUploadMode</code> is set true if it's constructed in folder upload mode and browser supports folder upload.
 *  
 */

@JavaScript("/src/upload/model-upload-filter.js")
public class PathmindModelUploader extends Upload {
	
	private int numOfFilesUploaded = 0;
	
	private List<Command> allFilesCompletedListeners = new ArrayList<>();
	
	private Boolean isFolderUploadSupported;
	private Boolean isFolderUploadMode;
	
	public PathmindModelUploader(boolean isFolderUploadMode) {
		super();
		checkIfFolderUploadSupported(isFolderUploadSupported -> {
			this.isFolderUploadMode = isFolderUploadMode && isFolderUploadSupported;
			if (this.isFolderUploadMode) {
				setReceiver(new MultiFileMemoryBufferWithFileStructure());
				setupFolderUpload();
				addNoFilesToUploadListener(evt -> triggerAllFilesCompletedListeners());
			} else {
				setReceiver(new MemoryBuffer());
			}
			setUploadButton(createUploadButton());
		});
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
	private Button createUploadButton() {
		Button uploadButton = new Button(VaadinIcon.UPLOAD.create());
		uploadButton.setText(this.isFolderUploadMode ? "Select folder to upload..." : "Select zip file...");
		return uploadButton;
	}
	/**
	 * Checks if folder upload supported by browser,
	 * If the check has already been done, returns cached value
	 * otherwise checks asynchronously
	 */
	public void isFolderUploadSupported(SerializableConsumer<Boolean> consumer) {
		if (isFolderUploadSupported != null) {
			consumer.accept(isFolderUploadSupported);
		} else {
			checkIfFolderUploadSupported(supported -> {
				isFolderUploadSupported = supported;
				consumer.accept(supported);
			});
		}
	}
	private void checkIfFolderUploadSupported(SerializableConsumer<Boolean> consumer) {
		getElement().executeJs("return window.Pathmind.ModelUploader.isInputDirSupported()").then(Boolean.class, supported -> {
			consumer.accept(supported);
		});
	}
	
	/**
	 * If all files are filtered out, still trigger zip file creation,
	 * so an invalid zip file will be created, and user will be notified
	 */
	private void addNoFilesToUploadListener(ComponentEventListener<NoFileToUploadEvent> listener) {
		addListener(NoFileToUploadEvent.class, listener);
	}

	private void uploadStarted(UploadStartEvent<Upload> evt) {
		numOfFilesUploaded++;
		if (isFolderUploadMode) {
			String filePath = evt.getDetailFile().getString("filePath");
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			MultiFileMemoryBufferWithFileStructure.class.cast(getReceiver()).addFilePath(filePath, fileName);
		}
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

	/**
	 * In upload component, for each file, upload-start, upload-succeeded or upload-failed events are dispatched,
	 * but in ModelUploader, since filtering is applied in client-side, there may be cases where server-side is not notified at all because all files are filtered out.
	 *  <code>no-file-to-upload</code> event is dispatched in this cases, to notify server-side that user triggered an upload operation, but there is no file to upload.
	 */
	@DomEvent("no-file-to-upload")
    public static class NoFileToUploadEvent extends ComponentEvent<PathmindModelUploader> {

        public NoFileToUploadEvent(PathmindModelUploader source, boolean fromClient) {
            super(source, fromClient);
        }
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
