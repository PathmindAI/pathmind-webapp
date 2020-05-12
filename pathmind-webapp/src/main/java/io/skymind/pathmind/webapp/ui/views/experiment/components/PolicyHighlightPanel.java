package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Span errorDescriptionLabel = LabelFactory.createLabel("", "tag", "error-label");
	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
		setVisible(false);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

		add(errorDescriptionLabel);
	}

	public void setErrorDescription(String errorDescriptionText) {
		setVisible(errorDescriptionText != null);
		errorDescriptionLabel.setText(errorDescriptionText);
	}
}