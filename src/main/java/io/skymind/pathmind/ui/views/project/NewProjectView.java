package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.experiment.RewardFunctionView;
import io.skymind.pathmind.ui.views.project.components.FileCheckPanel;
import io.skymind.pathmind.ui.views.project.components.NewProjectForm;
import io.skymind.pathmind.utils.WrapperUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "newProject", layout = MainLayout.class)
public class NewProjectView extends VerticalLayout implements BasicViewInterface, StatusUpdater
{
	@Autowired
	private ProjectRepository projectRepository;

	private Project project = new Project();
	private Binder<Project> binder = new Binder<>(Project.class);

	private UI ui;

	// TODO -> Do we need to be able to handle interrupts on file check (back to import)?
	private Button createProjectButton;
	private Button backToImportButton;
	private Button startYourProjectButton;

	private ActionMenu actionMenu;

	private NewProjectForm newProjectForm;
	private FileCheckPanel fileCheckPanel;

	private boolean hasErrorsFileCheck = false;

	// TODO -> Logo and project title panel
	public NewProjectView()
	{
		add(getActionMenu());
		add(getMainContent());

		this.ui = UI.getCurrent();

		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	private void handleBackToImportClicked() {
		setVisibleActionMenuButtons(true, false, false);
		fileCheckPanel.setVisible(false);
		fileCheckPanel.reset();
		newProjectForm.setVisible(true);
		backToImportButton.setEnabled(false);
	}

	private void handleCreateProjectClicked()
	{
		if(!isValidForm())
			return;

		hasErrorsFileCheck = false;
		setVisibleActionMenuButtons(false, true, false);
		newProjectForm.setVisible(false);
		fileCheckPanel.setVisible(true);
		backToImportButton.setEnabled(false);

		// TODO -> If the project is equal to Error then add errors.
		ProjectFileCheckService.checkFile(this, "Error".equalsIgnoreCase(newProjectForm.getProjectName()));
	}

	// TODO -> Implement properly. We need to pass a parameter, etc.
	private void handleStartYourProjectClicked() {
		long projectId = projectRepository.insertProject(project);
		UI.getCurrent().navigate(RewardFunctionView.class, projectId);
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

	public ActionMenu getActionMenu()
	{
		createProjectButton = new Button("Create Project >", click -> handleCreateProjectClicked());
		backToImportButton = new Button("< Back to Import", click -> handleBackToImportClicked());
		startYourProjectButton = new Button("Start Your Project! >", click -> handleStartYourProjectClicked());

		actionMenu = new ActionMenu(
				createProjectButton,
				backToImportButton,
				startYourProjectButton);

		// Can only be enabled once a file check has been completed.
		backToImportButton.setEnabled(false);

		setVisibleActionMenuButtons(true, false, false);

		return actionMenu;
	}

	public Component getMainContent()
	{
		newProjectForm = new NewProjectForm(binder);
		fileCheckPanel = new FileCheckPanel();

		fileCheckPanel.setVisible(false);

		return WrapperUtils.wrapCenterAlignmentFullVertical(
				newProjectForm,
				fileCheckPanel);
	}

	/**
	 * The titles are in the individual panels as the title changes based on the state.
	 */
	public VerticalLayout getTitlePanel() {
		return null;
	}

	private void setVisibleActionMenuButtons(
			boolean isCreateProjectButtonVisible,
			boolean isBackToImportProjectButtonVisible,
			boolean isStartYourProjectButtonVisible)
	{
		actionMenu.setButtonVisible(createProjectButton, isCreateProjectButtonVisible);
		actionMenu.setButtonVisible(backToImportButton, isBackToImportProjectButtonVisible);
		actionMenu.setButtonVisible(startYourProjectButton, isStartYourProjectButtonVisible);
	}

	@Override
	public void updateStatus(double percentage) {
		ui.access(() -> {
			fileCheckPanel.updateProgressBar(percentage);
		});
	}

	@Override
	public void updateError(String error) {
		ui.access(() -> {
			fileCheckPanel.addError(error);
			hasErrorsFileCheck = true;
		});
	}

	@Override
	public void done() {
		ui.access(() -> {
			fileCheckPanel.done();
			if(hasErrorsFileCheck) {
				backToImportButton.setEnabled(true);
			} else {
				setVisibleActionMenuButtons(false, false, true);
			}
		});
	}
}
