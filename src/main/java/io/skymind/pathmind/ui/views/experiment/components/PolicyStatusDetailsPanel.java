package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PolicyStatusDetailsPanel extends VerticalLayout implements PolicyUpdateSubscriber
{
	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runProgressLabel = new Label();
	private Label runTypeLabel = new Label();
	private Label elapsedTimeLabel = new Label();

	private Policy policy;

	public PolicyStatusDetailsPanel()
	{
		Label[] labels = Arrays.asList(
				getElementLabel("Status"),
				getElementLabel(""),
				getElementLabel("Run Type"),
				getElementLabel("Elapsed"))
				.stream().toArray(Label[]::new);

		removeTopMargins(labels);
		removeTopMargins(statusLabel, runProgressLabel, runTypeLabel, elapsedTimeLabel);

		VerticalLayout leftVerticalLayout = new VerticalLayout(labels);
		leftVerticalLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		leftVerticalLayout.setWidthFull();
		leftVerticalLayout.setPadding(false);

		VerticalLayout rightVerticalLayout = new VerticalLayout(
				statusLabel,
				runProgressLabel,
				runTypeLabel,
				elapsedTimeLabel);
		rightVerticalLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
		rightVerticalLayout.setPadding(false);

		HorizontalLayout wrapper = new HorizontalLayout(
				leftVerticalLayout,
				rightVerticalLayout);
		wrapper.getStyle().set("padding-top", "10px");
		wrapper.setWidthFull();

		add(wrapper);
	}

	// TODO -> CSS -> Move style to CSS
	private Label getElementLabel(String label) {
		Label fieldLabel = new Label(label + " : ");
		fieldLabel.getStyle().set("font-weight", "bold");
		return fieldLabel;
	}

	private void removeTopMargins(Label... labels) {
		Arrays.stream(labels).forEach(label ->
				label.getStyle().set("margin-top", "0px"));
	}

	public void update(Policy policy)
	{
		this.policy = policy;

		statusLabel.setText(PolicyUtils.getRunStatus(policy).toString());
		runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy)));
		runTypeLabel.setText(policy.getRun().getRunTypeEnum().toString());
		elapsedTimeLabel.setText(PolicyUtils.getElapsedTime(policy));
	}

	private Policy getPolicy() {
		return policy;
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
		this.policy = event.getPolicy();
		PushUtils.push(this, () -> update(event.getPolicy()));
	}

	@Override
	public boolean filterBusEvent(PolicyUpdateBusEvent event) {
		return getPolicy().getId() == event.getPolicy().getId();
	}
}
