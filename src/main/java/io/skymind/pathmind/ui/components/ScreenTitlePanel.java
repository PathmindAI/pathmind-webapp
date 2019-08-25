package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ScreenTitlePanel extends HorizontalLayout
{
	private Label subtitleLabel = new Label();

	public ScreenTitlePanel(String title)
	{
		this(title, "");
	}

	public ScreenTitlePanel(String title, String subtitle)
	{
		Label titleLabel = new Label(title);
		titleLabel.addClassName("section-label-title");
		add(titleLabel);

		subtitleLabel.setText(subtitle);
		subtitleLabel.addClassName("section-label-subtitle");
		add(subtitleLabel);
	}

	public void setSubtitle(String subtitle) {
		subtitleLabel.setText(subtitle);
	}
}
