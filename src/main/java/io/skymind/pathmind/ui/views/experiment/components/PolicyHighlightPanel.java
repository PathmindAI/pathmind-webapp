package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.PathmindLabelBox;

public class PolicyHighlightPanel extends VerticalLayout
{
	private PathmindLabelBox scoreLabel;

	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		
		scoreLabel = new PathmindLabelBox("Score");
		scoreLabel.addClassName("score");

		add(scoreLabel);
	}

	public void update(Policy policy) {
		scoreLabel.setValue(PolicyUtils.getFormattedLastScore(policy));
	}

}