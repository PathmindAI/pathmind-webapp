package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.ExperimentUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.services.experiment.ExperimentRunService;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.PolicyStatusDetailsPanel;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentListPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyChartPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@UIScope
@StyleSheet("frontend://styles/styles.css")
@Route(value="project", layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static Logger log = LogManager.getLogger(ProjectView.class);

	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private ExperimentRepository experimentRepository;

	private long projectId;
	private Project project;
	private Experiment selectedExperiment;

	private ScreenTitlePanel screenTitlePanel;
	private ExperimentListPanel experimentListPanel;
	private PolicyChartPanel projectChartPanel;
	private PolicyStatusDetailsPanel experimentStatusDetailsPanel;

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
//			projectChartPanel.update(experiment);
//			experimentStatusDetailsPanel.update(experiment);
			NotificationUtils.showTodoNotification("Update run status panel");
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
			// TODO -> DATA MODEL
			Notification.show("TODO");
//			UI.getCurrent().navigate(
//					ExperimentView.class,
//					ExperimentUtils.generateFakeExperiment(project, experimentRepository));
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
		experimentListPanel = new ExperimentListPanel(consumer);
		projectChartPanel = new PolicyChartPanel(consumer);
		experimentStatusDetailsPanel = new PolicyStatusDetailsPanel();

		experimentListPanel.addSelectionListener(experiment -> {
			// TODO -> Highlight selected row in chart.
//			projectChartPanel.update(experiment);
			NotificationUtils.showTodoNotification();
//			experimentStatusDetailsPanel.update(experiment);
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
		this.project = projectDAO.getProject(projectId);

		if(project == null)
			throw new InvalidDataException("Attempted to access Project : " + projectId);

		// TODO -> implement all this in the repository.
//		List<Experiment> experiments = experimentRepository.getExperimentsForProject(project.getId());
//		project.setExperiments(experiments);

		// TODO -> DATA MODEL -> All has to be redone with the new data models.

//		project.setExperiments(new ArrayList<Experiment>());
//		loadProjectWithFakeExperimentData();
//
//		// TODO -> Fake. For now just select the first one. We should be doing grid.select and let the change propogate.
//		selectedExperiment = project.getExperiments().get(0);
//
//		screenTitlePanel.setSubtitle(project.getName());
//		experimentListPanel.update(project);
//		// https://vaadin.com/forum/thread/17527564/typeerror-cannot-read-property-dodeselector-of-undefined-vaadin-10
//		experimentListPanel.selectExperiment(selectedExperiment);
//
//		// TODO -> Remove once table selects the experiment since it should all be linked through events.
//		experimentStatusDetailsPanel.update(selectedExperiment);
//		projectChartPanel.update(project);
	}

		// TODO -> Quick solution to fake a lot of data for testing chart.
	private void loadProjectWithFakeExperimentData()
	{
		// TODO -> DATA MODEL
//		while(project.getExperiments().size() < 45)
//			project.getExperiments().add(FakeDataUtils.generateFakeExperiment(project));
//
//		project.getExperiments().stream()
//				.forEach(experiment -> FakeDataUtils.loadExperimentWithFakeData(experiment));
	}
}
