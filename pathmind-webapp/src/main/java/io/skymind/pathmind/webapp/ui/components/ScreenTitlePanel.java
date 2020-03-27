package io.skymind.pathmind.webapp.ui.components;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;

@Component
public class ScreenTitlePanel extends HorizontalLayout
{

	public ScreenTitlePanel() {
		this("");
	}

	public ScreenTitlePanel(String title) {
		this(title, null, null);
	}

	public ScreenTitlePanel(String title, String subtitle, Class rootNavigationTarget){
		this(new Breadcrumbs(title, subtitle, rootNavigationTarget));
	}
	
	public ScreenTitlePanel(Breadcrumbs breadcrumbs) {
		setWidthFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		addClassName("page-title");
		add(breadcrumbs);
	}
}
