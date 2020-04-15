package io.skymind.pathmind.webapp.ui.views.model;

import java.time.LocalDateTime;
import java.util.List;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.TabPanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.notesField.NotesField;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.project.components.panels.ExperimentGrid;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

@Route(value = Routes.MODEL_URL, layout = MainLayout.class)
public class ModelView extends PathMindDefaultView implements HasUrlParameter<Long> {
	@Autowired
	private ExperimentDAO experimentDAO;
	@Autowired
	private ModelDAO modelDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private long modelId;
	private Model model;
	private List<Experiment> experiments;

	private ArchivesTabPanel<Experiment> archivesTabPanel;
	private ExperimentGrid experimentGrid;

	private Span modelName;
	private Span createdDate;
	private Span fileName;
	private Span actionsText;
	private Span observationsText;
	private Span rewardVariableNamesText;
	private Span rewardVariablesCountText;

	public ModelView() {
		super();
	}

	protected Component getMainContent() {
		setupExperimentListPanel();
		setupArchivesTabPanel();

		addClassName("model-view");

		HorizontalLayout headerWrapper = WrapperUtils.wrapWidthFullCenterHorizontal(archivesTabPanel, new NewExperimentButton(experimentDAO, modelId));
		headerWrapper.addClassName("page-content-header");

		FlexLayout leftPanel = new ViewSection(headerWrapper, experimentGrid);
		FlexLayout rightPanel = createRightPanel();

		SplitLayout gridWrapper = WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
			leftPanel,
			rightPanel,
		70);
		gridWrapper.addClassName("page-content");

		return gridWrapper;
	}

	private FlexLayout createRightPanel() {
		modelName = LabelFactory.createLabel("", CssMindPathStyles.SECTION_TITLE_LABEL, CssMindPathStyles.TRUNCATED_LABEL);
		createdDate = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		fileName = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		actionsText = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		observationsText = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		rewardVariableNamesText = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		rewardVariablesCountText = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		
		NotesField notesField = createViewNotesField();
		TabPanel panelHeader = new TabPanel("Details");
		panelHeader.setEnabled(false);
		return new ViewSection(panelHeader, modelName, createdDate, fileName, actionsText, observationsText, rewardVariablesCountText, notesField);
	}

	/**
	 * Using any experiment's getProject() since they should all be the same. I'm assuming at this point
	 * that there has to be at least one experiment to be able to get here.
	 */
	private Breadcrumbs createBreadcrumbs() {
		return new Breadcrumbs(experiments.get(0).getProject(), model);
	}

	private void setupArchivesTabPanel() {
		archivesTabPanel = new ArchivesTabPanel<Experiment>(
				"Experiments",
				true,
				experimentGrid,
				this::getExperiments,
				(experimentId, isArchivable) -> experimentDAO.archive(experimentId, isArchivable));
	}

	private void setupExperimentListPanel() {
		experimentGrid = new ExperimentGrid();
		experimentGrid.addItemClickListener(event -> handleExperimentClick(event.getItem()));
	}

	private NotesField createViewNotesField() {
		return new NotesField(
			"Notes",
			model.getUserNotes(),
			updatedNotes -> {
				modelDAO.updateUserNotes(modelId, updatedNotes);
				NotificationUtils.showSuccess("Notes saved");
				segmentIntegrator.updatedNotesExperimentsView();
			}
		);
	}

	private void handleExperimentClick(Experiment experiment) {
		if (ExperimentUtils.isDraftRunType(experiment)) {
			UI.getCurrent().navigate(NewExperimentView.class, experiment.getId());
		} else {
			UI.getCurrent().navigate(ExperimentView.class, experiment.getId());
		}
	}
	
	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToModel(modelId);
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel(createBreadcrumbs());
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	@Override
	protected void initLoadData() {
		model = modelDAO.getModel(modelId)
				.orElseThrow(() -> new InvalidDataException("Attempted to access Model: " + modelId));
		experiments = experimentDAO.getExperimentsForModel(modelId);
		if (experiments == null || experiments.isEmpty())
			throw new InvalidDataException("Attempted to access Experiments for Model: " + modelId);
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		modelName.setText("Model #"+model.getName());
		VaadinDateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			// experimentGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
			LocalDateTime dateCreatedData = model.getDateCreated();
			int rewardVariablesCountData = model.getRewardVariablesCount();
			experimentGrid.setItems(experiments);
			createdDate.setText(String.format("Uploaded on %s", DateAndTimeUtils.formatDateAndTimeShortFormatter(dateCreatedData, timeZoneId)));
			actionsText.setText("Actions: "+model.getNumberOfPossibleActions());
			observationsText.setText("Observations: "+model.getNumberOfObservations());
			rewardVariablesCountText.setText("Reward Variables Count: "+rewardVariablesCountData);
			if (dateCreatedData == null) {
				createdDate.setVisible(false);
			}
			if (rewardVariablesCountData == 0) {
				rewardVariablesCountText.setVisible(false);
			}
		});
		archivesTabPanel.initData();
	}

	@Override
	public void setParameter(BeforeEvent event, Long modelId) {
		this.modelId = modelId;
	}
}
