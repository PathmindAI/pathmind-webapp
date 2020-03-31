package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.webapp.ui.views.model.UploadMode;

public class UploadModeSwitcherButton extends Button {

	public UploadModeSwitcherButton(UploadMode mode, Command action) {
 		setText(mode == UploadMode.FOLDER ? "Upload as Zip" : "Upload Folder");
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		addClickListener(evt -> action.execute());
	}
	
}
