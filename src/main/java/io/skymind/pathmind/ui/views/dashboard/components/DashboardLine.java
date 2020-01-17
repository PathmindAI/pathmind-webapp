package io.skymind.pathmind.ui.views.dashboard.components;

import static io.skymind.pathmind.constants.RunStatus.isRunning;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.ui.components.ElapsedTimer;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.views.dashboard.filter.utils.DashboardUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class DashboardLine extends HorizontalLayout {

	private Span timestamp;
	private Breadcrumbs breadcrumb;
	private HorizontalLayout stages;
	
	private Stage currentStage;
	private DashboardItem item;
	
	public DashboardLine(DashboardItem item) {
		this.item = item;
		setClassName("dashboard-line");
		breadcrumb = new Breadcrumbs(item.getProject(), item.getModel(), item.getExperiment());
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			timestamp = new Span(DateAndTimeUtils.formatDateAndTimeShortFormatter(item.getLatestUpdateTime(), timeZoneId));
		});
		
		currentStage = DashboardUtils.calculateStage(item);
		stages = createStages();
		VerticalLayout wrapper = new VerticalLayout(timestamp, breadcrumb, stages);
		wrapper.setPadding(false);
		Span navigateIcon = new Span(VaadinIcon.CHEVRON_RIGHT.create());
		navigateIcon.setClassName("navigate-icon");
		add(wrapper, navigateIcon);
	}

	private HorizontalLayout createStages() {
		HorizontalLayout stagesContainer = new HorizontalLayout();
		stagesContainer.setClassName("stages-container");
		stagesContainer.add(createStageItem(Stage.SetUpSimulation));
		stagesContainer.add(createSeparator());
		stagesContainer.add(createStageItem(Stage.WriteRewardFunction));
		stagesContainer.add(createSeparator());
		stagesContainer.add(createStageItem(Stage.DiscoveryRunTraining));
		stagesContainer.add(createSeparator());
		stagesContainer.add(createStageItem(Stage.FullRunTraining));
		stagesContainer.add(createSeparator());
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
				elapsedTimer.setTextAlignment("center");
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
		return item.getExperiment().getRuns().stream().findAny().get();
	}

	private boolean isTrainingInProgress(Stage stage) {
		if (stage != Stage.DiscoveryRunTraining && stage != Stage.FullRunTraining) {
			return false;
		}
		return RunStatus.isRunning(getLatestRun().getStatusEnum());
	}

	private Span createSeparator() {
		return new Span(">");
	}
	
	private void updateElapsedTimer(ElapsedTimer elapsedTimer, Run run) {
		final var elapsedTime = RunUtils.getElapsedTime(run);
		elapsedTimer.updateTimer(elapsedTime, isRunning(run.getStatusEnum()));
	}
	
}
