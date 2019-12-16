package io.skymind.pathmind.ui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.Command;

import elemental.json.Json;

@JavaScript("/src/upload/upload-connector.js")
public class PathmindUpload extends Upload {
	
	private int numOfFilesUploaded = 0;
	
	private List<Command> allFilesCompletedListeners = new ArrayList<>();
	private Map<String, String> filePathMap = new HashMap<>();
	
	public PathmindUpload(Receiver receiver) {
		super(receiver);
		addUploadStartListener(evt -> uploadStarted(evt));
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
		filePathMap.put(fileName, filePath);
	}

	public void addAllFilesUploadedListener(Command command) {
		allFilesCompletedListeners.add(command);
	}
	
	private void triggerAllFilesCompletedListeners() {
		getElement().setPropertyJson("files", Json.createArray());
		allFilesCompletedListeners.forEach(listener -> listener.execute());
	}

	public void setFolderUpload(boolean folderUpload) {
		getElement().executeJs("$0.$.fileInput.webkitdirectory = true");
		getElement().executeJs("$0.$.fileInput.mozdirectory = true");
		getElement().executeJs("window.addClientSideFiltering($0)");
	}
	
	public Map<String, String> getFilePathMap() {
		return filePathMap;
	}
}
