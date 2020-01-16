package io.skymind.pathmind.ui.views.dashboard.components;

import static io.skymind.pathmind.constants.RunStatus.isRunning;

import java.util.List;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.ui.components.ElapsedTimer;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class DashboardItem extends HorizontalLayout {

	private Span timestamp;
	private Breadcrumbs breadcrumb;
	private HorizontalLayout stages;
	
	private Stage currentStage;
	private Experiment experiment;
	
	public DashboardItem(Experiment experiment) {
		this.experiment = experiment;
		setClassName("dashboard-item");
		breadcrumb = new Breadcrumbs(experiment.getProject(), getModelIfExist(experiment.getModel()), getExperimentIfExist(experiment));
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			timestamp = new Span(DateAndTimeUtils.formatDateAndTimeShortFormatter(experiment.getLastActivityDate(), timeZoneId));
		});
		
		currentStage = calculateCurrentStage();
		stages = createStages();
		VerticalLayout wrapper = new VerticalLayout(timestamp, breadcrumb, stages);
		wrapper.setPadding(false);
		Span navigateIcon = new Span(VaadinIcon.CHEVRON_RIGHT.create());
		navigateIcon.setClassName("navigate-icon");
		add(wrapper, navigateIcon);
	}

	private Model getModelIfExist(Model model) {
		if (model.getId() == 0) {
			return null;
		} else {
			return model;
		}
	}

	private Experiment getExperimentIfExist(Experiment experiment) {
		if (experiment.getId() == 0) {
			return null;
		} else {
			return experiment;
		}
	}

	private Stage calculateCurrentStage() {
		if (experiment.getModel().getId() == 0) {
			return Stage.SetUpSimulation;
		} else if (experiment.getId() == 0) {
			return Stage.WriteRewardFunction;
		} else if (!hasCompletedRunOfType(experiment.getRuns(), RunType.DiscoveryRun)) {
			return Stage.DiscoveryRunTraining;
		} else if (!hasCompletedRunOfType(experiment.getRuns(), RunType.FullRun)) {
			return Stage.FullRunTraining;
		} else {
			return Stage.Export;
		}
	}

	private boolean hasCompletedRunOfType(List<Run> runs, RunType runType) {
		return runs.stream().anyMatch(run -> run.getRunTypeEnum() == runType && run.getStatusEnum() == RunStatus.Completed);
	}

	private HorizontalLayout createStages() {
		HorizontalLayout stagesContainer = new HorizontalLayout();
		stagesContainer.setClassName("stages-container");
		stagesContainer.add(createStageItem(Stage.SetUpSimulation));
		stagesContainer.add(createSeperator());
		stagesContainer.add(createStageItem(Stage.WriteRewardFunction));
		stagesContainer.add(createSeperator());
		stagesContainer.add(createStageItem(Stage.DiscoveryRunTraining));
		stagesContainer.add(createSeperator());
		stagesContainer.add(createStageItem(Stage.FullRunTraining));
		stagesContainer.add(createSeperator());
		stagesContainer.add(createStageItem(Stage.Export));
		return stagesContainer;
	}

	private Span createStageItem(Stage stage) {
		Span item = null; 
		if (stage.getValue() < currentStage.getValue()) {
			item = new Span(VaadinIcon.CHECK.create(), new Text(stage.toString()));
			item.setClassName("stage-done");
		} else if (stage.getValue() == currentStage.getValue()) {
			if (isTrainingInProgress(stage)) {
				ElapsedTimer elapsedTimer = new ElapsedTimer();
				updateElapsedTimer(elapsedTimer, getLatestRun());
				item = new Span(VaadinIcon.HOURGLASS.create(), new Text(stage.toString()), elapsedTimer);
			} else {
				item = new Span(stage.toString());
			}
			item.setClassName("stage-active");
		} else {
			item = new Span(stage.toString());
			item.setClassName("stage-next");
		}
		return item;
	}
	
	// TODO: How to find latest Run?
	private Run getLatestRun() {
		return experiment.getRuns().stream().findAny().get();
	}

	private boolean isTrainingInProgress(Stage stage) {
		if (stage != Stage.DiscoveryRunTraining && stage != Stage.FullRunTraining) {
			return false;
		}
		return RunStatus.isRunning(getLatestRun().getStatusEnum());
	}

	private Span createSeperator() {
		return new Span(">");
	}
	
	private void updateElapsedTimer(ElapsedTimer elapsedTimer, Run run) {
		final var elapsedTime = RunUtils.getElapsedTime(run);
		elapsedTimer.updateTimer(elapsedTime, isRunning(run.getStatusEnum()));
	}
	
}
