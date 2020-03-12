package io.skymind.pathmind.ui.views.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.PathmindUserDetails;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.FileCheckResult;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.meta.PathmindMeta;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.FormUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.ui.views.guide.GuideOverview;
import io.skymind.pathmind.ui.views.model.components.ModelDetailsWizardPanel;
import io.skymind.pathmind.ui.views.model.components.UploadModelWizardPanel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@CssImport("./styles/styles.css")
@Route(value = Routes.UPLOAD_MODEL, layout = MainLayout.class)
@Slf4j
public class UploadModelView extends PathMindDefaultView implements StatusUpdater, HasUrlParameter<Long> {

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ProjectFileCheckService projectFileCheckService;

	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private Model model;

	private Binder<Model> modelBinder;

	private UI ui;

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

		uploadModelWizardPanel = new UploadModelWizardPanel(model);
		modelDetailsWizardPanel = new ModelDetailsWizardPanel(modelBinder);

		wizardPanels = Arrays.asList(
				uploadModelWizardPanel,
				modelDetailsWizardPanel);

		setVisibleWizardPanel(uploadModelWizardPanel);

		uploadModelWizardPanel.addFileUploadCompletedListener(() -> handleUploadWizardClicked());
		modelDetailsWizardPanel.addButtonClickListener(click -> handleMoreDetailsClicked());

		VerticalLayout wrapper = WrapperUtils.wrapFormCenterVertical(
				uploadModelWizardPanel,
				modelDetailsWizardPanel,
				createBacktoGuideButton());

		wrapper.getStyle().set("width", "auto");
		wrapper.getStyle().set("padding-top", "var(--lumo-space-xxl)");
		return wrapper;
	}

    @Override
    protected void initLoadData() throws InvalidDataException {
		project = projectDAO.getProject(projectId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + projectId));
    }

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		uploadModelWizardPanel.setProjectName(project.getName());
		modelDetailsWizardPanel.setProjectName(project.getName());
	}

	private void handleMoreDetailsClicked()
	{
		if(!FormUtils.isValidForm(modelBinder, model)) {
			return;
		}

		final String modelNotes = modelDetailsWizardPanel.notesFieldTextArea.getValue();
		final long experimentId = modelService.addModelToProject(model, project.getId(), modelNotes);
		
		if (!modelNotes.isEmpty()) {
			segmentIntegrator.addedNotesUploadModelView();
		}

		UI.getCurrent().navigate(NewExperimentView.class, experimentId);
	}

	private void handleUploadWizardClicked() {
		uploadModelWizardPanel.showFileCheckPanel();
		projectFileCheckService.checkFile(this, model.getFile());
	}

	private void setVisibleWizardPanel(Component wizardPanel) {
		wizardPanels.stream()
				.forEach(panel -> panel.setVisible(panel.equals(wizardPanel)));
	}

	private Button createBacktoGuideButton() {
		Button backToGuideButton = new Button("Back to Pathmind Guide", click -> UI.getCurrent().navigate(GuideOverview.class, projectId));
		backToGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		backToGuideButton.getStyle().set("margin-top", "var(--lumo-space-xxl)");
		return backToGuideButton;
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
			segmentIntegrator.modelImported(false);
		});
	}

	@Override
	public void fileSuccessfullyVerified(FileCheckResult result) {
		PushUtils.push(ui, () -> {
			uploadModelWizardPanel.setFileCheckStatusProgressBarValue(1.0);
			setVisibleWizardPanel(modelDetailsWizardPanel);

			if (result != null) {
				AnylogicFileCheckResult anylogicFileCheckResult = ((AnylogicFileCheckResult) (result));
				if (anylogicFileCheckResult.getPathmindMeta() != null) {
					PathmindMeta meta = anylogicFileCheckResult.getPathmindMeta();
					model.setNumberOfPossibleActions(meta.getPossibleActionCount());
					model.setNumberOfObservations(meta.getObservationCount());
				} else {
					model.setNumberOfPossibleActions(anylogicFileCheckResult.getNumAction());
					model.setNumberOfObservations(anylogicFileCheckResult.getNumObservation());
				}
			}

			modelBinder.readBean(model);
			segmentIntegrator.modelImported(true);
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
