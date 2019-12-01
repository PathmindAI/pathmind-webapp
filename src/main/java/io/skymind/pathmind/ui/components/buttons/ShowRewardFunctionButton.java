package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.ui.components.PathmindButton;

public class ShowRewardFunctionButton extends PathmindButton {

	public ShowRewardFunctionButton() {
		super(VaadinIcon.CODE.create());
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		addClassName("action-button");
		setTitle("Show reward function");
	}
}
