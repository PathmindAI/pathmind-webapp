package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.stereotype.Component;

@Component
public class ScreenTitlePanel extends HorizontalLayout
{
	private Label subtitleLabel = new Label();
	private Label titleLabel = new Label();

	public ScreenTitlePanel() {
	}

	public ScreenTitlePanel(String title)
	{
		this(title, "");
	}

	public ScreenTitlePanel(String title, String subtitle)
	{
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
