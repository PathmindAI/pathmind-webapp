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
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.NavBarItemExperimentUpdatedSubscriber;
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

        addClickListener(event -> handleRowClicked(experiment, selectExperimentConsumer));

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
            ExperimentUtils.archiveExperiment(experimentDAO, experimentToArchive, true);
            segmentIntegrator.archived(Experiment.class, true);
        });
    }

    private void addNavBarTextAndButton(UI ui, ExperimentDAO experimentDAO, Experiment experiment) {
        VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
            add(createExperimentText(
                    experiment.getName(),
                    DateAndTimeUtils.formatDateAndTimeShortFormatter(experiment.getDateCreated(), timeZoneId),
                    new FavoriteStar(experiment.isFavorite(), newIsFavorite -> ExperimentUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite))));
            add(createArchiveExperimentButton(experiment));
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(experiment.isArchived())
            return;
        EventBus.subscribe(this, new NavBarItemExperimentUpdatedSubscriber(getUISupplier, this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    private Component createStatusIcon(RunStatus status) {
        if(ExperimentUtils.isDraftRunType(experiment))
            return new Icon(VaadinIcon.PENCIL);
        if (status.getValue() <= RunStatus.Running.getValue() || status == RunStatus.Restarting) {
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
        Div experimentNameWrapper = new Div();
        experimentNameWrapper.add(experimentNameLine);
        experimentNameWrapper.add(new Paragraph("Created " + experimentDateCreated));
        experimentNameWrapper.addClassName("experiment-name");
        return experimentNameWrapper;
    }

    public void updateStatus(RunStatus runStatus) {
        Component newStatusComponent = createStatusIcon(runStatus);
        replace(statusComponent, newStatusComponent);
        statusComponent = newStatusComponent;
    }

    private void handleRowClicked(Experiment experiment, Consumer<Experiment> selectExperimentConsumer) {
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
    }
}
