package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindTrainingProgress;
import io.skymind.pathmind.webapp.ui.components.atoms.GoalsReachedStatus;
import io.skymind.pathmind.webapp.ui.components.atoms.IconStopped;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.Stage;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.PROJECT_TITLE;

public class DashboardLine extends HorizontalLayout {

    private Span timestamp;
    private Breadcrumbs breadcrumb;
    private FavoriteStar favoriteStar;
    private HorizontalLayout stages;
    private Button menuButton;
    private ContextMenu contextMenu;

    private Stage currentStage;
    private DashboardItem dashboardItem;

    private String experimentNotes = "—";

    public DashboardLine(ExperimentDAO experimentDAO, DashboardItem item, SerializableConsumer<DashboardItem> archiveAction) {
        this.dashboardItem = item;
        Experiment experiment = item.getExperiment();
        setClassName("dashboard-line");
        breadcrumb = new Breadcrumbs(item.getProject(), item.getModel(), experiment, false);
        if (experiment != null) {
            favoriteStar = new FavoriteStar(experiment.isFavorite(), newIsFavorite ->
                    ExperimentGuiUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite));
        }
        timestamp = new Span();

        RouterLink projectTitle = new RouterLink();
        projectTitle.setText(item.getProject().getName());
        projectTitle.addClassName(PROJECT_TITLE);
        setProjectTitleNavigationTarget(projectTitle, item);

        menuButton = new Button(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        contextMenu = new ContextMenu(menuButton);
        contextMenu.addItem("Archive", evt -> archiveAction.accept(item));
        contextMenu.setOpenOnClick(true);

        currentStage = DashboardUtils.calculateStage(item);
        stages = createStages();
        HorizontalLayout itemBreadcrumbsWrapper = new HorizontalLayout(breadcrumb);
        itemBreadcrumbsWrapper.setSpacing(false);
        if (experiment != null) {
            itemBreadcrumbsWrapper.add(favoriteStar);
        }
        VerticalLayout wrapper = new VerticalLayout(projectTitle, timestamp, itemBreadcrumbsWrapper, stages);
        wrapper.setPadding(false);
        wrapper.addClassName("dashboard-item-main");

        VerticalLayout notesWrapper = new VerticalLayout();
        notesWrapper.addClassName("dashboard-item-notes");
        if (item.getExperiment() != null) {
            if (!item.getExperiment().getUserNotes().isEmpty()) {
                experimentNotes = item.getExperiment().getUserNotes();
            }
            notesWrapper.add(new Span("Experiment Notes"), new Paragraph(experimentNotes));
        }
        add(wrapper, notesWrapper, menuButton);
    }

    private void setProjectTitleNavigationTarget(RouterLink projectTitle, DashboardItem item) {
        Stage stage = DashboardUtils.calculateStage(item);
        switch (stage) {
            case SetUpSimulation:
                if (item.getModel() != null && item.getModel().isDraft()) {
                    projectTitle.setRoute(UploadModelView.class, UploadModelView.createResumeUploadTarget(item.getProject(), item.getModel()));
                } else {
                    projectTitle.setRoute(UploadModelView.class, String.valueOf(item.getProject().getId()));
                }
                break;
            case WriteRewardFunction:
                if (item.getExperiment() == null) {
                    // getUI().ifPresent(ui ->
                    // ExperimentUtils.createAndNavigateToNewExperiment(ui,
                    // experimentDAO, item.getModel().getId()));
                } else {
                    projectTitle.setRoute(NewExperimentView.class, ""+item.getExperiment().getId());
                }
                break;
            default:
                projectTitle.setRoute(ExperimentView.class, ""+item.getExperiment().getId());
                break;
        }
    }

    @Override
    protected void onAttach(AttachEvent evt) {
        VaadinDateAndTimeUtils.withUserTimeZoneId(evt.getUI(), timeZoneId -> {
            timestamp.setText("Last Activity: " + DateAndTimeUtils.formatDateAndTimeShortFormatter(dashboardItem.getLatestUpdateTime(), timeZoneId));
        });
    }

    private HorizontalLayout createStages() {
        HorizontalLayout stagesContainer = WrapperUtils.wrapWidthFullBetweenHorizontal();
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
            Icon successIcon = VaadinIcon.CHECK_CIRCLE.create();
            successIcon.getElement().setAttribute("title", "Done");
            item = new Span(new Span(successIcon, new Text(stage.getNameAfterDone())));
            item.setClassName("stage-done");
        } else if (stage.getValue() == currentStage.getValue()) {
            if (DashboardUtils.isTrainingInProgress(stage, dashboardItem.getLatestRun())) {
                PathmindTrainingProgress trainingProgress = new PathmindTrainingProgress();
                trainingProgress.setTextMode(true);
                updateProgress(trainingProgress, dashboardItem);
                item = new Span(trainingProgress);
                item.setClassName("stage-active");
            } else if (DashboardUtils.isTrainingStopped(stage, dashboardItem.getLatestRun()) || DashboardUtils.isTrainingInFailed(stage, dashboardItem.getLatestRun())) {
                if (dashboardItem.getExperiment().getTrainingStatusEnum() == RunStatus.Error) {
                    Icon errorIcon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
                    errorIcon.getElement().setAttribute("title", "Failed");
                    item = new Span(new Span(errorIcon, new Span(stage.getNameAfterDone())));
                    item.setClassName("stage-failed");
                } else {
                    item = new Span(new Span(new IconStopped(), new Span(stage.getNameAfterDone())));
                    item.setClassName("stage-stopped");
                }
            } else {
                item = LabelFactory.createLabel(stage.getNameAfterDone(), "stage-active");
            }
        } else {
            item = LabelFactory.createLabel(stage.getName(), "stage-next");
        }
        if (stage.equals(Stage.TrainPolicy)) {
            Experiment experiment = dashboardItem.getExperiment();
            if (experiment != null && experiment.isHasGoals() && !experiment.isDraft()) {
                GoalsReachedStatus goalStatusComponent = new GoalsReachedStatus(experiment.isGoalsReached());
                goalStatusComponent.setSize("large");
                item.add(goalStatusComponent);
            }
        }
        return item;
    }

    private void updateProgress(PathmindTrainingProgress trainingProgress, DashboardItem item) {
        final double progress = ExperimentUtils.calculateProgressByIterationsProcessed(item.getIterationsProcessed());
        if (progress >= 0 && progress <= 100) {
            final double estimatedTime = ExperimentUtils.getEstimatedTrainingTime(item.getExperiment(), progress);
            trainingProgress.setValue(progress, estimatedTime);
        }
    }

}
