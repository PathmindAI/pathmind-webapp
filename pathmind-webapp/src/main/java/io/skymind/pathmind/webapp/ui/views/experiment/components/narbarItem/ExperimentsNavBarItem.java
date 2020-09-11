package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.atoms.GoalsReachedStatus;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.NavBarItemExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.NavBarItemRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExperimentsNavBarItem extends HorizontalLayout {

    private static final String CURRENT = "current";

    private ExperimentsNavBar experimentsNavbar;
    private Supplier<Optional<UI>> getUISupplier;
    private ExperimentDAO experimentDAO;

    private Experiment experiment;
    private Component statusComponent;
    private FavoriteStar favoriteStar;
    private Div experimentNameWrapper;
    private GoalsReachedStatus goalStatusComponent;

    private SegmentIntegrator segmentIntegrator;

    public ExperimentsNavBarItem(ExperimentsNavBar experimentsNavbar, Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, Experiment experiment, Consumer<Experiment> selectExperimentConsumer, SegmentIntegrator segmentIntegrator) {
        this.experimentsNavbar = experimentsNavbar;
        this.getUISupplier = getUISupplier;
        this.experimentDAO = experimentDAO;
        this.experiment = experiment;
        this.segmentIntegrator = segmentIntegrator;

        Boolean isDraft = ExperimentUtils.isDraftRunType(experiment);
        RunStatus overallExperimentStatus = ExperimentUtils.getTrainingStatus(experiment);
        statusComponent = isDraft ? new Icon(VaadinIcon.PENCIL) : createStatusIcon(overallExperimentStatus);
        add(statusComponent);

        addClickListener(event -> handleRowClicked(selectExperimentConsumer));

        addClassName("experiment-navbar-item");
        setSpacing(false);

        UI.getCurrent().getUI().ifPresent(ui ->
                addNavBarTextAndButton(ui, experimentDAO, experiment));
    }

    private Button createArchiveExperimentButton(Experiment experiment) {
        Button archiveExperimentButton = new Button(VaadinIcon.ARCHIVE.create());
        archiveExperimentButton.getElement().addEventListener("click",
                click -> archiveExperiment(experiment)).addEventData("event.stopPropagation()");
        archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        archiveExperimentButton.addClassName("action-button");
        return archiveExperimentButton;
    }

    private void archiveExperiment(Experiment experimentToArchive) {
        ConfirmationUtils.archive("Experiment #"+experimentToArchive.getName(), () -> {
            segmentIntegrator.archived(Experiment.class, true);
            ExperimentUtils.archiveExperiment(experimentDAO, experimentToArchive, true);
            ExperimentUtils.navigateToFirstUnarchivedOrModel(getUISupplier, experimentsNavbar.getExperiments());
        });
    }

    private void addNavBarTextAndButton(UI ui, ExperimentDAO experimentDAO, Experiment experiment) {
        favoriteStar = new FavoriteStar(experiment.isFavorite(), newIsFavorite -> ExperimentUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite));
        VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
            add(createExperimentText(
                    experiment.getName(),
                    DateAndTimeUtils.formatDateAndTimeShortFormatter(experiment.getDateCreated(), timeZoneId),
                    favoriteStar));
            add(createArchiveExperimentButton(experiment));
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(experiment.isArchived())
            return;
        EventBus.subscribe(this,
                new NavBarItemExperimentUpdatedSubscriber(getUISupplier, this),
                new NavBarItemRunUpdateSubscriber(getUISupplier, this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    private Component createStatusIcon(RunStatus status) {
        if(ExperimentUtils.isDraftRunType(experiment))
            return new Icon(VaadinIcon.PENCIL);
        if (RunStatus.isRunning(status)) {
            Div loadingSpinner = new Div();
            loadingSpinner.addClassName("icon-loading-spinner");
            return loadingSpinner;
        } else if (status == RunStatus.Completed) {
            return new Icon(VaadinIcon.COMMENTS.CHECK_CIRCLE);
        } else if (status == RunStatus.Killed || status == RunStatus.Stopping) {
            Div stoppedIcon = new Div();
            stoppedIcon.addClassName("icon-stopped");
            return stoppedIcon;
        }
        return new Icon(VaadinIcon.EXCLAMATION_CIRCLE_O);
    }

    private Div createExperimentText(String experimentNumber, String experimentDateCreated, FavoriteStar favoriteStar) {
        Paragraph experimentNameLine = new Paragraph("Experiment #" + experimentNumber);

        experimentNameLine.add(favoriteStar);
        experimentNameWrapper = new Div();
        experimentNameWrapper.add(experimentNameLine);
        experimentNameWrapper.add(new Paragraph("Created " + experimentDateCreated));
        updateGoalStatus(experiment.isGoalsReached());
        experimentNameWrapper.addClassName("experiment-name");
        return experimentNameWrapper;
    }
    private void updateStatus(RunStatus runStatus) {
        Component newStatusComponent = createStatusIcon(runStatus);
        replace(statusComponent, newStatusComponent);
        statusComponent = newStatusComponent;
    }

    // Part of this will need to be moved to the javascript part of the refactored Nav Bar Item
    private void updateGoalStatus(Boolean goalStatus) {
        if (goalStatusComponent != null) {
            experimentNameWrapper.remove(goalStatusComponent);
            goalStatusComponent = null;
        }
        if (goalStatus != null) {
            goalStatusComponent = new GoalsReachedStatus(goalStatus);
            experimentNameWrapper.add(goalStatusComponent);
        }
        Boolean trainingNotCompleted = ExperimentUtils.getTrainingStatus(experiment).getValue() < RunStatus.Completed.getValue();
        goalStatusComponent.setVisible(!trainingNotCompleted);
    }

    private void handleRowClicked(Consumer<Experiment> selectExperimentConsumer) {
        selectExperimentConsumer.accept(experiment);
        experimentsNavbar.setCurrentExperiment(experiment);
    }

    public void setAsCurrent() {
        addClassName(CURRENT);
    }

    public void removeAsCurrent() {
        removeClassName(CURRENT);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void updateExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateStatus(ExperimentUtils.getTrainingStatus(experiment));
        updateGoalStatus(experiment.isGoalsReached());
        favoriteStar.setValue(experiment.isFavorite());
    }

    public void updateRun(Run run) {
        experiment.updateRun(run);
        updateStatus(ExperimentUtils.getTrainingStatus(experiment));
        updateGoalStatus(run.getExperiment().isGoalsReached());
    }
}
