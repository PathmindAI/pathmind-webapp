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
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.NavBarExperimentUpdatedSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavBar extends VerticalLayout
{
	private List<ExperimentsNavBarItem> experimentsNavBarItems = new ArrayList<>();
	private VerticalLayout rowsWrapper;
	private Consumer<Experiment> selectExperimentConsumer;
	private Consumer<Experiment> archiveExperimentHandler;
    private ExperimentsNavBarItem currentExperimentNavItem;

    public long modelId;

    private ExperimentDAO experimentDAO;
    private Supplier<Optional<UI>> getUISupplier;

    public ExperimentsNavBar(Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, Experiment experiment, Consumer<Experiment> selectExperimentConsumer, Consumer<Experiment> archiveExperimentHandler)
	{
	    this.getUISupplier = getUISupplier;
	    this.experimentDAO = experimentDAO;
	    this.modelId = experiment.getModelId();
        this.selectExperimentConsumer = selectExperimentConsumer;
        this.archiveExperimentHandler = archiveExperimentHandler;
		rowsWrapper = new VerticalLayout();
		rowsWrapper.addClassName("experiments-navbar-items");
		rowsWrapper.setPadding(false);
		rowsWrapper.setSpacing(false);

		setPadding(false);
		setSpacing(false);
		add(new NewExperimentButton(experimentDAO, modelId));
		add(rowsWrapper);
		addClassName("experiments-navbar");
	}

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this, new NavBarExperimentUpdatedSubscriber(getUISupplier, this));
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

    public void setExperiments(Supplier<Optional<UI>> getUISupplier, List<Experiment> experiments, Experiment currentExperiment) {
		rowsWrapper.removeAll();
		experimentsNavBarItems.clear();
		
		experiments.stream()
			.forEach(experiment -> {
				ExperimentsNavBarItem navBarItem = new ExperimentsNavBarItem(this, getUISupplier, experimentDAO, experiment, selectExperimentConsumer, archiveExperimentHandler);
				experimentsNavBarItems.add(navBarItem);
				if(experiment.equals(currentExperiment)) {
					navBarItem.setAsCurrent();
					currentExperimentNavItem = navBarItem;
				}
				rowsWrapper.add(navBarItem);
		});
	}

	public void setCurrentExperiment(Experiment newCurrentExperiment) {
	    if (currentExperimentNavItem != null) {
	        currentExperimentNavItem.removeAsCurrent();
	    }
		experimentsNavBarItems.stream().forEach(experimentsNavBarItem -> {
			if (experimentsNavBarItem.getExperiment().equals(newCurrentExperiment)) {
				experimentsNavBarItem.setAsCurrent();
				currentExperimentNavItem = experimentsNavBarItem;
			}
		});
    }
}
