package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.mock.MockDefaultValues;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.model.components.RewardVariablesTable;
import lombok.extern.slf4j.Slf4j;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
@Slf4j
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {
	private long experimentId = -1;
	private Experiment experiment;
	private List<RewardVariable> rewardVariables;

	private Div errorMessageWrapper;
	private RewardFunctionEditor rewardFunctionEditor;
	private TextArea notesFieldTextArea;
	private RewardVariablesTable rewardVariablesTable;
	
	private Button startRunButton;

	@Autowired
	private ExperimentDAO experimentDAO;
	@Autowired
	private RewardVariableDAO rewardVariableDAO;
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;
	@Autowired
	private RewardValidationService rewardValidationService;

	private Binder<Experiment> binder;

	public NewExperimentView() {
		super();
		addClassName("new-experiment-view");
	}

	@Override
	protected Component getTitlePanel() {
		return null;
	}

	@Override
	protected Component getMainContent() {
		VerticalLayout mainContent = WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(createBreadcrumbs()), 
				createMainPanel());
		binder = new Binder<>(Experiment.class);
		setupBinder();
		return mainContent;
	}

	private Component createMainPanel() {
		startRunButton = new Button("Train Policy", VaadinIcon.PLAY.create(), click -> handleStartRunButtonClicked());
		startRunButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		startRunButton.setEnabled(false);
		
		VerticalLayout mainPanel = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
		VerticalLayout panelTitle = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
				LabelFactory.createLabel("Write your reward function", CssMindPathStyles.SECTION_TITLE_LABEL),
				LabelFactory.createLabel("To judge if an action is a good one, we calculate a reward score. "
						+ "The reward score is based on the reward function.", CssMindPathStyles.SECTION_SUBTITLE_LABEL));
		panelTitle.setClassName("panel-title");
		
		SplitLayout rewardFunctionWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(getRewardFnEditorPanel(), 
				WrapperUtils.wrapSizeFullCenterHorizontal(getRewardVariableNamesPanel(), getNotesPanel()));
		rewardFunctionWrapper.setClassName("reward-function-wrapper");
		
		mainPanel.add(
				WrapperUtils.wrapWidthFullBetweenHorizontal(panelTitle, startRunButton),
				rewardFunctionWrapper,
				getErrorsPanel());
		mainPanel.setClassName("view-section");
		return mainPanel;
	}

	private VerticalLayout getRewardFnEditorPanel() {
		rewardFunctionEditor = new RewardFunctionEditor();
		rewardFunctionEditor.addValueChangeListener(changeEvent -> {
			final List<String> errors = rewardValidationService.validateRewardFunction(changeEvent.getValue());
			final String errorText = String.join("\n", errors);
			final String wrapperClassName = (errorText.length() == 0) ? "noError" : "hasError";

			errorMessageWrapper.removeAll();
			if ((errorText.length() == 0)) {
				errorMessageWrapper.add(new Icon(VaadinIcon.CHECK), new Span("No Errors"));
				startRunButton.setEnabled(true);
			} else {
				errorMessageWrapper.setText(errorText);
				startRunButton.setEnabled(false);
			}
			errorMessageWrapper.removeClassNames("hasError", "noError");
			errorMessageWrapper.addClassName(wrapperClassName);
		});

		HorizontalLayout header = WrapperUtils.wrapWidthFullBetweenHorizontal(LabelFactory.createLabel("Reward Function", CssMindPathStyles.BOLD_LABEL), getActionButton());
		header.getStyle().set("align-items", "center");
		VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapSizeFullVertical(header, rewardFunctionEditor);
		rewardFnEditorPanel.addClassName("reward-fn-editor-panel");
		rewardFnEditorPanel.setPadding(false);
		return rewardFnEditorPanel;
	}
	private Component getErrorsPanel() {
		errorMessageWrapper = new Div();
		errorMessageWrapper.addClassName("error-message-wrapper");
		Div errorsPanel = new Div(LabelFactory.createLabel("Errors", CssMindPathStyles.BOLD_LABEL), errorMessageWrapper);
		errorsPanel.addClassName("errors-wrapper");
		return errorsPanel;
	}

	private void setupBinder() {
		binder.forField(rewardFunctionEditor).asRequired().bind(Experiment::getRewardFunction, Experiment::setRewardFunction);
		binder.forField(notesFieldTextArea).bind(Experiment::getUserNotes, Experiment::setUserNotes);
	}

	private VerticalLayout getNotesPanel() {
		notesFieldTextArea = new TextArea("", "", "Add Notes (Optional)");
		notesFieldTextArea.setSizeFull();
		VerticalLayout wrapper = WrapperUtils.wrapSizeFullVertical(LabelFactory.createLabel("Experiment Notes", CssMindPathStyles.BOLD_LABEL), notesFieldTextArea);
		wrapper.setPadding(false);
		return wrapper;
	}
	private VerticalLayout getRewardVariableNamesPanel() {
		rewardVariablesTable = new RewardVariablesTable();
		rewardVariablesTable.setSizeFull();
		// rewardVariablesTable.addValueChangeListener(event -> handleRewardVariableNameChanged(event.getValue()));
		VerticalLayout wrapper = WrapperUtils.wrapSizeFullVertical(LabelFactory.createLabel("Reward variable names", CssMindPathStyles.BOLD_LABEL), rewardVariablesTable);
		wrapper.setPadding(false);
		return wrapper;
	}

	private void handleRewardVariableNameChanged(List<RewardVariable> updatedRewardVariables) {
		System.out.println("updatedRewardVariables: "+updatedRewardVariables);
		rewardFunctionEditor.setVariableNames(updatedRewardVariables);
		rewardVariableDAO.saveRewardVariables(updatedRewardVariables);
	}

	private void handleStartRunButtonClicked() {
		if (!FormUtils.isValidForm(binder, experiment)) {
			return;
		}

		if (!notesFieldTextArea.isEmpty()) {
			segmentIntegrator.addedNotesNewExperimentView();
		}

		experimentDAO.updateExperiment(experiment);
		segmentIntegrator.rewardFuntionCreated();

		trainingService.startRun(experiment);
		segmentIntegrator.discoveryRunStarted();

		UI.getCurrent().navigate(ExperimentView.class, experimentId);
	}

	private Button getActionButton() {
		return new Button("Save", click -> handleSaveDraftClicked());
	}

	private void handleSaveDraftClicked() {
		experimentDAO.updateExperiment(experiment);
		segmentIntegrator.draftSaved();
		NotificationUtils.showSuccess("Draft successfully saved");
	}

	private Breadcrumbs createBreadcrumbs() {
		return new Breadcrumbs(experiment.getProject(), experiment.getModel(), experiment);
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToExperiment(experimentId);
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void initLoadData() {
		experiment = experimentDAO.getExperiment(experimentId).orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		rewardVariables = rewardVariableDAO.getRewardVariablesForModel(experiment.getModelId());
		if (MockDefaultValues.isDebugAccelerate() && StringUtils.isEmpty(experiment.getRewardFunction()))
			experiment.setRewardFunction(MockDefaultValues.NEW_EXPERIMENT_REWARD_FUNCTION);
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		binder.setBean(experiment);
		rewardFunctionEditor.setVariableNames(rewardVariables);
		rewardVariablesTable.setValue(rewardVariables);
	}
}
