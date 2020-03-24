package io.skymind.pathmind.webapp.ui.components;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

@Component
public class ScreenTitlePanel extends HorizontalLayout
{

	public ScreenTitlePanel() {
		this("");
	}

	public ScreenTitlePanel(String title) {
		this(title, "", null);
	}

	public ScreenTitlePanel(String title, String subtitle, Class rootNavigationTarget)
	{
		setWidthFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		addClassName("page-title");

		if (subtitle == "") {
			add(LabelFactory.createLabel(title, "section-label-title"));
		} else {
			add(new RouterLink(title, rootNavigationTarget));
			add(new Span(">"));
			add(LabelFactory.createLabel(subtitle, "section-label-subtitle"));
		}

	}
}
