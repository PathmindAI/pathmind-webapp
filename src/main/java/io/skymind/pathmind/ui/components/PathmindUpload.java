package io.skymind.pathmind.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.Command;

import elemental.json.Json;

@JavaScript("/src/upload/upload-connector.js")
public class PathmindUpload extends Upload {
	
	private int numOfFilesUploaded = 0;
	
	private List<Command> allFilesCompletedListeners = new ArrayList<>();
	
	public PathmindUpload(Receiver receiver) {
		super(receiver);
		addUploadStartListener(evt -> numOfFilesUploaded++);
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
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
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
}
