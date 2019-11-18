package io.skymind.pathmind.ui.views.experiment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.buttons.BackButton;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.components.buttons.ShowRewardFunctionButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.components.RewardFunctionEditor;
import io.skymind.pathmind.ui.views.experiment.filter.ExperimentFilter;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentGrid;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPERIMENTS_URL, layout = MainLayout.class)
public class ExperimentsView extends PathMindDefaultView implements HasUrlParameter<Long> {
    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private RunDAO runDAO;
    @Autowired
    private ModelDAO modelDAO;
	@Autowired
	private UserDAO userDAO;

    private long modelId;
    private Model currentModel;
    private List<Experiment> experiments;

    private ArchivesTabPanel<Experiment> archivesTabPanel;
    private ExperimentGrid experimentGrid;
    private TextArea getObservationTextArea;
    private RewardFunctionEditor rewardFunctionEditor;
    private Label rewardFunctionTitle;

    public ExperimentsView() {
        super();
        addClassName("experiments-view");
    }

    protected Component getMainContent() {
        setupExperimentListPanel();
        setupGetObservationTextArea();
        setupRewardFunctionEditor();
        setupArchivesTabPanel();

        return WrapperUtils.wrapSizeFullVertical(
                WrapperUtils.wrapWidthFullCenterHorizontal(getBackToModelsButton()),
                WrapperUtils.wrapWidthFullRightHorizontal(getSearchBox()),
                archivesTabPanel,
                WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
                        WrapperUtils.wrapSizeFullVertical(
                                experimentGrid),
                        WrapperUtils.wrapSizeFullVertical(
                        		rewardFunctionTitle,
                                rewardFunctionEditor,
                                getObservationTextArea),
                        70),
                WrapperUtils.wrapWidthFullCenterHorizontal(new NewExperimentButton(experimentDAO, modelId)));
    }

    private void setupRewardFunctionEditor() {
    	rewardFunctionTitle = new Label("Reward Functions");
    	rewardFunctionTitle.addClassNames("readonly-label");
        rewardFunctionEditor = new RewardFunctionEditor();
        rewardFunctionEditor.setReadonly(true);
        rewardFunctionEditor.setSizeFull();
    }

    /**
     * Using any experiment's getProject().getId() since they should all be the same. I'm assuming at this point
     * that there has to be at least one experiment to be able to get here.
     */
    private Button getBackToModelsButton() {
        return new BackButton("Back to Models",
                click -> UI.getCurrent().navigate(ModelsView.class, experiments.get(0).getProject().getId()));
    }

    private void setupGetObservationTextArea() {
        getObservationTextArea = new TextArea("getObservations");
        getObservationTextArea.setSizeFull();
        getObservationTextArea.setReadOnly(true);
    }

    private SearchBox getSearchBox() {
        return new SearchBox<Experiment>(experimentGrid, new ExperimentFilter());
    }

    private void setupArchivesTabPanel() {
        archivesTabPanel = new ArchivesTabPanel<Experiment>(
                "Experiments",
                false,
                experimentGrid,
                this::getExperiments,
                (experimentId, isArchivable) -> experimentDAO.archive(experimentId, isArchivable));
    }

    private void setupExperimentListPanel() {
        experimentGrid = new ExperimentGrid();
        experimentGrid.addComponentColumn(exp -> createActionButtons(exp)).setHeader("Actions").setSortable(false);
        experimentGrid.addItemClickListener(event -> handleExperimentClick(event.getItem()));
    }

    private HorizontalLayout createActionButtons(Experiment exp) {
    	ShowRewardFunctionButton showRewardFunctionButton = new ShowRewardFunctionButton();
    	showRewardFunctionButton.addClickListener(evt -> showRewardFunction(exp));
		HorizontalLayout layout = new HorizontalLayout();
		layout.add(archivesTabPanel.getArchivesButton(exp), showRewardFunctionButton);
		return layout;
	}

	private void showRewardFunction(Experiment exp) {
		rewardFunctionTitle.setText("Reward Function - Experiment #" + exp.getName());
		rewardFunctionEditor.setValue(exp.getRewardFunction());
	}

	private void handleExperimentClick(Experiment experiment) {
        if (ExperimentUtils.isDraftRunType(experiment)) {
            UI.getCurrent().navigate(NewExperimentView.class, experiment.getId());
        } else {
            UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(experiment));
        }
    }

	@Override
	protected boolean isAccessAllowedForUser() {
        // The UserDAO method does less than the initLoadData already does and is an extra database call that gives no current
        // benefit and has a performance cost. Once we add proper logic we can re-implement it in the software.
		// return userDAO.isUserAllowedAccessToModel(modelId);
        return true;
	}

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("PROJECT " + getProjectName());
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    // It's either get the project name from the first experiment (which has to exist for the page to load) or
    // we need to do a separate database call.
    private String getProjectName() {
        return experiments.get(0).getProject().getName();
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        experiments = experimentRepository.getExperimentsForModel(modelId);
        if (experiments == null || experiments.isEmpty())
            throw new InvalidDataException("Attempted to access Experiments for Model: " + modelId);

        // set runs to experiment
        experiments.stream()
                .forEach(e -> e.setRuns(runDAO.getRunsForExperiment(e.getId())));

        // set current model
        currentModel = modelDAO.getModel(modelId);
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) throws InvalidDataException {
        experimentGrid.setItems(experiments);
        archivesTabPanel.initData();
        getObservationTextArea.setValue(currentModel.getGetObservationForRewardFunction());
        showRewardFunction(experiments.get(0));
    }

    @Override
    public void setParameter(BeforeEvent event, Long modelId) {
        this.modelId = modelId;
    }
}
