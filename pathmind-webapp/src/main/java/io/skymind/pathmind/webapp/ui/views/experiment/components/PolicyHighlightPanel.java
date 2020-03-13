package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindLabelBox;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Span errorDescriptionLabel = LabelFactory.createLabel("", "tag", "error-label");
	private PathmindLabelBox scoreLabel;
	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
		errorDescriptionLabel.setVisible(false);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		
		scoreLabel = new PathmindLabelBox("Score");
		scoreLabel.addClassName("score");

		add(errorDescriptionLabel, scoreLabel);
	}

	public void update(Policy policy) {
		scoreLabel.setValue(PolicyUtils.getFormattedLastScore(policy));
	}

	public void setErrorDescription(String errorDescriptionText) {
		errorDescriptionLabel.setVisible(errorDescriptionText != null);
		errorDescriptionLabel.setText(errorDescriptionText);
	}
}