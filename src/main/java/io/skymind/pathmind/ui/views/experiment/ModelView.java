package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ViewSection;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.components.notesField.NotesField;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentGrid;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@CssImport("./styles/styles.css")
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
	private String projectName;
	private List<Experiment> experiments;

	private ArchivesTabPanel<Experiment> archivesTabPanel;
	private ExperimentGrid experimentGrid;

	public ModelView() {
		super();
		addClassName("model-view");
	}

	protected Component getMainContent() {
		setupExperimentListPanel();
		setupArchivesTabPanel();
		projectName = getProjectName();

		VerticalLayout leftPanel = WrapperUtils.wrapSizeFullVertical(
			archivesTabPanel,
			new ViewSection(experimentGrid)
		);
		leftPanel.setPadding(false);

		return WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(createBreadcrumbs()),
				WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
						leftPanel,
						createViewNotesField(),
						70),
				WrapperUtils.wrapWidthFullCenterHorizontal(new NewExperimentButton(experimentDAO, modelId)));
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

	private HorizontalLayout createViewNotesField() {
		return new NotesField(
			"Model Notes",
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
		return null;
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	// It's either get the project name from the first experiment (which has to exist for the page to load) or
	// we need to do a separate database call.
	private String getProjectName() {
		return ExperimentUtils.getProjectName(experiments.get(0));
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
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			// experimentGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
			experimentGrid.setItems(experiments);
		});
		archivesTabPanel.initData();
	}

	@Override
	public void setParameter(BeforeEvent event, Long modelId) {
		this.modelId = modelId;
	}
}
