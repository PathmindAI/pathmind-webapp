package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarNotificationExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main.NavBarNotificationExperimentStartTrainingSubscriber;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavBar extends VerticalLayout {
    private List<Experiment> experiments;
    private Experiment selectedExperiment;

    private List<ExperimentsNavBarItem> experimentsNavBarItems = new ArrayList<>();
    private VerticalLayout rowsWrapper;
    private NewExperimentButton newExperimentButton;

    public long modelId;

    private ExperimentDAO experimentDAO;

    private AbstractExperimentView abstractExperimentView;

    public ExperimentsNavBar(AbstractExperimentView abstractExperimentView, ExperimentDAO experimentDAO) {
        this.abstractExperimentView = abstractExperimentView;
        this.experimentDAO = experimentDAO;
        this.selectedExperiment = abstractExperimentView.getExperiment();
        this.modelId = selectedExperiment.getModelId();

        rowsWrapper = new VerticalLayout();
        rowsWrapper.addClassName("experiments-navbar-items");
        rowsWrapper.setPadding(false);
        rowsWrapper.setSpacing(false);

        newExperimentButton = new NewExperimentButton(experimentDAO, modelId, abstractExperimentView.getSegmentIntegrator());

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
            EventBus.subscribe(this, abstractExperimentView.getUISupplier(),
                    new NavBarNotificationExperimentStartTrainingSubscriber(this),
                    new NavBarNotificationExperimentArchivedSubscriber(this));
        } else {
            EventBus.subscribe(this, abstractExperimentView.getUISupplier(),
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

    public void updateExperiment(Experiment experiment) {
        if (experiment.isArchived()) {
            removeExperiment(experiment);
        } else {
            addExperiment(experiment);
        }
    }

    public void removeExperiment(Experiment experiment) {
        experimentsNavBarItems.stream()
                .filter(experimentsNavBarItem -> ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), experiment))
                .findFirst()
                .ifPresent(exp -> {
                    experimentsNavBarItems.remove(exp);
                    rowsWrapper.remove(exp);
                });
    }

    public void addExperiment(Experiment experiment) {
        experiments.add(experiment);
        experiments.sort(Comparator.comparing(Experiment::getDateCreated, Comparator.reverseOrder()));
        ExperimentsNavBarItem navBarItem = createExperimentNavBarItem(experiment);
        experimentsNavBarItems.add(navBarItem);
        experimentsNavBarItems.sort(Comparator.comparing(experimentsNavBarItem -> experimentsNavBarItem.getExperiment().getDateCreated(), Comparator.reverseOrder()));
        // Remove and re-add the navbar items so that they are in sorted order.
        rowsWrapper.removeAll();
        experimentsNavBarItems.forEach(experimentsNavBarItem -> {
            setIsOnDraftExperimentView(experimentsNavBarItem, selectedExperiment);
            rowsWrapper.add(experimentsNavBarItem);
        });
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiment(Experiment experiment) {
        experiments
                .stream()
                .filter(exp -> ExperimentUtils.isSameExperiment(exp, experiment))
                .findFirst()
                .ifPresent(exp -> experiments.set(experiments.indexOf(exp), experiment));
    }

    public List<ExperimentsNavBarItem> getExperimentsNavBarItems() {
        return experimentsNavBarItems;
    }

    public Experiment getSelectedExperiment() {
        return selectedExperiment;
    }

    private void addExperimentsToNavBar() {
        rowsWrapper.removeAll();
        experimentsNavBarItems.clear();

        experiments = experimentDAO.getExperimentsForModel(modelId).stream()
                .filter(exp -> !exp.isArchived()).collect(Collectors.toList());

        experiments.stream()
                .forEach(experiment -> {
                    ExperimentsNavBarItem navBarItem = createExperimentNavBarItem(experiment);
                    setIsOnDraftExperimentView(navBarItem, selectedExperiment);
                    experimentsNavBarItems.add(navBarItem);
                    if (experiment.equals(selectedExperiment)) {
                        navBarItem.setAsCurrent();
                    }
                    rowsWrapper.add(navBarItem);
                });
    }

    private ExperimentsNavBarItem createExperimentNavBarItem(Experiment experiment) {
        return new ExperimentsNavBarItem(this, abstractExperimentView, experimentDAO, experiment);
    }

    private void setIsOnDraftExperimentView(ExperimentsNavBarItem targetNavBarItem, Experiment experiment) {
        targetNavBarItem.setIsOnDraftExperimentView(experiment.isDraft());
    }

    public void setCurrentExperiment(Experiment newCurrentExperiment) {
        selectedExperiment = newCurrentExperiment;
        setVisible(!newCurrentExperiment.isArchived());

        // There's no need processing any further if archived as the navbar is not visible.
        if(newCurrentExperiment.isArchived()) {
            return;
        }

        setAllowNewExperimentCreation(ModelUtils.isValidModel(newCurrentExperiment.getModel()));

        experimentsNavBarItems.stream().forEach(experimentsNavBarItem -> {
            setIsOnDraftExperimentView(experimentsNavBarItem, newCurrentExperiment);
            experimentsNavBarItem.removeAsCurrent();
            if (experimentsNavBarItem.getExperiment().equals(newCurrentExperiment)) {
                experimentsNavBarItem.setAsCurrent();
            }
        });
    }

    public void setAllowNewExperimentCreation(boolean allowNewExperimentCreation) {
        newExperimentButton.setEnabled(allowNewExperimentCreation);
    }
}