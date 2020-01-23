package io.skymind.pathmind.ui.views.experiment.components;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.progressbar.ProgressBar;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.db.dao.PolicyDAO;

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
import io.skymind.pathmind.services.training.constant.RunConstants;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.stereotype.Component;

import static io.skymind.pathmind.constants.RunStatus.isRunning;
import static io.skymind.pathmind.services.training.constant.RunConstants.*;

@Component
public class PolicyStatusDetailsPanel extends VerticalLayout implements PolicyUpdateSubscriber
{
	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runProgressLabel = new Label();
	private Label runTypeLabel = new Label();
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
	private ProgressBar progressBar = new ProgressBar(0, 100);
	private Label progressValueLabel = new Label();

	private Policy policy;

	private PolicyDAO policyDAO;

	public PolicyStatusDetailsPanel(PolicyDAO policyDAO)
	{
		Label[] labels = Arrays.asList(
				getElementLabel("Status"),
				getElementLabel(""),
				getElementLabel("Run Type"),
				getElementLabel("Elapsed"),
				getElementLabelWithoutColon(""),
				getElementLabel("Progress"))
				.stream().toArray(Label[]::new);

		removeTopMargins(labels);
		removeTopMargins(statusLabel, runProgressLabel, runTypeLabel, elapsedTimeLabel, progressValueLabel);
		progressBar.getStyle().set("margin-top", "12px");


		VerticalLayout leftVerticalLayout = new VerticalLayout(labels);
		leftVerticalLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		leftVerticalLayout.setWidthFull();
		leftVerticalLayout.setPadding(false);

		VerticalLayout rightVerticalLayout = new VerticalLayout(
				statusLabel,
				runProgressLabel,
				runTypeLabel,
				elapsedTimeLabel,
				progressBar,
				progressValueLabel);

		rightVerticalLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
		rightVerticalLayout.setPadding(false);

		HorizontalLayout wrapper = new HorizontalLayout(
				leftVerticalLayout,
				rightVerticalLayout);
		wrapper.getStyle().set("padding-top", "10px");
		wrapper.setWidthFull();

		add(wrapper);
		this.policyDAO = policyDAO;
	}

	// TODO -> CSS -> Move style to CSS
	private Label getElementLabel(String label) {
		Label fieldLabel = new Label(label + " : ");
		fieldLabel.getStyle().set("font-weight", "bold");
		return fieldLabel;
	}

	// TODO (KW): 22.01.2020 refactor
	private Label getElementLabelWithoutColon(String label) {
		Label fieldLabel = new Label(label);
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
		runTypeLabel.setText(policy.getRun().getRunTypeEnum().toString());
		updateElapsedTimer(policy);
		DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy), userTimeZone));
		});
		updateProgressBar(policy.getExperiment().getId());
	}

	// TODO (KW): 23.01.2020 refactor
	private void updateProgressBar(long expId) {
		final var policiesForExperiment = policyDAO.getPoliciesForExperiment(expId);
		final var totalIterations = (double) DISCOVERY_RUN_ITERATIONS * RunConstants.getNumberOfDiscoveryRuns();
		final Integer iterationsProcessed = policiesForExperiment.stream()
				.map(Policy::getScores)
				.map(List::size)
				.reduce(0, Integer::sum);
		final var progress = (iterationsProcessed / totalIterations) * 100;
		double estimatedTime = 0;
		if (progress > 0) {
			final var earliestPolicyStartedDate = policiesForExperiment.stream()
					.map(Policy::getStartedAt)
					.min(LocalDateTime::compareTo)
					.orElse(LocalDateTime.now());
			var difference = Duration.between(earliestPolicyStartedDate, LocalDateTime.now());
			estimatedTime = difference.toSeconds() * (100 - progress) / progress;
		}
		if (progress <= 100) {
			final String formattedEstimatedTime = DateAndTimeUtils.formatDurationTime((long) estimatedTime);
			final String progressValue = String.format("%.0f %% (estimated time: %s)", progress, formattedEstimatedTime);
			progressValueLabel.setText(progressValue);
			progressBar.setValue(progress);
		}
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

	private void updateElapsedTimer(Policy policy) {
		final var runStatus = PolicyUtils.getRunStatus(policy);
		final var elapsedTime = RunUtils.getElapsedTime(policy.getRun());
		elapsedTimeLabel.updateTimer(elapsedTime, isRunning(runStatus));
	}
}