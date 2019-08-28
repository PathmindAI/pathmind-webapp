package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.stream.Stream;

public class ActionMenu extends HorizontalLayout
{
	public ActionMenu(Button... buttons)
	{
		Stream.of(buttons).forEach(button -> {
			button.setThemeName("primary");
			add(buttons);
		});

	  	setWidthFull();
	  	addClassName("action-menu");
	  	setJustifyContentMode(JustifyContentMode.CENTER);
	}

	public void setButtonVisible(Button button, boolean isVisible) {
		getChildren()
				.filter(b -> b.equals(button))
				.forEach(b -> b.setVisible(isVisible));
	}
}
