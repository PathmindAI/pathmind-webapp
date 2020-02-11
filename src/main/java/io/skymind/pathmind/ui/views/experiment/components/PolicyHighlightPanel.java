package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.LabelFactory;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Span policyLabel = LabelFactory.createLabel("");
	private Span scoreLabel = LabelFactory.createLabel("");
	private Span algorithmLabel = LabelFactory.createLabel("");

	public PolicyHighlightPanel()
	{
		setWidthFull();
		setPadding(false);

		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP));

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

}