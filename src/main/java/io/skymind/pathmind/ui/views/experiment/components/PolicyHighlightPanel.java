package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.LabelFactory;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Label policyLabel = new Label();
	private Label scoreLabel = new Label();
	private Label algorithmLabel = new Label();
	private Label errorDescriptionLabel = LabelFactory.createLabel("", "tag error-label");

	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);
		
		errorDescriptionLabel.setVisible(false);

		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP));
		formLayout.add(errorDescriptionLabel, 3);
		formLayout.addFormItem(policyLabel, "Policy").addClassNames("label-box","policy");
		formLayout.addFormItem(scoreLabel, "Score").addClassNames("label-box","score");
		formLayout.addFormItem(algorithmLabel, "Algorithm").addClassNames("label-box","algorithm");

		add(formLayout);
	}

	public void update(Policy policy) {
		policyLabel.setText(policy.getName());
		scoreLabel.setText(PolicyUtils.getFormattedLastScore(policy));
		algorithmLabel.setText(policy.getAlgorithmEnum().name());
	}

	public void setErrorDescription(String errorDescriptionText) {
		errorDescriptionLabel.setVisible(errorDescriptionText != null);
		errorDescriptionLabel.setText(errorDescriptionText);
	}

}