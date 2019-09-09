package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.bus.data.ProjectUpdateStatus;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.services.project.ProjectRunService;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentScoreboardPanel;
import io.skymind.pathmind.ui.views.project.components.ExperimentListPanel;
import io.skymind.pathmind.ui.views.project.components.ProjectChartPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.Arrays;

@StyleSheet("frontend://styles/styles.css")
@Route(value="project", layout = MainLayout.class)
public class ProjectView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static Logger log = LogManager.getLogger(ProjectView.class);

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ExperimentRepository experimentRepository;

	private Project project;
	private long projectId;

	private ScreenTitlePanel screenTitlePanel;

	private ExperimentListPanel experimentPanel;
	private ProjectChartPanel projectChartPanel;

	private final UnicastProcessor<ProjectUpdateStatus> publisher;
	private final Flux<ProjectUpdateStatus> consumer;

	public ProjectView(UnicastProcessor<ProjectUpdateStatus> publisher, Flux<ProjectUpdateStatus> consumer)
	{
		super();

		this.publisher = publisher;
		this.consumer = consumer;

		subscribeToEventBus(consumer);
	}

	private void subscribeToEventBus(Flux<ProjectUpdateStatus> consumer) {
		consumer.filter(projectUpdateStatus -> projectId == projectUpdateStatus.getProjectId())
				.subscribe(projectUpdateStatus ->
						PushUtils.push(this, () -> {
							projectChartPanel.addChartData(Integer.valueOf(projectUpdateStatus.getScoreValue()));
						}));
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				getAddExperimentButton(),
				getFullRunButton()
		);
	}

	private Button getFullRunButton() {
		return new Button("Full Run >", click -> {
			ProjectRunService.fullRun(projectId, publisher);
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
		experimentPanel = new ExperimentListPanel();
		projectChartPanel = new ProjectChartPanel();

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
					projectChartPanel,
					new ExperimentScoreboardPanel()),
				experimentPanel
		);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}

	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		this.project = projectRepository.getProject(projectId);

		if(project == null)
			throw new InvalidDataException("Attempted to access Project : " + projectId);

		screenTitlePanel.setSubtitle(project.getName());
		experimentPanel.setExperiments(experimentRepository.getExperimentsForProject(project.getId()));

		// TODO -> to implement
		projectChartPanel.setChartData(Arrays.asList(10, 40, 60, 20, 40, 50, 50, 10, 100, 80));
	}
}
