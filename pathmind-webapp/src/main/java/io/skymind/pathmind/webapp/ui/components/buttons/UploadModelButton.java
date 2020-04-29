package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;

public class UploadModelButton extends Button {

	public UploadModelButton(long projectId) {
		super("Upload Model", new Icon(VaadinIcon.ARROW_UP));
		addClickListener(evt -> getUI().ifPresent(ui -> ui.navigate(UploadModelView.class, ""+projectId)));
		addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}
}
