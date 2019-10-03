package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Policy;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Label policyLabel = new Label();
	private Label scoreLabel = new Label();
	private Label algorithmLabel = new Label();

	public PolicyHighlightPanel()
	{
		setWidthFull();

		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP));

		formLayout.addFormItem(policyLabel, "Policy");
		formLayout.addFormItem(scoreLabel, "Score");
		formLayout.addFormItem(algorithmLabel, "Algorithm");

		add(formLayout);
	}

	public void update(Policy policy) {
		policyLabel.setText(policy.getName());
		// TODO -> How do we calculate?
//		scoreLabel.setText(policy.getProgress());
		algorithmLabel.setText(policy.getAlgorithm().toString());
	}
}
