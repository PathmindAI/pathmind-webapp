package io.skymind.pathmind.ui.views.project;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.ProjectUtils;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.FormUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.components.panels.NewProjectLogoWizardPanel;
import io.skymind.pathmind.ui.views.project.components.wizard.CreateANewProjectWizardPanel;

@CssImport("./styles/styles.css")
@Route(value = Routes.NEW_PROJECT, layout = MainLayout.class)
public class NewProjectView extends PathMindDefaultView 
{

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private Project project;

	private Binder<Project> projectBinder;

	private NewProjectLogoWizardPanel logoPanel;
	private CreateANewProjectWizardPanel createProjectPanel;

	public NewProjectView()
	{
		super();
	}

	protected Component getMainContent()
	{
		this.project = ProjectUtils.generateNewDefaultProject();
		projectBinder = new Binder<>(Project.class);

		logoPanel = new NewProjectLogoWizardPanel();
		createProjectPanel = new CreateANewProjectWizardPanel(projectBinder, projectDAO);

		// This is only used in case we setup MockDefaultValues through ProjectUtils above.
		projectBinder.readBean(project);

		createProjectPanel.addButtonClickListener(click -> handleNewProjectClicked());

		return WrapperUtils.wrapFormCenterVertical(
				logoPanel,
				createProjectPanel);
	}

	private void handleNewProjectClicked()
	{
		if(!FormUtils.isValidForm(projectBinder, project))
			return;
		
		final long projectId = projectDAO.createNewProject(project);
		segmentIntegrator.projectCreated();
		UI.getCurrent().navigate(ModelsView.class, projectId);
	}

	/**
	 * The titles are in the individual panels as the title changes based on the state.
	 */
	protected VerticalLayout getTitlePanel() {
		return null;
	}
	
	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

}
