package io.skymind.pathmind.ui.views.experiment;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.mock.MockDefaultValues;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.PathmindTextArea;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.FormUtils;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import lombok.extern.slf4j.Slf4j;

@CssImport("./styles/views/new-experiment-view.css")
@Route(value = Routes.NEW_EXPERIMENT, layout = MainLayout.class)
@Slf4j
public class NewExperimentView extends PathMindDefaultView implements HasUrlParameter<Long> {
    private static final double DEFAULT_SPLIT_PANE_RATIO = 60;

    private long experimentId = -1;
    private Experiment experiment;

    private Div errorsWrapper;
    private PathmindTextArea tipsTextArea;
    private RewardFunctionEditor rewardFunctionEditor;
    private TextArea notesFieldTextArea;
    private Button startRunButton;

    @Autowired
    private ExperimentDAO experimentDAO;
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
        binder = new Binder<>(Experiment.class);
        VerticalLayout mainContent = WrapperUtils.wrapWidthFullVertical(
            WrapperUtils.wrapWidthFullCenterHorizontal(createBreadcrumbs()),
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        getLeftPanel(),
                        getRightPanel(),
                        DEFAULT_SPLIT_PANE_RATIO)
                );
        setupBinder();
        return mainContent;
    }

    private void setupBinder() {
    	binder.forField(rewardFunctionEditor)
	        .asRequired()
	        .bind(Experiment::getRewardFunction, Experiment::setRewardFunction);
    	binder.forField(notesFieldTextArea)
        	.bind(Experiment::getUserNotes, Experiment::setUserNotes);
	}

	private Component getLeftPanel() {
        Div errorMessageWrapper = new Div();
        errorMessageWrapper.addClassName("error-message-wrapper");
        errorsWrapper = new Div(
            new Span("Errors"),
            errorMessageWrapper
        );
        errorsWrapper.addClassName("errors-wrapper");

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

        return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
                getRewardFnEditorPanel(),
                errorsWrapper,
            60);
    }

    private VerticalLayout getRewardFnEditorPanel() {
        HorizontalLayout header = WrapperUtils.wrapWidthFullBetweenHorizontal(
            new Span("Write your reward function:"),
            getActionButton()
        );
        header.getStyle().set("align-items", "center");
        VerticalLayout rewardFnEditorPanel = WrapperUtils.wrapSizeFullVertical(
            header,
            rewardFunctionEditor
        );
        rewardFnEditorPanel.addClassName("reward-fn-editor-panel");
        return rewardFnEditorPanel;
    }

    private VerticalLayout getRightPanel() {
        tipsTextArea = new PathmindTextArea("Tips");
        tipsTextArea.setSizeFull();
        tipsTextArea.setReadOnly(true);
        tipsTextArea.setValue(
                "1. The \"after\" variable is used to retrieve the observation value after an action is performed. The \"before\" variable is used to retrieve the observation value before an action is performed. So if our observation value needs to be maximized, we can write\n" +
                "reward = after[0] - before[0];\n" +
                "\n" +
                "2. Weights can also be added to the reward calculation. In this case, if a condition is satisfied, then the learning agent is rewarded a score of 5 otherwise it gets a zero.\n" +
                "reward = after[0] > before[0] ? 5 : 0;\n" +
                "\n" +
                "3. For multiple lines, the reward variable can be summed or subtracted. The reward function is calculated between every two actions.\n" +
                "reward = after[0] - before[0];\n" +
                "reward += after[1] > before[1] ? 5 : 0;\n" +
                "reward -= after[2] - before[2];"
        );

        notesFieldTextArea = new TextArea("Experiment Notes", "", "Add Notes");
        notesFieldTextArea.setSizeFull();

        return WrapperUtils.wrapSizeFullVertical(
                getTopButtonPanel(),
                notesFieldTextArea,
                tipsTextArea);
    }

    private Component getTopButtonPanel() {
        startRunButton = new Button("Start Training", new Image("frontend/images/start.svg", "run"),
                click -> handleStartRunButtonClicked());
        startRunButton.addClassNames("large-image-btn","run");
        startRunButton.setEnabled(false);
        return WrapperUtils.wrapWidthFullCenterVertical(startRunButton);
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
        
        trainingService.startDiscoveryRun(experiment);
        segmentIntegrator.discoveryRunStarted();

        UI.getCurrent().navigate(ExperimentView.class, experimentId);
    }

    private Button getActionButton() {
        Button actionButton = new Button("Save Draft", new Icon(VaadinIcon.FILE),
                click -> handleSaveDraftClicked());
        actionButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return actionButton;
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
        experiment = experimentDAO.getExperiment(experimentId)
                .orElseThrow(() -> new InvalidDataException("Attempted to access Experiment: " + experimentId));
		if(MockDefaultValues.isDebugAccelerate() && StringUtils.isEmpty(experiment.getRewardFunction()))
			experiment.setRewardFunction(MockDefaultValues.NEW_EXPERIMENT_REWARD_FUNCTION);
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        binder.setBean(experiment);
    }
}
