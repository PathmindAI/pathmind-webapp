package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentChartPanel;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentRecentPanel;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentScoreboardPanel;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="project", layout = MainLayout.class)
public class ProjectView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ProjectView.class);

	// TODO -> Implement correctly based on parameters passed into the view.
	private ProjectRepository projectRepository;

	private ScreenTitlePanel screenTitlePanel = new ScreenTitlePanel("PROJECT");
	private ExperimentRecentPanel experimentRecentPanel = new ExperimentRecentPanel();

	public ProjectView(@Autowired ProjectRepository projectRepository)
	{
		this.projectRepository = projectRepository;

		add(getActionMenu());
		add(getTitlePanel());
		add(getMainContent());

		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	@Override
	public ActionMenu getActionMenu() {
		return new ActionMenu(
			new Button("Stop")
		);
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	public Component getTitlePanel() {
		return screenTitlePanel;
	}

	// TODO -> Since I'm not sure exactly what the panels on the right are I'm going to make some big
	// assumptions as to which Layout should wrap which one.
	// TODO -> Hardcoded data.
	@Override
	public Component getMainContent() {
		return WrapperUtils.wrapCenterAlignmentFullVertical(
				WrapperUtils.wrapCenterAlignmentFullWidthHorizontal(
					new ExperimentChartPanel(),
					new ExperimentScoreboardPanel()),
				experimentRecentPanel
		);
	}

	// TODO -> Fake data
	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		// TODO -> Implement as I transition the code to JOOQ.
//		projectRepository.findById(1L).get().getExperiments();
//		projectRepository.findById(projectId)
//				.ifPresentOrElse(
//						project -> updateScreen(project),
//						() -> log.info("TODO -> Implement"));
	}

	private void updateScreen(Project project) {
		screenTitlePanel.setSubtitle(project.getName());
		experimentRecentPanel.setExperiments(project.getExperiments());
	}
}
