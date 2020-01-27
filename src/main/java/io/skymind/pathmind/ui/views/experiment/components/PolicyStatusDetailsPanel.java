package io.skymind.pathmind.ui.views.experiment.components;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.vaadin.flow.component.progressbar.ProgressBar;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.db.dao.PolicyDAO;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.constant.RunConstants;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.stereotype.Component;

import static io.skymind.pathmind.constants.RunStatus.*;
import static io.skymind.pathmind.constants.RunType.FullRun;
import static io.skymind.pathmind.services.training.constant.RunConstants.*;

@Component
// TODO (KW): 25.01.2020 rename class and fields
public class PolicyStatusDetailsPanel extends VerticalLayout /*implements PolicyUpdateSubscriber*/ {
	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runProgressLabel = new Label();
	private Label runTypeLabel = new Label();
	private ElapsedTimer elapsedTimeLabel = new ElapsedTimer();
	private ProgressBar progressBar = new ProgressBar(0, 100);
	private Label progressValueLabel = new Label();
	VerticalLayout progressLayout = new VerticalLayout(progressBar, progressValueLabel);
	private Label progressLabel = getElementLabel("Progress");

	// TODO (KW): 25.01.2020 remove unused fields
	private Policy policy;

	// TODO (KW): 27.01.2020 remove if not used
	private PolicyDAO policyDAO;

	public PolicyStatusDetailsPanel(PolicyDAO policyDAO) {
		Label[] labels = Arrays.asList(
				getElementLabel("Status"),
				getElementLabel(""),
				getElementLabel("Run Type"),
				getElementLabel("Elapsed"),
				getElementLabelWithoutColon(""),
				progressLabel)
				.stream().toArray(Label[]::new);

		removeTopMargins(labels);
		removeTopMargins(statusLabel, runProgressLabel, runTypeLabel, elapsedTimeLabel, progressValueLabel);
		progressBar.getStyle().set("margin-top", "12px");


		VerticalLayout leftVerticalLayout = new VerticalLayout(labels);
		leftVerticalLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		leftVerticalLayout.setWidthFull();
		leftVerticalLayout.setPadding(false);

		progressLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		progressLayout.setWidthFull();
		progressLayout.setPadding(false);

		VerticalLayout rightVerticalLayout = new VerticalLayout(
				statusLabel,
				runProgressLabel,
				runTypeLabel,
				elapsedTimeLabel,
				progressLayout);


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

//	public void update(Policy policy)
//	{
//		this.policy = policy;
//
//		statusLabel.setText(PolicyUtils.getRunStatus(policy).toString());
//		runTypeLabel.setText(policy.getRun().getRunTypeEnum().toString());
//		updateElapsedTimer(policy);
//		DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
//			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy), userTimeZone));
//		});
//		updateProgressBar(policy.getExperiment().getId());
//	}

	public void update(Experiment experiment) {
		// TODO (KW): 25.01.2020 refactor
		final var max = experiment.getPolicies().stream().map(Policy::getRun).map(Run::getStatusEnum).min(Comparator.comparingInt(RunStatus::getValue)).orElse(NotStarted);
		final var runType = experiment.getPolicies().stream().map(Policy::getRun).map(Run::getRunTypeEnum).max(Comparator.comparingInt(RunType::getValue)).orElse(FullRun);


//		statusLabel.setText(PolicyUtils.getRunStatus(policy).toString());
		statusLabel.setText(max.toString());
		runTypeLabel.setText(runType.toString());
		updateElapsedTimer(experiment);
		DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
//			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy), userTimeZone));
			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(getTrainingCompletedTime(experiment), userTimeZone));
		});
		if(max == Running) {
			progressLabel.setVisible(true);
			progressLayout.setVisible(true);
			updateProgressBar(experiment);
		} else {
			progressLabel.setVisible(false);
			progressLayout.setVisible(false);
		}
	}

	// TODO (KW): 23.01.2020 refactor
	private void updateProgressBar(Experiment experiment) {
//		final var policiesForExperiment = policyDAO.getPoliciesForExperiment(expId);
		final var policiesForExperiment = experiment.getPolicies();
		// TODO (KW): 25.01.2020 add full run scenario
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
					.filter(Objects::nonNull)
					.min(LocalDateTime::compareTo)
					.orElse(LocalDateTime.now());
			var difference = Duration.between(earliestPolicyStartedDate, LocalDateTime.now());
			estimatedTime = difference.toSeconds() * (100 - progress) / progress;
		}
		if (progress <= 100) {
			final String formattedEstimatedTime = DateAndTimeUtils.formatDurationTime((long) estimatedTime);
			final String progressValue = String.format("%.0f %% (ETA: %s)", progress, formattedEstimatedTime);
			progressValueLabel.setText(progressValue);
			progressBar.setValue(progress);
		}
	}

	private Policy getPolicy() {
		return policy;
	}

//	@Override
//	protected void onDetach(DetachEvent event) {
//		EventBus.unsubscribe(this);
//	}
//
//	@Override
//	protected void onAttach(AttachEvent event) {
//		EventBus.subscribe(this);
//	}
//
//	@Override
//	public void handleBusEvent(PolicyUpdateBusEvent event) {
////		this.policy = event.getPolicy();
////		PushUtils.push(this, () -> update(event.getPolicy()));
//	}
//
//	@Override
//	public boolean filterBusEvent(PolicyUpdateBusEvent event) {
//		return getPolicy().getId() == event.getPolicy().getId();
//	}

	private void updateElapsedTimer(Experiment experiment) {
		final var runStatus =// PolicyUtils.getRunStatus(policy);
				experiment.getPolicies().stream()
						.map(Policy::getRun)
						.map(Run::getStatusEnum)
						.max(Comparator.comparingInt(RunStatus::getValue))
						.orElse(NotStarted);
		getTrainingStartedDate(experiment).ifPresent(time ->
				elapsedTimeLabel.updateTimer(Duration.between(time, LocalDateTime.now()).toSeconds(), isRunning(runStatus)));
	}

	// TODO (KW): 25.01.2020 move to ExperimentUtils
	private Optional<LocalDateTime> getTrainingStartedDate(Experiment experiment) {
		return experiment.getPolicies().stream()
				.map(Policy::getStartedAt)
				.filter(Objects::nonNull)
				.min(LocalDateTime::compareTo);
	}

	private LocalDateTime getTrainingCompletedTime(Experiment experiment) {
		var stoppedTimes = experiment.getPolicies().stream()
				.map(Policy::getStoppedAt)
				.collect(Collectors.toList());

		if (isAnyPolicyNotFinished(stoppedTimes)) {
			return null;
		}

		return stoppedTimes.stream()
				.max(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	private boolean isAnyPolicyNotFinished(List<LocalDateTime> stoppedTimes) {
		return stoppedTimes.stream().anyMatch(Objects::isNull);
	}
}