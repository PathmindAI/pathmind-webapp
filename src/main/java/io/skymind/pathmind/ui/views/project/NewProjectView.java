package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.ModelUtils;
import io.skymind.pathmind.data.utils.ProjectUtils;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.project.FileCheckResult;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;
import io.skymind.pathmind.ui.utils.FormUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.ui.views.project.components.panels.NewProjectLogoWizardPanel;
import io.skymind.pathmind.ui.views.project.components.wizard.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "newProject", layout = MainLayout.class)
public class NewProjectView extends PathMindDefaultView implements StatusUpdater
{
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private ProjectFileCheckService projectFileCheckService ;

	private Project project;
	private Model model;

	private Binder<Project> projectBinder;
	private Binder<Model> modelBinder;

	private UI ui;

	private NewProjectLogoWizardPanel logoPanel;
	private NewProjectStatusWizardPanel statusPanel;
	private CreateANewProjectWizardPanel createProjectPanel;
	private PathminderHelperWizardPanel pathminderHelperWizardPanel;
	private UploadModelWizardPanel uploadModelWizardPanel;
	private ModelDetailsWizardPanel modelDetailsWizardPanel;

	private List<Component> wizardPanels;

	public NewProjectView()
	{
		super();
		this.ui = UI.getCurrent();
	}

	protected Component getMainContent()
	{
		this.project = ProjectUtils.generateNewDefaultProject();
		this.model = ModelUtils.generateNewDefaultModel();

		projectBinder = new Binder<>(Project.class);
		modelBinder = new Binder<>(Model.class);

		logoPanel = new NewProjectLogoWizardPanel();
		statusPanel = new NewProjectStatusWizardPanel();
		createProjectPanel = new CreateANewProjectWizardPanel(projectBinder);
		pathminderHelperWizardPanel = new PathminderHelperWizardPanel();
		uploadModelWizardPanel = new UploadModelWizardPanel(model);
		modelDetailsWizardPanel = new ModelDetailsWizardPanel(modelBinder);

		wizardPanels = Arrays.asList(
				createProjectPanel,
				pathminderHelperWizardPanel,
				uploadModelWizardPanel,
				modelDetailsWizardPanel);

		setVisibleWizardPanel(createProjectPanel);

		createProjectPanel.addButtonClickListener(click -> handleNewProjectClicked());
		pathminderHelperWizardPanel.addButtonClickListener(click -> handleNextStepClicked());
		uploadModelWizardPanel.addButtonClickListener(click -> handleUploadWizardClicked());
		modelDetailsWizardPanel.addButtonClickListener(click -> handleMoreDetailsClicked());

		return WrapperUtils.wrapFormCenterVertical(
				logoPanel,
				statusPanel,
				createProjectPanel,
				pathminderHelperWizardPanel,
				uploadModelWizardPanel,
				modelDetailsWizardPanel);
	}

	private void handleMoreDetailsClicked()
	{
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			// Project has already passed validations in a previous panel of the wizard.
			if(!FormUtils.isValidForm(modelBinder, model))
				return;

			final long experimentId = projectDAO.setupNewProject(project, model);

			UI.getCurrent().navigate(NewExperimentView.class, experimentId);
		});
	}

	private void handleUploadWizardClicked()  {
		uploadModelWizardPanel.showFileCheckPanel();
		projectFileCheckService.checkFile(this, model.getFile());
	}

	private void handleNextStepClicked() {
		setVisibleWizardPanel(uploadModelWizardPanel);
		statusPanel.setUploadModel();
	}

	private void handleNewProjectClicked()
	{
		if(!FormUtils.isValidForm(projectBinder, project))
			return;

		pathminderHelperWizardPanel.setProjectName(project.getName());
		uploadModelWizardPanel.setProjectName(project.getName());
		setVisibleWizardPanel(pathminderHelperWizardPanel);
		statusPanel.setPathmindHelper();
	}

	private void setVisibleWizardPanel(Component wizardPanel) {
		wizardPanels.stream()
				.forEach(panel -> panel.setVisible(panel.equals(wizardPanel)));
	}

	/**
	 * The titles are in the individual panels as the title changes based on the state.
	 */
	protected VerticalLayout getTitlePanel() {
		return null;
	}

	@Override
	public void updateStatus(double percentage) {
		PushUtils.push(ui, () ->
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(percentage));
	}

	@Override
	public void updateError(String error) {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			uploadModelWizardPanel.setError(error);
		});
	}

	@Override
	public void fileSuccessfullyVerified() {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			setVisibleWizardPanel(modelDetailsWizardPanel);
			projectBinder.readBean(project);
			modelBinder.readBean(model);
			statusPanel.setModelDetails();
		});
	}
}
