package io.skymind.pathmind.ui.views.dashboard.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.ui.components.PathmindTrainingProgress;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class DashboardLine extends HorizontalLayout {

	private Span timestamp;
	private Breadcrumbs breadcrumb;
	private HorizontalLayout stages;
	
	private Stage currentStage;
	private DashboardItem dashboardItem;
	
	private static String INPROGRESS_INDICATOR = "...";
	
	public DashboardLine(DashboardItem item, SerializableConsumer<DashboardItem> clickHandler) {
		this.dashboardItem = item;
		setClassName("dashboard-line");
		breadcrumb = new Breadcrumbs(item.getProject(), item.getModel(), item.getExperiment(), false);
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
		
		addClickListener(evt -> clickHandler.accept(item));
	}

	private HorizontalLayout createStages() {
		HorizontalLayout stagesContainer = new HorizontalLayout();
		stagesContainer.setClassName("stages-container");
		stagesContainer.add(createStageItem(Stage.SetUpSimulation));
		stagesContainer.add(createSeparator());
		stagesContainer.add(createStageItem(Stage.WriteRewardFunction));
		stagesContainer.add(createSeparator());
		stagesContainer.add(createStageItem(Stage.TrainPolicy));
		stagesContainer.add(createSeparator());
		stagesContainer.add(createStageItem(Stage.Export));
		return stagesContainer;
	}

	private Span createStageItem(Stage stage) {
		Span item = null; 
		if (stage.getValue() < currentStage.getValue()) {
			item = new Span(VaadinIcon.CHECK.create(), new Text(stage.getNameAfterDone()));
			item.setClassName("stage-done");
		} else if (stage.getValue() == currentStage.getValue()) {
			if (DashboardUtils.isTrainingInProgress(stage, dashboardItem.getLatestRun())) {
				PathmindTrainingProgress trainingProgress = new PathmindTrainingProgress();
				updateProgress(trainingProgress, dashboardItem);
				item = new Span(new Text(stage.getNameAfterDone() + INPROGRESS_INDICATOR), trainingProgress);
				item.setClassName("stage-active");
			} else if (DashboardUtils.isTrainingInFailed(stage, dashboardItem.getLatestRun())) {
				item = new Span(VaadinIcon.CLOSE.create(), new Text(stage.getNameAfterDone()));
				item.setClassName("stage-failed");
			} else {
				item = new Span(stage.getNameAfterDone());
				item.setClassName("stage-active");
			}
		} else {
			item = new Span(stage.getName());
			item.setClassName("stage-next");
		}
		return item;
	}
	
	private Span createSeparator() {
		return new Span(">");
	}
	
	private void updateProgress(PathmindTrainingProgress trainingProgress, DashboardItem item) {
		final double progress = ExperimentUtils.calculateProgressByIterationsProcessed(item.getIterationsProcessed());
		if (progress > 0 && progress <= 100) {
			final double estimatedTime = ExperimentUtils.getEstimatedTrainingTime(item.getLatestRun().getStartedAt(), progress);
			trainingProgress.setValue(progress, estimatedTime);
		}
	}
	
}
