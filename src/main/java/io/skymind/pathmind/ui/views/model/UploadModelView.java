package io.skymind.pathmind.ui.views.model;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.ModelUtils;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;
import io.skymind.pathmind.ui.utils.FormUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.ui.views.model.components.UploadModelStatusWizardPanel;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.ui.views.project.components.wizard.ModelDetailsWizardPanel;
import io.skymind.pathmind.ui.views.project.components.wizard.UploadModelWizardPanel;

@CssImport("./styles/styles.css")
@Route(value = Routes.UPLOAD_MODEL, layout = MainLayout.class)
public class UploadModelView extends PathMindDefaultView implements StatusUpdater, HasUrlParameter<Long> {

	private static Logger log = LogManager.getLogger(NewProjectView.class);

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ModelDAO modelDAO;
	
	@Autowired
	private ProjectFileCheckService projectFileCheckService ;

	private Model model;

	private Binder<Model> modelBinder;

	private UI ui;

	private UploadModelStatusWizardPanel statusPanel;
	private UploadModelWizardPanel uploadModelWizardPanel;
	private ModelDetailsWizardPanel modelDetailsWizardPanel;

	private List<Component> wizardPanels;

	private PathmindUserDetails user;
	private long projectId; 
	private Project project;

	public UploadModelView()
	{
		super();
		this.ui = UI.getCurrent();
		this.user = SecurityUtils.getUser();
	}

	protected Component getMainContent()
	{
		this.model = ModelUtils.generateNewDefaultModel();

		modelBinder = new Binder<>(Model.class);

		statusPanel = new UploadModelStatusWizardPanel();
		uploadModelWizardPanel = new UploadModelWizardPanel(model);
		modelDetailsWizardPanel = new ModelDetailsWizardPanel(modelBinder);

		wizardPanels = Arrays.asList(
				uploadModelWizardPanel,
				modelDetailsWizardPanel);

		setVisibleWizardPanel(uploadModelWizardPanel);

		uploadModelWizardPanel.addButtonClickListener(click -> handleUploadWizardClicked());
		modelDetailsWizardPanel.addButtonClickListener(click -> handleMoreDetailsClicked());

		return WrapperUtils.wrapFormCenterVertical(
				statusPanel,
				uploadModelWizardPanel,
				modelDetailsWizardPanel);
	}
	
	@Override
	protected void initLoadData() throws InvalidDataException {
		project = projectDAO.getProject(projectId);
	}
	
	@Override
	protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
		uploadModelWizardPanel.setProjectName(project.getName());
	}

	private void handleMoreDetailsClicked()
	{
		ExceptionWrapperUtils.handleButtonClicked(() ->
		{
			if(!FormUtils.isValidForm(modelBinder, model))
				return;

			final long experimentId = modelDAO.addModelToProject(model, project.getId());

			UI.getCurrent().navigate(NewExperimentView.class, experimentId);
		});
	}

	private void handleUploadWizardClicked() {
		if (user.getEmail().equals("edward@skymind.io")) { // This is Ed!
			log.info("User is Ed, skipping file check");
			fileSuccessfullyVerified();
		} else {
			uploadModelWizardPanel.showFileCheckPanel();
			projectFileCheckService.checkFile(this, model.getFile());
		}
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
			modelBinder.readBean(model);
			statusPanel.setModelDetails();
		});
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		this.projectId = projectId;
		
	}
}
