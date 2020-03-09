package io.skymind.pathmind.webapp.ui.components;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Component
public class ScreenTitlePanel extends HorizontalLayout
{
	private Span subtitleLabel;
	private Span titleLabel;

	public ScreenTitlePanel() {
		this("");
	}

	public ScreenTitlePanel(String title)
	{
		this(title, "");
	}

	public ScreenTitlePanel(String title, String subtitle)
	{
		setWidthFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		addClassName("page-title");

		titleLabel = LabelFactory.createLabel(title, "section-label-title");
		add(titleLabel);

		subtitleLabel = LabelFactory.createLabel(subtitle, "section-label-subtitle");
		add(subtitleLabel);
	}

	public void setSubtitle(String subtitle) {
		subtitleLabel.setText(subtitle);
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
