package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value="project", layout = MainLayout.class)
public class ProjectView extends VerticalLayout implements HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ProjectView.class);

	private ProjectRepository projectRepository;

	private ScreenTitlePanel screenTitlePanel = new ScreenTitlePanel("PROJECTS");

	public ProjectView(@Autowired ProjectRepository projectRepository)
	{
		this.projectRepository = projectRepository;

		add(new ActionMenu());
		add(screenTitlePanel);
	}

	// TODO -> Only allow navigation to this screen if there is a valid parameter
	// TODO -> Implement what happens if we don't find a project.
	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		projectRepository.findById(projectId)
				.ifPresentOrElse(
						project -> updateScreen(project),
						() -> log.info("TODO -> Implement"));
	}

	private void updateScreen(Project project) {
		screenTitlePanel.setSubtitle(project.getName());
		List<Experiment> experiments = project.getExperiments();
		experiments.stream().forEach(experiment -> log.info(experiment.getId()));
	}
}
