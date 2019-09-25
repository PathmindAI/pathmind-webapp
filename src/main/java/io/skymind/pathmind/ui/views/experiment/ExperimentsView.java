package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.TodoView;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentSearchBox;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentGrid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@StyleSheet("frontend://styles/styles.css")
@Route(value="experiments", layout = MainLayout.class)
public class ExperimentsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ExperimentRepository experimentRepository;
	@Autowired
	private ModelDAO modelDAO;

	private long modelId;
	private List<Experiment> experiments;

	private ArchivesTabPanel archivesTabPanel;
	private ExperimentSearchBox searchBox;
	private ExperimentGrid experimentGrid;

	private TextArea getObservationTextArea;

	public ExperimentsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupTabPanel();
		setupExperimentListPanel();
		setupSearchBox();
		setupGetObservationTextArea();

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
			WrapperUtils.wrapSizeFullVertical(
					WrapperUtils.wrapWidthFullRightHorizontal(searchBox),
					archivesTabPanel,
					experimentGrid),
			WrapperUtils.wrapSizeFullVertical(
					getObservationTextArea
			),
			70);
	}

	private void setupGetObservationTextArea() {
		getObservationTextArea = new TextArea("getObservations");
		getObservationTextArea.setSizeFull();
	}

	private void setupSearchBox() {
		searchBox = new ExperimentSearchBox(experimentGrid, () -> getExperiments());
	}

	private void setupTabPanel() {
		archivesTabPanel = new ArchivesTabPanel("Experiments",
				() -> UI.getCurrent().navigate(TodoView.class));
	}

	private void setupExperimentListPanel() {
		experimentGrid = new ExperimentGrid();
		experimentGrid.addSelectionListener(selectedExperiment ->
				UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(selectedExperiment.getFirstSelectedItem().get())));
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("Back to Models", click ->
						ExceptionWrapperUtils.handleButtonClicked(() -> {
							UI.getCurrent().navigate(ModelsView.class, modelDAO.getProjectIdForModel(modelId));
						})),
				new Button("New Experiment", click ->
						NotificationUtils.showTodoNotification()));
//						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("EXPERIMENTS");
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		experiments = experimentRepository.getExperimentsForModel(modelId);

		if(experiments == null || experiments.isEmpty())
			throw new InvalidDataException("Attempted to access Experiments for Model: " + modelId);

		experimentGrid.setItems(experiments);
	}

	@Override
	public void setParameter(BeforeEvent event, Long modelId)
	{
		this.modelId = modelId;
	}
}
