package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Logger log = LogManager.getLogger(PolicyHighlightPanel.class);

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
		String policyName = null;
		try {
			String[] split = policy.getName().split("_");
//			policyName = split[1] + "_" + split[2];
			policyName = "#" + split[2];
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		policyLabel.setText(policyName);

		String policyScore = null;
		try {
			policyScore = policy.getScores().get(policy.getScores().size() - 1).toString();
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		scoreLabel.setText(policyScore);
		algorithmLabel.setText(ProgressInterpreter.interpretKey(policy.getName()).getAlgorithm());
	}
}
