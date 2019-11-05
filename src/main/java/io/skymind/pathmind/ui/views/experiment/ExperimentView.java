package io.skymind.pathmind.ui.views.experiment;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.components.dialog.RunConfirmDialog;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.PolicyChartPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyHighlightPanel;
import io.skymind.pathmind.ui.views.experiment.components.PolicyStatusDetailsPanel;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.experiment.components.TrainingsListPanel;
import io.skymind.pathmind.ui.views.policy.ExportPolicyView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPERIMENT_URL, layout = MainLayout.class)
public class ExperimentView extends PathMindDefaultView implements HasUrlParameter<String> {
    private Button exportPolicyButton;

    private enum ActionButtonState {
        Start, Next, Stop
    }

    private static final int EXPERIMENT_ID_SEGMENT = 0;
    private static final int POLICY_ID_SEGMENT = 1;

    private static final double DEFAULT_SPLIT_PANE_RATIO = 70;

    private Logger log = LogManager.getLogger(ExperimentView.class);

    private final UnicastProcessor<PathmindBusEvent> publisher;
    private final Flux<PathmindBusEvent> consumer;

    private long experimentId = -1;
    private long policyId = -1;
    private Policy policy;
    private Experiment experiment;

    private ScreenTitlePanel screenTitlePanel;

    private PolicyHighlightPanel policyHighlightPanel;
    private PolicyStatusDetailsPanel policyStatusDetailsPanel;
    private RewardFunctionEditor rewardFunctionEditor;
    private PolicyChartPanel policyChartPanel;
    private TrainingsListPanel trainingsListPanel;

    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private PolicyDAO policyDAO;
    @Autowired
    private TrainingService trainingService;
	@Autowired
	private UserDAO userDAO;

    private Button actionButton;
    private Button runFullTraining;
    private Button runDiscoveryTraining;

    public ExperimentView(UnicastProcessor<PathmindBusEvent> publisher, Flux<PathmindBusEvent> consumer) {
        super();
        this.publisher = publisher;
        this.consumer = consumer;
        addClassName("experiment-view");
    }

    @Override
    protected Component getTitlePanel() {
        screenTitlePanel = new ScreenTitlePanel("PROJECT");
        return screenTitlePanel;
    }

