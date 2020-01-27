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
	private VerticalLayout progressRow = new VerticalLayout(progressBar, progressValueLabel);

	// TODO (KW): 25.01.2020 remove unused fields
	private Policy policy;

	// TODO (KW): 27.01.2020 remove if not used
	private PolicyDAO policyDAO;

	public PolicyStatusDetailsPanel(PolicyDAO policyDAO) {
		VerticalLayout wrapper = new VerticalLayout();
		var statusRow = createHorizontalLayout("Status", statusLabel);
		var runProgressRow = createHorizontalLayout("", runProgressLabel);
		var runTypeRow = createHorizontalLayout("Run Type", runTypeLabel);
		var elapsedTimeRow = createHorizontalLayout("Elapsed", elapsedTimeLabel);

		styleProgressLayout();

		wrapper.add(statusRow, progressRow, runProgressRow, runTypeRow, elapsedTimeRow);
		wrapper.getStyle().set("padding-top", "10px");
		wrapper.setWidthFull();

		add(wrapper);

		this.policyDAO = policyDAO;
	}

	private void styleProgressLayout() {
		progressBar.getStyle().set("margin", "0px").set("max-width", "200px");
		progressRow.getStyle().set("margin-top" ,"24px").set("margin-left", "110px");
		progressRow.setPadding(false);
	}

	private HorizontalLayout createHorizontalLayout(String labelTitle, Label valueLabel) {
		final var elementLabel = getElementLabel(labelTitle);
		final var horizontalLayout = new HorizontalLayout(elementLabel, valueLabel);
		horizontalLayout.getStyle().set("margin-top" ,"0px");
		valueLabel.getStyle().set("margin-top", "0px");
		horizontalLayout.setPadding(false);
		return horizontalLayout;
	}

	// TODO -> CSS -> Move style to CSS
	private Label getElementLabel(String label) {
		Label fieldLabel = new Label(label + ":");
		fieldLabel.getStyle()
				.set("font-weight", "bold")
				.set("margin-top", "0px")
				.set("min-width", "90px")
				.set("text-align", "right");
		return fieldLabel;
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
		updateElapsedTimer(experiment, max);
		DateAndTimeUtils.withUserTimeZoneId(userTimeZone -> {
//			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy), userTimeZone));
			runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(getTrainingCompletedTime(experiment), userTimeZone));
		});
		if(max == Running) {
			progressRow.setVisible(true);
			updateProgressBar(experiment);
		} else {
			progressRow.setVisible(false);
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

	private void updateElapsedTimer(Experiment experiment, RunStatus trainingStatus) {
		getTrainingStartedDate(experiment).ifPresent(time ->
				elapsedTimeLabel.updateTimer(Duration.between(time, LocalDateTime.now()).toSeconds(), isRunning(trainingStatus)));
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