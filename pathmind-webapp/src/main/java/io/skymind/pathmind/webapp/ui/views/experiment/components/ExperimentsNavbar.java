package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@CssImport("./styles/views/experiment/experiment-navbar.css")
public class ExperimentsNavbar extends VerticalLayout implements RunUpdateSubscriber
{
	private static final String CURRENT = "current";

	private List<ExperimentsNavBarItem> experimentsNavBarItems = new ArrayList<>();
	private VerticalLayout rowsWrapper;
	private Consumer<Experiment> selectExperimentConsumer;
	private ExperimentsNavBarItem currentExperimentNavItem;

	public ExperimentsNavbar(ExperimentDAO experimentDAO, long modelId, Consumer<Experiment> selectExperimentConsumer)
	{
		this.selectExperimentConsumer = selectExperimentConsumer;
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

	public void setExperiments(UI ui, List<Experiment> experiments, Experiment currentExperiment) {
		rowsWrapper.removeAll();
		experimentsNavBarItems.clear();
		
		experiments.stream()
			.sorted(Comparator.comparing(Experiment::getDateCreated).reversed())
			.forEach(experiment -> {
				ExperimentsNavBarItem navBarItem = new ExperimentsNavBarItem(ui, experiment, selectExperimentConsumer);
				experimentsNavBarItems.add(navBarItem);
				if(experiment.equals(currentExperiment)) {
					navBarItem.setAsCurrent();
					currentExperimentNavItem = navBarItem;
				}
				rowsWrapper.add(navBarItem);
		});
	}

	public void setCurrentExperiment(Experiment newCurrentExperiment) {
		currentExperimentNavItem.removeAsCurrent();
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

	@Override
	public boolean isAttached() {
		return getUI().isPresent();
	}

	@Override
	public void handleBusEvent(RunUpdateBusEvent event) {
		PushUtils.push(this, () -> {
			// Look into each experiment to see if any run matches and if so update that experiment nav item.
			experimentsNavBarItems.stream()
					.forEach(experimentsNavBarItem -> {
						if(experimentsNavBarItem.getExperiment().getRuns().stream().anyMatch(run -> run.getId() == event.getRun().getId()))
							experimentsNavBarItem.updateStatus(event.getRun().getStatusEnum());
					});
		});
	}

	@Override
	public boolean filterBusEvent(RunUpdateBusEvent event) {
		// We are interested in any run update that's related to any experiment
		return experimentsNavBarItems.stream().anyMatch(
				experimentsNavBarItem -> experimentsNavBarItem.getExperiment().getRuns().stream().anyMatch(run -> run.getId() == event.getRun().getId()));
	}

	class ExperimentsNavBarItem extends HorizontalLayout
	{
		private Experiment experiment;
		private Component statusComponent;

		ExperimentsNavBarItem(UI ui, Experiment experiment, Consumer<Experiment> selectExperimentConsumer) {
			this.experiment = experiment;
			Boolean isDraft = ExperimentUtils.isDraftRunType(experiment);
			RunStatus overallExperimentStatus = ExperimentUtils.getTrainingStatus(experiment);
			statusComponent = isDraft ? new Icon(VaadinIcon.PENCIL) : createStatusIcon(overallExperimentStatus);
			add(statusComponent);
			addClickListener(event -> handleRowClicked(experiment, selectExperimentConsumer));
			addClassName("experiment-navbar-item");
			setSpacing(false);
			VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
				add(createExperimentText(experiment.getName(), DateAndTimeUtils.formatDateAndTimeShortFormatter(experiment.getDateCreated(), timeZoneId), isDraft));
			});
		}

		private Component createStatusIcon(RunStatus status) {
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
				experimentNameLine.add(LabelFactory.createLabel("Draft", "tag"));
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
	}
}
