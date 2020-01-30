package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.ui.views.guide.GuideOverview;

public class UploadModelButton extends Button {

	public UploadModelButton(long projectId) {
		super("Upload Model", new Icon(VaadinIcon.ARROW_UP),
				click -> UI.getCurrent().navigate(GuideOverview.class)); // may need to pass the projectId
		addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}
}
