package io.skymind.pathmind.webapp.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.webapp.data.utils.ProjectUtils;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.project.components.panels.CreateANewProjectPanel;
import io.skymind.pathmind.webapp.ui.views.project.components.panels.NewProjectLogoWizardPanel;
import org.springframework.beans.factory.annotation.Autowired;

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
	private CreateANewProjectPanel createProjectPanel;

	public NewProjectView()
	{
		super();
	}

	protected Component getMainContent()
	{
		this.project = ProjectUtils.generateNewDefaultProject();
		projectBinder = new Binder<>(Project.class);

		logoPanel = new NewProjectLogoWizardPanel();
		createProjectPanel = new CreateANewProjectPanel(projectBinder, projectDAO);

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
		UI.getCurrent().navigate(UploadModelView.class, ""+projectId);
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
