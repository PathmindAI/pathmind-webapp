package io.skymind.pathmind.ui.views.dashboard.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.utils.DateAndTimeUtils;

public class DashboardItem extends HorizontalLayout {

	private Span timestamp;
	private Breadcrumbs breadcrumb;
	private HorizontalLayout stages;
	
	private Stage currentStage;
	
	public DashboardItem(Policy policy) {
		setClassName("dashboard-item");
		breadcrumb = new Breadcrumbs(policy.getProject(), policy.getModel(), policy.getExperiment());
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			timestamp = new Span(DateAndTimeUtils.formatDateAndTimeShortFormatter(policy.getStartedAt(), timeZoneId));
		});
		
		currentStage = calculateCurrentStage();
		stages = createStages();
		VerticalLayout wrapper = new VerticalLayout(timestamp, breadcrumb, stages);
		wrapper.setPadding(false);
		Span navigateIcon = new Span(VaadinIcon.CHEVRON_RIGHT.create());
		navigateIcon.setClassName("navigate-icon");
		add(wrapper, navigateIcon);
	}

	private Stage calculateCurrentStage() {
		return Stage.WriteRewardFunction;
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
			item = new Span(stage.toString());
			item.setClassName("stage-active");
		} else {
			item = new Span(stage.toString());
			item.setClassName("stage-next");
		}
		return item;
	}
	
	private Span createSeperator() {
		return new Span(">");
	}
	
}
