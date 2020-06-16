package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.PROJECT_TITLE;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.webapp.ui.views.dashboard.utils.Stage;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindTrainingProgress;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

public class DashboardLine extends HorizontalLayout {

	private Span timestamp;
	private Breadcrumbs breadcrumb;
	private HorizontalLayout stages;
	private Button menuButton;
	private ContextMenu contextMenu;
	
	private Stage currentStage;
	private DashboardItem dashboardItem;
	
	private static String INPROGRESS_INDICATOR = "...";
	private String experimentNotes = "—";
	
	public DashboardLine(DashboardItem item, SerializableConsumer<DashboardItem> clickHandler, SerializableConsumer<DashboardItem> archiveAction) {
		this.dashboardItem = item;
		setClassName("dashboard-line");
		breadcrumb = new Breadcrumbs(item.getProject(), item.getModel(), item.getExperiment(), false);
		timestamp = new Span();
		
		Span projectTitle = new Span(item.getProject().getName());
		projectTitle.addClassName(PROJECT_TITLE);
		
		menuButton = new Button(VaadinIcon.ELLIPSIS_DOTS_H.create());
		menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		contextMenu = new ContextMenu(menuButton);
		contextMenu.addItem("Archive", evt -> archiveAction.accept(item));
		contextMenu.setOpenOnClick(true);
		
		currentStage = DashboardUtils.calculateStage(item);
		stages = createStages();
		VerticalLayout wrapper = new VerticalLayout(projectTitle, timestamp, breadcrumb, stages);
		wrapper.setPadding(false);
		wrapper.addClassName("dashboard-item-main");
		Span navigateIcon = new Span(VaadinIcon.CHEVRON_RIGHT.create());
		navigateIcon.setClassName("navigate-icon");
		VerticalLayout iconContainer = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(menuButton, navigateIcon);
		iconContainer.setWidth(null);
		iconContainer.setClassName("dashboard-item-icons");

		VerticalLayout notesWrapper = new VerticalLayout();
		notesWrapper.addClassName("dashboard-item-notes");
		if (item.getExperiment() != null) {
			if (!item.getExperiment().getUserNotes().isEmpty()) {
				experimentNotes = item.getExperiment().getUserNotes();
			}
			notesWrapper.add(LabelFactory.createLabel("Experiment notes", "bold-label"), new Paragraph(experimentNotes));
		} 
		add(wrapper, notesWrapper, iconContainer);

		// When a link in breadcrumb is clicked, the same click event is also triggered for DashboardLine
		// We cannot stop propagation yet (see issue: https://github.com/vaadin/flow/issues/1363)
		// but applied the workaround suggested in the issue
		breadcrumb.getChildren()
			.filter(comp -> RouterLink.class.isInstance(comp))
			.forEach(comp -> comp.getElement().addEventListener("click", evt -> {}).addEventData("event.stopPropagation()"));
		menuButton.getElement().addEventListener("click", evt -> {}).addEventData("event.stopPropagation()");
		addClickListener(evt -> clickHandler.accept(item));
	}
	
	@Override
	protected void onAttach(AttachEvent evt) {
		VaadinDateAndTimeUtils.withUserTimeZoneId(evt.getUI(), timeZoneId -> {
			timestamp.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(dashboardItem.getLatestUpdateTime(), timeZoneId));
		});
	}

	private HorizontalLayout createStages() {
		HorizontalLayout stagesContainer = new HorizontalLayout();
		stagesContainer.setClassName("stages-container");
		stagesContainer.add(createStageItem(Stage.SetUpSimulation));
		stagesContainer.add(createStageItem(Stage.WriteRewardFunction));
		stagesContainer.add(createStageItem(Stage.TrainPolicy));
		stagesContainer.add(createStageItem(Stage.Export));
		return stagesContainer;
	}

	private Span createStageItem(Stage stage) {
		Span item = null; 
		if (stage.getValue() < currentStage.getValue()) {
			item = new Span(VaadinIcon.CHECK_CIRCLE.create(), new Text(stage.getNameAfterDone()));
			item.setClassName("stage-done");
		} else if (stage.getValue() == currentStage.getValue()) {
			if (DashboardUtils.isTrainingInProgress(stage, dashboardItem.getLatestRun())) {
				PathmindTrainingProgress trainingProgress = new PathmindTrainingProgress();
				updateProgress(trainingProgress, dashboardItem);
				item = new Span(new Text(stage.getNameAfterDone() + INPROGRESS_INDICATOR), trainingProgress);
				item.setClassName("stage-active");
            } else if (DashboardUtils.isTrainingStopped(stage, dashboardItem.getLatestRun()) || DashboardUtils.isTrainingInFailed(stage, dashboardItem.getLatestRun())) {
                if (ExperimentUtils.getTrainingStatus(dashboardItem.getExperiment()) == RunStatus.Error) {
                    item = new Span(VaadinIcon.EXCLAMATION_CIRCLE_O.create(), new Text(stage.getNameAfterDone()));
                    item.setClassName("stage-failed");
                } else {
                    String trainingStatusText = dashboardItem.getLatestRun().getStatusEnum() == RunStatus.Stopping ? "Stopping" : "Stopped";
                    Span stoppedIcon = new Span();
                    stoppedIcon.addClassName("icon-stopped");
                    item = new Span(stoppedIcon, new Span(new Text(stage.getNameAfterDone()), new Html("<br/>"), LabelFactory.createLabel(trainingStatusText, "hint-label")));
                    item.setClassName("stage-stopped");
                }
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
	
	private void updateProgress(PathmindTrainingProgress trainingProgress, DashboardItem item) {
		final double progress = ExperimentUtils.calculateProgressByIterationsProcessed(item.getIterationsProcessed());
		if (progress >= 0 && progress <= 100) {
			final double estimatedTime = ExperimentUtils.getEstimatedTrainingTime(item.getLatestRun().getEc2CreatedAt(), progress);
			trainingProgress.setValue(progress, estimatedTime);
		}
	}
	
}
