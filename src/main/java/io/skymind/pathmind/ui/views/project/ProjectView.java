package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
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
import io.skymind.pathmind.ui.views.BasicViewInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value="project", layout = MainLayout.class)
public class ProjectView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ProjectView.class);

	@Autowired
	private ProjectRepository projectRepository;

	private ScreenTitlePanel screenTitlePanel = new ScreenTitlePanel("PROJECTS");

	public ProjectView()
	{
		add(getActionMenu());
		add(getTitlePanel());
	}

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

	@Override
	public ActionMenu getActionMenu() {
		return new ActionMenu();
	}

	@Override
	public Component getTitlePanel() {
		return screenTitlePanel;
	}

	@Override
	public Component getMainContent() {
		return new Label("TODO -> To implement");
	}
}
