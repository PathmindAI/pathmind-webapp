package io.skymind.pathmind.webapp.ui.views.experiment.components;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.BOLD_LABEL;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Span errorDescriptionLabel = LabelFactory.createLabel("", "tag", "error-label");
	private Span scoreLabel = new Span();
	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
		errorDescriptionLabel.setVisible(false);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		
		Span scoreTitleLabel = LabelFactory.createLabel("Reward Score", BOLD_LABEL);
		scoreLabel.addClassName("score");

		add(errorDescriptionLabel, WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(scoreTitleLabel, scoreLabel));
	}

	public void update(Policy policy) {
		scoreLabel.setText(PolicyUtils.getFormattedLastScore(policy));
	}

	public void setErrorDescription(String errorDescriptionText) {
		errorDescriptionLabel.setVisible(errorDescriptionText != null);
		errorDescriptionLabel.setText(errorDescriptionText);
	}
}