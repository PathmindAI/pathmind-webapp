package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar;

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
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.NavBarItemExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.TAG_LABEL;

public class ExperimentsNavBarItem extends HorizontalLayout {

    private static final String CURRENT = "current";

    private NavBarItemExperimentUpdatedSubscriber navBarItemExperimentUpdatedSubscriber;
    private ExperimentsNavbar experimentsNavbar;

    private Experiment experiment;
    private Component statusComponent;

    ExperimentsNavBarItem(ExperimentsNavbar experimentsNavbar, Supplier<Optional<UI>> getUISupplier, Experiment experiment, Consumer<Experiment> selectExperimentConsumer, Consumer<Experiment> archiveExperimentHandler) {
        this.experimentsNavbar = experimentsNavbar;
        this.experiment = experiment;
        Boolean isDraft = ExperimentUtils.isDraftRunType(experiment);
        RunStatus overallExperimentStatus = ExperimentUtils.getTrainingStatus(experiment);
        statusComponent = isDraft ? new Icon(VaadinIcon.PENCIL) : createStatusIcon(overallExperimentStatus);
        add(statusComponent);
        addClickListener(event -> handleRowClicked(experiment, selectExperimentConsumer));
        addClassName("experiment-navbar-item");
        setSpacing(false);
        Button archiveExperimentButton = new Button(VaadinIcon.ARCHIVE.create());
        archiveExperimentButton.getElement().addEventListener("click", click -> archiveExperimentHandler.accept(experiment)).addEventData("event.stopPropagation()");
        archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        archiveExperimentButton.addClassName("action-button");
        VaadinDateAndTimeUtils.withUserTimeZoneId(getUISupplier.get().get(), timeZoneId -> {
            add(createExperimentText(experiment.getName(), DateAndTimeUtils.formatDateAndTimeShortFormatter(experiment.getDateCreated(), timeZoneId), isDraft));
            add(archiveExperimentButton);
        });

        // Add eventbus listeners
        navBarItemExperimentUpdatedSubscriber = new NavBarItemExperimentUpdatedSubscriber(getUISupplier, this);
        EventBus.subscribe(this, navBarItemExperimentUpdatedSubscriber);
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

    private Div createExperimentText(String experimentNumber, String experimentDateCreated, Boolean isDraft) {
        Paragraph experimentNameLine = new Paragraph("Experiment #" + experimentNumber);
        if (isDraft) {
            experimentNameLine.add(LabelFactory.createLabel("Draft", TAG_LABEL));
        }

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
