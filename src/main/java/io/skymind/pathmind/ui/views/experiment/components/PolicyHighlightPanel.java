package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.ui.utils.PushUtils;
import reactor.core.publisher.Flux;

public class PolicyHighlightPanel extends VerticalLayout
{
	private Policy policy;

	private Label policyLabel = new Label();
	private Label scoreLabel = new Label();
	private Label algorithmLabel = new Label();

	private Flux<PathmindBusEvent> consumer;

	public PolicyHighlightPanel(Flux<PathmindBusEvent> consumer)
	{
		this.consumer = consumer;

		setWidthFull();
		setPadding(false);

		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP));

		formLayout.addFormItem(policyLabel, "Policy").addClassNames("label-box","policy");
		formLayout.addFormItem(scoreLabel, "Score").addClassNames("label-box","score");
		formLayout.addFormItem(algorithmLabel, "Algorithm").addClassNames("label-box","algorithm");

		add(formLayout);
	}

	public void update(Policy policy)
	{
		updateComponentsForPolicy(policy);
		subscribeToEventBus(consumer);
	}

	private void updateComponentsForPolicy(Policy policy) {
		this.policy = policy;
		policyLabel.setText(PolicyUtils.getParsedPolicyName(policy));
		scoreLabel.setText(PolicyUtils.getLastScore(policy));
		algorithmLabel.setText(ProgressInterpreter.interpretKey(policy.getName()).getAlgorithm());
	}

	private Policy getPolicy() {
		return policy;
	}

	private void subscribeToEventBus(Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnPolicy(
				consumer,
                this::getPolicy,
				updatedPolicy -> PushUtils.push(getUI().orElseGet(null), () -> updateComponentsForPolicy(updatedPolicy)));
	}
}
