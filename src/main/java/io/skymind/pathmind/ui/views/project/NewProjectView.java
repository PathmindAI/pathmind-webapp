package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.ProjectUtils;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.project.components.NewProjectLogoWizardPanel;
import io.skymind.pathmind.ui.views.project.components.wizard.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "newProject", layout = MainLayout.class)
public class NewProjectView extends PathMindDefaultView implements StatusUpdater
{
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ExperimentRepository experimentRepository;

	private Project project;
	private Binder<Project> binder;

	private UI ui;

	private NewProjectLogoWizardPanel logoPanel;
	private NewProjectStatusWizardPanel statusPanel;
	private CreateANewProjectWizardPanel createProjectPanel;
	private PathminderHelperWizardPanel pathminderHelperWizardPanel;
	private UploadModelWizardPanel uploadModelWizardPanel;
	private ModelDetailsWizardPanel modelDetailsWizardPanel;

	private List<Component> wizardPanels;

	private boolean hasErrorsFileCheck = false;

	public NewProjectView()
	{
		super();
		this.ui = UI.getCurrent();
		this.project = ProjectUtils.generateNewDefaultProject();
	}

	/**
	 * I put it in a method because the screenflow is not yet stable enough and I suspect we may need to reference
	 * in more than one place.
	 */
	private boolean isValidForm() {
		try {
			binder.writeBean(project);
			return true;
		} catch (ValidationException e) {
			return false;
		}
	}

	protected Component getMainContent()
	{
		binder = new Binder<>(Project.class);

		logoPanel = new NewProjectLogoWizardPanel();
		statusPanel = new NewProjectStatusWizardPanel();
		createProjectPanel = new CreateANewProjectWizardPanel(binder);
		pathminderHelperWizardPanel = new PathminderHelperWizardPanel();
		uploadModelWizardPanel = new UploadModelWizardPanel();
		modelDetailsWizardPanel = new ModelDetailsWizardPanel(binder);

		wizardPanels = Arrays.asList(
				createProjectPanel,
				pathminderHelperWizardPanel,
				uploadModelWizardPanel,
				modelDetailsWizardPanel);

		setVisibleWizardPanel(createProjectPanel);

		createProjectPanel.addButtonClickListener(click -> handleNewProjectClicked());
		pathminderHelperWizardPanel.addButtonClickListener(click -> handleNextStepClicked());
		uploadModelWizardPanel.addButtonClickListener(click -> handleUploadWizardClicked());
		modelDetailsWizardPanel.addButtonClickListener(click -> handleMoreDetailsClicked(click));

		return WrapperUtils.wrapCenteredFormVertical(
				logoPanel,
				statusPanel,
				createProjectPanel,
				pathminderHelperWizardPanel,
				uploadModelWizardPanel,
				modelDetailsWizardPanel);
	}

	private void handleMoreDetailsClicked(ClickEvent<Button> click) {
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			if(!isValidForm())
				return;

			projectRepository.insertProject(project);
			experimentRepository.insertExperimentsForProject(project);
			UI.getCurrent().navigate(ExperimentView.class, project.getExperiments().get(0).getId());
		});
	}

	private void handleUploadWizardClicked() {
		uploadModelWizardPanel.showFileCheckPanel();
		ProjectFileCheckService.checkFile(this, true);
	}

	private void handleNextStepClicked() {
		setVisibleWizardPanel(uploadModelWizardPanel);
		statusPanel.setUploadModel();
	}

	private void handleNewProjectClicked()
	{
		if(!isValidForm())
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

	private Component getProjectView() {
		return this;
	}

	@Override
	public void updateStatus(double percentage) {
//		PushUtils.push(getProjectView(), () -> {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(percentage);
		});
	}

	@Override
	public void updateError(String error) {
		// TODO -> Implement
	}

	@Override
	public void done() {
//		PushUtils.push(getProjectView(), () -> {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			setVisibleWizardPanel(modelDetailsWizardPanel);
			binder.readBean(project);
			statusPanel.setModelDetails();
		});
	}
}
