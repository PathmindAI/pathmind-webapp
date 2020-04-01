package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;

public class PathmindGreetingComponent extends VerticalLayout {
	
	public PathmindGreetingComponent() {
		this("Welcome to");
	}
	public PathmindGreetingComponent(String greetingText) {
		setSpacing(false);
		Image pathMindLogo = new Image("frontend/images/pathmind-logo.png", "Pathmind Logo");
		pathMindLogo.addClassName("navbar-logo");
		add(LabelFactory.createLabel(greetingText, CssMindPathStyles.SMALL_LIGHT_LABEL),
				pathMindLogo);
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}
}