    @Override
    protected Component getMainContent() {
        return WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                getLeftPanel(),
                getRightPanel(),
                DEFAULT_SPLIT_PANE_RATIO);
    }

    private Component getLeftPanel() {
        trainingsListPanel = new TrainingsListPanel(consumer);

        trainingsListPanel.addSelectionListener(selectedPolicy -> {
            policy = selectedPolicy;
            policyHighlightPanel.update(selectedPolicy);
            policyStatusDetailsPanel.update(selectedPolicy);
            policyChartPanel.update(selectedPolicy);
            policyChartPanel.highlightPolicy(selectedPolicy);
            setActionButtonValue(selectedPolicy);
            exportPolicyButton.setVisible(policyDAO.hasPolicyFile(selectedPolicy.getId()));

            RunType selectedRunType = selectedPolicy.getRun().getRunTypeEnum();
            if (selectedRunType == RunType.TestRun && experiment.getPolicies().size() == 1) {
                runDiscoveryTraining.setVisible(true);
            } else if (selectedRunType == RunType.DiscoveryRun) {
                runDiscoveryTraining.setVisible(false);
                runFullTraining.setVisible(true);
            } else if (selectedRunType == RunType.FullRun) {
                runDiscoveryTraining.setVisible(false);
                runFullTraining.setVisible(false);
            }
        });

        policyChartPanel = new PolicyChartPanel(consumer);
        policyChartPanel.addSeriesClickListener(policyId -> trainingsListPanel.selectPolicyWithId(policyId));

        trainingsListPanel.getSearchBox().addFilterableComponents(policyChartPanel);

        return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
                policyChartPanel,
                trainingsListPanel);
    }

    private VerticalLayout getRightPanel() {
        rewardFunctionEditor = new RewardFunctionEditor();
        rewardFunctionEditor.setReadonly(true);
        rewardFunctionEditor.setSizeFull();

        policyHighlightPanel = new PolicyHighlightPanel();
        policyStatusDetailsPanel = new PolicyStatusDetailsPanel();

        actionButton = new Button(ActionButtonState.Start.name(), click -> handleActionButtonClicked());
        actionButton.setVisible(false);

        // TODO: Put this in the appropriate place
        runFullTraining = new Button("Start Full Run", new Image("frontend/images/start.svg", "run"), click -> {
            final Experiment experiment = experimentDAO.getExperiment(policy.getRun().getExperimentId());
            trainingService.startFullRun(experiment, policy);

            ConfirmDialog confirmDialog = new RunConfirmDialog();
            confirmDialog.open();

            try {
                loadData();
                updateScreen(null);
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        });
        runFullTraining.setVisible(false);
        runFullTraining.addClassNames("large-image-btn", "run");

        runDiscoveryTraining = new Button("Start Discovery Run", new Image("frontend/images/start.svg", "run"), click -> {
            final Experiment experiment = experimentDAO.getExperiment(policy.getRun().getExperimentId());
            trainingService.startDiscoveryRun(experiment);

            ConfirmDialog confirmDialog = new RunConfirmDialog();
            confirmDialog.open();

            try {
                loadData();
                updateScreen(null);
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        });
        runDiscoveryTraining.setVisible(false);
        runDiscoveryTraining.addClassNames("large-image-btn", "run");


        final HorizontalLayout buttons = WrapperUtils.wrapWidthFullCenterHorizontal(
                new NewExperimentButton(experimentDAO, experiment.getModelId(), "TODO")
        );
        exportPolicyButton = new Button("Export Policy", click -> UI.getCurrent().navigate(ExportPolicyView.class, policy.getId()));
        buttons.add(exportPolicyButton);
        exportPolicyButton.setVisible(false);

        return WrapperUtils.wrapSizeFullVertical(
                WrapperUtils.wrapWidthFullCenterHorizontal(actionButton, runDiscoveryTraining, runFullTraining),
                policyHighlightPanel,
                policyStatusDetailsPanel,
                rewardFunctionEditor,
                buttons);
    }

    // TODO -> I don't fully understand the button logic, including when it's muted from just the screenshots.
    private void setActionButtonValue(Policy policy) {
        switch (policy.getRun().getRunTypeEnum()) {
            case TestRun:
                actionButton.setText("Next");
                break;
            case DiscoveryRun:
                actionButton.setText("Stop");
                break;
            case FullRun:
                actionButton.setText("Todo");
                break;
        }
    }

    private void handleActionButtonClicked() {
        NotificationUtils.showTodoNotification("Needs to be implemented");
        // TODO -> We need to hook Paul's backend code here.
    }

    @Override
    protected boolean isAccessAllowedForUser() {
      return userDAO.isUserAllowedAccessToExperiment(experimentId);
    }

    /**
     * For now I'm doing a manual parse of the parameter since Vaadin only seems
     * to have the ability to parse to a wildcard parameter if you need more than one parameter
     * as explained in this Vaadin issue: https://github.com/vaadin/flow/issues/4213 There is an
     * add-on but I don't think it's worth adding on yet since this is the only place we have this
     * need and Vaadin will most likely add this capability in the future.
     */
    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        if (StringUtils.isEmpty(parameter)) {
            this.experimentId = -1;
            return;
        }

        String[] segments = parameter.split("/");

        if (NumberUtils.isDigits(segments[EXPERIMENT_ID_SEGMENT]))
            experimentId = Long.parseLong(segments[EXPERIMENT_ID_SEGMENT]);
        if (segments.length > 1 && NumberUtils.isDigits(segments[POLICY_ID_SEGMENT]))
            policyId = Long.parseLong(segments[POLICY_ID_SEGMENT]);
    }

    @Override
    protected void loadData() throws InvalidDataException {
        experiment = experimentDAO.getExperiment(experimentId);
        if (experiment == null)
            throw new InvalidDataException("Attempted to access Experiment: " + experimentId);
        experiment.setPolicies(policyDAO.getPoliciesForExperiment(experimentId));
    }

    @Override
    protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException {
        experiment.getPolicies().stream()
                .filter(p -> p.getRun().getRunTypeEnum().equals(RunType.DiscoveryRun))
                .findAny()
                .ifPresent(p -> runDiscoveryTraining.setVisible(false));

        screenTitlePanel.setSubtitle(experiment.getProject().getName());
        rewardFunctionEditor.setValue(experiment.getRewardFunction());
        policyChartPanel.update(experiment);
        trainingsListPanel.update(experiment, policyId);
    }
}
