package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.ui.utils.ExperimentViewUtil;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Label policyLabel = new Label();
	private Label scoreLabel = new Label();
	private Label algorithmLabel = new Label();

	public PolicyHighlightPanel()
	{
		setWidthFull();

		// TODO -> Using the FormLayout as a quick solution until we have the CSS to make it look nice.
		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP));

		formLayout.addFormItem(policyLabel, "Policy");
		formLayout.addFormItem(scoreLabel, "Score");
		formLayout.addFormItem(algorithmLabel, "Algorithm");

		add(formLayout);
	}

	public void update(Policy policy) {
		policyLabel.setText(ExperimentViewUtil.getParsedPolicyName(policy));
		scoreLabel.setText(ExperimentViewUtil.getLastScore(policy));
		algorithmLabel.setText(ProgressInterpreter.interpretKey(policy.getName()).getAlgorithm());
	}
}
