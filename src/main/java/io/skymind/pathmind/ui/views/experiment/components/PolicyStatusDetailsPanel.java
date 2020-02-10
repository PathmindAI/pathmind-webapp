package io.skymind.pathmind.ui.views.experiment.components;

import static io.skymind.pathmind.constants.RunStatus.isRunning;
import static io.skymind.pathmind.ui.constants.CssMindPathStyles.BOLD_LABEL;

import java.util.Arrays;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.ui.components.ElapsedTimer;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class PolicyStatusDetailsPanel extends VerticalLayout implements PolicyUpdateSubscriber
{
	private Span statusLabel = LabelFactory.createLabel(RunStatus.NotStarted.toString());
	private Span runProgressLabel = LabelFactory.createLabel("");
	private Span runTypeLabel = LabelFactory.createLabel("");
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();

	private Policy policy;

	public PolicyStatusDetailsPanel()
	{
		Span[] labels = Arrays.asList(
				getElementLabel("Status"),
				getElementLabel(""),
				getElementLabel("Run Type"),
				getElementLabel("Elapsed"))
				.stream().toArray(Span[]::new);

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

	private Span getElementLabel(String label) {
		return LabelFactory.createLabel(label + " : ",BOLD_LABEL);
	}

	private void removeTopMargins(HasStyle... labels) {
		Arrays.stream(labels).forEach(label ->
				label.getStyle().set("margin-top", "0px"));
	}

	public void update(Policy policy)
	{
		this.policy = policy;

		statusLabel.setText(PolicyUtils.getRunStatus(policy).toString());
		runTypeLabel.setText(policy.getRun().getRunTypeEnum().toString());
		updateElapsedTimer(policy);
		DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy), userTimeZone));
		});
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
		return getPolicy() != null && getPolicy().getId() == event.getPolicy().getId();
	}

	private void updateElapsedTimer(Policy policy) {
		final var runStatus = PolicyUtils.getRunStatus(policy);
		final var elapsedTime = RunUtils.getElapsedTime(policy.getRun());
		elapsedTimeLabel.updateTimer(elapsedTime, isRunning(runStatus));
	}
}