package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.utils.PushUtils;

public class PolicyHighlightPanel extends VerticalLayout implements PolicyUpdateSubscriber
{
	private Label policyLabel = new Label();
	private Label scoreLabel = new Label();
	private Label algorithmLabel = new Label();

	private Policy policy;

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
		this.policy = policy;
		policyLabel.setText(PolicyUtils.getParsedPolicyName(policy));
		scoreLabel.setText(PolicyUtils.getFormattedLastScore(policy));
		algorithmLabel.setText(policy.getAlgorithmEnum().name());
	}

	@Override
	protected void onDetach(DetachEvent event) {
		EventBus.unsubscribe(this);
	}

	@Override
	protected void onAttach(AttachEvent event) {
		EventBus.subscribe(this);
	}

	@Override
	public void handleBusEvent(PolicyUpdateBusEvent event) {
		PushUtils.push(this, () -> update(event.getPolicy()));
	}

	@Override
	public boolean filterBusEvent(PolicyUpdateBusEvent event) {
		return policy.getId() == event.getPolicy().getId();
	}
}