package io.skymind.pathmind.ui.views.dashboard.components;

import static io.skymind.pathmind.constants.RunStatus.isRunning;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.ui.components.ElapsedTimer;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class DashboardLine extends HorizontalLayout {

	private Span timestamp;
	private Breadcrumbs breadcrumb;
	private HorizontalLayout stages;
	
	private Stage currentStage;
	private DashboardItem dashboardItem;
	
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

		// When a link in breadcrumb is clicked, the same click event is also triggered for DashboardLine
		// We cannot stop propagation yet (see issue: https://github.com/vaadin/flow/issues/1363)
		// but applied the workaround suggested in the issue
		breadcrumb.getElement().addEventListener("click", evt -> {}).addEventData("event.stopPropagation()");
		addClickListener(evt -> clickHandler.accept(item));
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
			if (DashboardUtils.isTrainingInProgress(stage, dashboardItem.getLatestRun())) {
				ElapsedTimer elapsedTimer = new ElapsedTimer();
				updateElapsedTimer(elapsedTimer, dashboardItem.getLatestRun());
				item = new Span(VaadinIcon.HOURGLASS.create(), new Text(stage.toString()), elapsedTimer);
				item.setClassName("stage-active");
			} else if (DashboardUtils.isTrainingInFailed(stage, dashboardItem.getLatestRun())) {
				item = new Span(VaadinIcon.CLOSE.create(), new Text(stage.toString()));
				item.setClassName("stage-failed");
			} else {
				item = new Span(stage.toString());
				item.setClassName("stage-active");
			}
		} else {
			item = new Span(stage.toString());
			item.setClassName("stage-next");
		}
		return item;
	}
	
	private Span createSeparator() {
		return new Span(">");
	}
	
	private void updateElapsedTimer(ElapsedTimer elapsedTimer, Run run) {
		final var elapsedTime = RunUtils.getElapsedTime(run);
		elapsedTimer.updateTimer(elapsedTime, isRunning(run.getStatusEnum()));
	}
	
}
