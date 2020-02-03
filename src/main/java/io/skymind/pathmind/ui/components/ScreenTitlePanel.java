package io.skymind.pathmind.ui.components;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Component
public class ScreenTitlePanel extends HorizontalLayout
{
	private Span subtitleLabel = new Span();
	private Span titleLabel = new Span();

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
		addClassName("action-menu");

		titleLabel.setText(title);
		titleLabel.addClassName("section-label-title");
		add(titleLabel);

		subtitleLabel.setText(subtitle);
		subtitleLabel.addClassName("section-label-subtitle");
		add(subtitleLabel);
	}

	public void setSubtitle(String subtitle) {
		subtitleLabel.setText(subtitle);
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
