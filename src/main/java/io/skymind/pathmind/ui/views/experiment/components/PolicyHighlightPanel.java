package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.components.PathmindLabelBox;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Span errorDescriptionLabel = LabelFactory.createLabel("", "tag", "error-label");
	private PathmindLabelBox scoreLabel;
	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
//<<<<<<< HEAD
//
//
//		FormLayout formLayout = new FormLayout();
//		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP));
//		formLayout.add(errorDescriptionLabel, 3);
//		formLayout.addFormItem(policyLabel, "Policy").addClassNames("label-box","policy");
//		formLayout.addFormItem(scoreLabel, "Score").addClassNames("label-box","score");
//		formLayout.addFormItem(algorithmLabel, "Algorithm").addClassNames("label-box","algorithm");
//
//		add(formLayout);
//=======
		errorDescriptionLabel.setVisible(false);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		
		scoreLabel = new PathmindLabelBox("Score");
		scoreLabel.addClassName("score");

		add(scoreLabel);
//>>>>>>> dev
	}

	public void update(Policy policy) {
		scoreLabel.setValue(PolicyUtils.getFormattedLastScore(policy));
	}

	public void setErrorDescription(String errorDescriptionText) {
		errorDescriptionLabel.setVisible(errorDescriptionText != null);
		errorDescriptionLabel.setText(errorDescriptionText);
	}
}