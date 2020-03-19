package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.server.Command;

public class UploadModeSwitcherButton extends Button {

	public UploadModeSwitcherButton(boolean isFolderUploadMode, Command action) {
		setText(isFolderUploadMode ? "Upload as Zip" : "Upload Folder");
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		addClickListener(evt -> action.execute());
	}
	
}
