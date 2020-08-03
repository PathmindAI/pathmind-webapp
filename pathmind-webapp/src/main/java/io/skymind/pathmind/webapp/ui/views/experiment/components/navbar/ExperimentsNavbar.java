package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavbar extends VerticalLayout
{
	private List<ExperimentsNavBarItem> experimentsNavBarItems = new ArrayList<>();
	private VerticalLayout rowsWrapper;
	private Consumer<Experiment> selectExperimentConsumer;
	private Consumer<Experiment> archiveExperimentHandler;
    private ExperimentsNavBarItem currentExperimentNavItem;

    private long modelId;

    public ExperimentsNavbar(ExperimentDAO experimentDAO, Experiment experiment, Consumer<Experiment> selectExperimentConsumer, Consumer<Experiment> archiveExperimentHandler)
	{
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


    /**
     * Helper method because in some cases we have the UI for sure.
     */
    public void setExperiments(UI ui, List<Experiment> experiments, Experiment currentExperiment) {
        setExperiments(VaadinUtils.convertUiToOptionalSupplier(ui), experiments, currentExperiment);
    }

    public void setExperiments(Supplier<Optional<UI>> getUISupplier, List<Experiment> experiments, Experiment currentExperiment) {
		rowsWrapper.removeAll();
		experimentsNavBarItems.clear();
		
		experiments.stream()
			.forEach(experiment -> {
				ExperimentsNavBarItem navBarItem = new ExperimentsNavBarItem(this, getUISupplier, experiment, selectExperimentConsumer, archiveExperimentHandler);
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

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		EventBus.subscribe(this);
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		EventBus.unsubscribe(this);
	}
}
