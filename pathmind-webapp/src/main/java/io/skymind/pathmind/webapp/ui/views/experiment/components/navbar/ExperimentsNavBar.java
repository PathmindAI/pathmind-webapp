package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.NavBarExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.NavBarExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.NotificationExperimentUpdatedSubscriber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavBar extends VerticalLayout
{
    private List<Experiment> experiments;
    private Experiment selectedExperiment;

    // REFACTOR -> Temporary placeholder until I finish the merging
    private NotificationExperimentUpdatedSubscriber notificationExperimentUpdatedSubscriber;

    private List<ExperimentsNavBarItem> experimentsNavBarItems = new ArrayList<>();
	private VerticalLayout rowsWrapper;
	private Consumer<Experiment> selectExperimentConsumer;
    private ExperimentsNavBarItem currentExperimentNavItem;
    private NewExperimentButton newExperimentButton;

    private SegmentIntegrator segmentIntegrator;

    public long modelId;

    private ExperimentDAO experimentDAO;
    private Supplier<Optional<UI>> getUISupplier;

    public ExperimentsNavBar(Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, Experiment selectedExperiment, List<Experiment> experiments, Consumer<Experiment> selectExperimentConsumer, SegmentIntegrator segmentIntegrator)
	{
 	    this.getUISupplier = getUISupplier;
	    this.experimentDAO = experimentDAO;
	    this.experiments = experiments;
	    this.selectedExperiment = selectedExperiment;
	    this.modelId = selectedExperiment.getModelId();
        this.selectExperimentConsumer = selectExperimentConsumer;
        this.segmentIntegrator = segmentIntegrator;

        notificationExperimentUpdatedSubscriber = new NotificationExperimentUpdatedSubscriber(getUISupplier, experiments, selectedExperiment);

        rowsWrapper = new VerticalLayout();
		rowsWrapper.addClassName("experiments-navbar-items");
		rowsWrapper.setPadding(false);
		rowsWrapper.setSpacing(false);
		
		newExperimentButton = new NewExperimentButton(experimentDAO, modelId);

		setPadding(false);
		setSpacing(false);
		add(newExperimentButton);
		add(rowsWrapper);
		addClassName("experiments-navbar");
        addExperimentsToNavBar();
	}

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(selectedExperiment.isArchived()) {
            EventBus.subscribe(this,
                    notificationExperimentUpdatedSubscriber);
        } else {
            EventBus.subscribe(this,
                    new NavBarExperimentUpdatedSubscriber(getUISupplier, this),
                    new NavBarExperimentCreatedSubscriber(getUISupplier, this),
                    notificationExperimentUpdatedSubscriber);
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

    private void addExperimentsToNavBar() {
		rowsWrapper.removeAll();
		experimentsNavBarItems.clear();
		
		experiments.stream()
			.forEach(experiment -> {
                ExperimentsNavBarItem navBarItem = createExperimentNavBarItem(experiment);
                experimentsNavBarItems.add(navBarItem);
				if(experiment.equals(selectedExperiment)) {
					navBarItem.setAsCurrent();
				}
				rowsWrapper.add(navBarItem);
		});
	}

    private ExperimentsNavBarItem createExperimentNavBarItem(Experiment experiment) {
        return new ExperimentsNavBarItem(getUISupplier, experimentDAO, experiment, selectExperimentConsumer, segmentIntegrator);
    }

    public void setCurrentExperiment(Experiment newCurrentExperiment) {
		experimentsNavBarItems.stream().forEach(experimentsNavBarItem -> {
            experimentsNavBarItem.removeAsCurrent();
			if (experimentsNavBarItem.getExperiment().equals(newCurrentExperiment)) {
				experimentsNavBarItem.setAsCurrent();
			}
		});
        notificationExperimentUpdatedSubscriber.setExperiment(newCurrentExperiment);
    }
    
    public void setAllowNewExperimentCreation(boolean allowNewExperimentCreation) {
        newExperimentButton.setEnabled(allowNewExperimentCreation);
    }
}
