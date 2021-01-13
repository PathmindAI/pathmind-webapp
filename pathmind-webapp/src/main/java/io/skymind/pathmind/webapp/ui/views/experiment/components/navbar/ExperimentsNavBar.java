package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarNotificationExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarNotificationExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.view.NavBarExperimentSelectedViewSubscriber;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavBar extends VerticalLayout {
    public long modelId;
    private List<Experiment> experiments;
    private Experiment selectedExperiment;
    private List<ExperimentsNavBarItem> experimentsNavBarItems = new ArrayList<>();
    private VerticalLayout rowsWrapper;
    private NewExperimentButton newExperimentButton;
    private SegmentIntegrator segmentIntegrator;
    private ExperimentDAO experimentDAO;
    private PolicyDAO policyDAO;
    private Supplier<Optional<UI>> getUISupplier;

    public ExperimentsNavBar(Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, PolicyDAO policyDAO, Experiment selectedExperiment, List<Experiment> experiments, SegmentIntegrator segmentIntegrator) {
        this.getUISupplier = getUISupplier;
        this.experimentDAO = experimentDAO;
        this.policyDAO = policyDAO;
        this.experiments = experiments;
        this.selectedExperiment = selectedExperiment;
        this.modelId = selectedExperiment.getModelId();
        this.segmentIntegrator = segmentIntegrator;

        rowsWrapper = new VerticalLayout();
        rowsWrapper.addClassName("experiments-navbar-items");
        rowsWrapper.setPadding(false);
        rowsWrapper.setSpacing(false);

        newExperimentButton = new NewExperimentButton(experimentDAO, modelId, segmentIntegrator);

        setPadding(false);
        setSpacing(false);
        add(newExperimentButton);
        add(rowsWrapper);
        addClassName("experiments-navbar");
        addExperimentsToNavBar();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (selectedExperiment.isArchived()) {
            EventBus.subscribe(this, getUISupplier,
                    new NavBarNotificationExperimentStartTrainingSubscriber(this),
                    new NavBarNotificationExperimentArchivedSubscriber(this));
        } else {
            EventBus.subscribe(this, getUISupplier,
                    new NavBarExperimentSelectedViewSubscriber(this),
                    new NavBarExperimentArchivedSubscriber(this),
                    new NavBarExperimentCreatedSubscriber(this),
                    new NavBarNotificationExperimentStartTrainingSubscriber(this),
                    new NavBarNotificationExperimentArchivedSubscriber(this));
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public long getModelId() {
        return modelId;
    }

    public void removeExperiment(Experiment experiment) {
        List<ExperimentsNavBarItem> toRemoveNavBarItems = experimentsNavBarItems.stream()
                .filter(experimentsNavBarItem -> ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), experiment))
                .collect(Collectors.toList());
        experimentsNavBarItems.removeAll(toRemoveNavBarItems);
        toRemoveNavBarItems.forEach(navBarItem -> rowsWrapper.remove(navBarItem));
    }

    public void addExperiment(Experiment experiment) {
        experiments.add(experiment);
        experiments.sort(Comparator.comparing(Experiment::getDateCreated, Comparator.reverseOrder()));
        ExperimentsNavBarItem navBarItem = createExperimentNavBarItem(experiment);
        experimentsNavBarItems.add(navBarItem);
        experimentsNavBarItems.sort(Comparator.comparing(experimentsNavBarItem -> experimentsNavBarItem.getExperiment().getDateCreated(), Comparator.reverseOrder()));
        // Remove and re-add the navbar items so that they are in sorted order.
        rowsWrapper.removeAll();
        experimentsNavBarItems.forEach(experimentsNavBarItem ->
                rowsWrapper.add(experimentsNavBarItem));
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public Experiment getSelectedExperiment() {
        return selectedExperiment;
    }

    private void addExperimentsToNavBar() {
        rowsWrapper.removeAll();
        experimentsNavBarItems.clear();

        experiments.stream()
                .forEach(experiment -> {
                    ExperimentsNavBarItem navBarItem = createExperimentNavBarItem(experiment);
                    experimentsNavBarItems.add(navBarItem);
                    if (experiment.equals(selectedExperiment)) {
                        navBarItem.setAsCurrent();
                    }
                    rowsWrapper.add(navBarItem);
                });
    }

    private ExperimentsNavBarItem createExperimentNavBarItem(Experiment experiment) {
        return new ExperimentsNavBarItem(this, getUISupplier, experimentDAO, policyDAO, experiment, segmentIntegrator);
    }

    public void setCurrentExperiment(Experiment newCurrentExperiment) {
        experimentsNavBarItems.stream().forEach(experimentsNavBarItem -> {
            experimentsNavBarItem.removeAsCurrent();
            if (experimentsNavBarItem.getExperiment().equals(newCurrentExperiment)) {
                experimentsNavBarItem.setAsCurrent();
            }
        });
        selectedExperiment = newCurrentExperiment;
    }

    public void setAllowNewExperimentCreation(boolean allowNewExperimentCreation) {
        newExperimentButton.setEnabled(allowNewExperimentCreation);
    }
}