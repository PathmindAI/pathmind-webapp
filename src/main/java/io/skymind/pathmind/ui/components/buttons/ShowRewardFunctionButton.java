package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ShowRewardFunctionButton extends Button {

	public ShowRewardFunctionButton() {
		super(VaadinIcon.CODE.create());
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		addClassName("action-button");
	}
}
