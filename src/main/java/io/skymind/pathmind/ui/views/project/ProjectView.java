package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.UIScope;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.data.ExperimentUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.services.experiment.ExperimentRunService;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentStatusDetailsPanel;
import io.skymind.pathmind.ui.views.project.components.ExperimentListPanel;
import io.skymind.pathmind.ui.views.project.components.ProjectChartPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UIScope
@StyleSheet("frontend://styles/styles.css")
@Route(value="project", layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static Logger log = LogManager.getLogger(ProjectView.class);

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ExperimentRepository experimentRepository;

	private long projectId;
	private Project project;
	private Experiment selectedExperiment;

	private ScreenTitlePanel screenTitlePanel;
	private ExperimentListPanel experimentListPanel;
	private ProjectChartPanel projectChartPanel;
	private ExperimentStatusDetailsPanel experimentStatusDetailsPanel;

	private final UnicastProcessor<PathmindBusEvent> publisher;
	private final Flux<PathmindBusEvent> consumer;

	public ProjectView(UnicastProcessor<PathmindBusEvent> publisher, Flux<PathmindBusEvent> consumer)
	{
		super();
		this.publisher = publisher;
		this.consumer = consumer;
	}

	@Override
	protected void subscribeToEventBus() {
		consumer.filter(busEvent -> busEvent.getEventType().equals(BusEventType.ExperimentUpdate))
				.filter(busEvent -> (busEvent.getEventDataId() == selectedExperiment.getId()))
				.subscribe(busEvent ->
						pushValues(((ExperimentUpdateBusEvent)busEvent).getExperiment()));
	}

	private void pushValues(Experiment experiment) {
		PushUtils.push(this, () -> {
			projectChartPanel.update(experiment);
			experimentStatusDetailsPanel.update(experiment);
		});
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				getAddExperimentButton(),
				getFullRunButton());
	}

	private Button getFullRunButton() {
		return new Button("Full Run >", click -> {
			ExperimentRunService.fullRun(selectedExperiment, publisher);
		});
	}

	// TODO -> Exception handling with database.
	private Button getAddExperimentButton() {
		return new Button("+ Add Experiment", click -> {
			UI.getCurrent().navigate(
					ExperimentView.class,
					ExperimentUtils.generateFakeExperiment(project, experimentRepository));
		});
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	protected Component getTitlePanel() {
		screenTitlePanel = new ScreenTitlePanel("PROJECT");
		return screenTitlePanel;
	}

	// TODO -> Since I'm not sure exactly what the panels on the right are I'm going to make some big
	// assumptions as to which Layout should wrap which one.
	@Override
	protected Component getMainContent()
	{
		experimentListPanel = new ExperimentListPanel();
		projectChartPanel = new ProjectChartPanel();
		experimentStatusDetailsPanel = new ExperimentStatusDetailsPanel();

		experimentListPanel.addSelectionListener(experiment -> {
			projectChartPanel.update(experiment);
			experimentStatusDetailsPanel.update(experiment);
			selectedExperiment = experiment;
		});

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
					projectChartPanel,
					experimentStatusDetailsPanel),
				experimentListPanel
		);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		this.project = projectRepository.getProject(projectId);

		if(project == null)
			throw new InvalidDataException("Attempted to access Project : " + projectId);

		List<Experiment> experiments = experimentRepository.getExperimentsForProject(project.getId());
		// TODO -> implement all this in the repository.
		project.setExperiments(experiments);

		// TODO -> remove
		selectedExperiment = project.getExperiments().get(0);
		project.getExperiments().get(0).setStartTime(Instant.now().minusSeconds(600));

		screenTitlePanel.setSubtitle(project.getName());
		experimentListPanel.setExperiments(experiments);
		// https://vaadin.com/forum/thread/17527564/typeerror-cannot-read-property-dodeselector-of-undefined-vaadin-10
		experimentListPanel.selectExperiment(selectedExperiment);

		// TODO -> Remove once table selects the experiment since it should all be linked through events.
		experimentStatusDetailsPanel.update(selectedExperiment);
		projectChartPanel.update(selectedExperiment);

		// TODO -> to implement
		ArrayList<Number> fakeScores = new ArrayList<>();
		Arrays.asList(10, 40, 60, 20, 40, 50, 50, 10, 100, 80).stream()
				.forEach(score -> selectedExperiment.getScores().add(score));
	}
}
