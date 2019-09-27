package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.archive.ArchivesTabPanel;
import io.skymind.pathmind.ui.components.buttons.BackButton;
import io.skymind.pathmind.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
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
	private ExperimentDAO experimentDAO;

	private long modelId;
	private List<Experiment> experiments;

	private ExperimentGrid experimentGrid;

	private TextArea getObservationTextArea;

	public ExperimentsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupExperimentListPanel();
		setupGetObservationTextArea();

		return WrapperUtils.wrapWidthFullCenterVertical(
				WrapperUtils.wrapWidthFullCenterHorizontal(getBackToModelsButton()),
				WrapperUtils.wrapWidthFullRightHorizontal(getSearchBox()),
				getArchivesTabPanel(),
				WrapperUtils.wrapCenterAlignmentFullSplitLayoutHorizontal(
						WrapperUtils.wrapSizeFullVertical(
								experimentGrid),
						WrapperUtils.wrapSizeFullVertical(
								getObservationTextArea
						),70),
				WrapperUtils.wrapWidthFullCenterHorizontal(new NewExperimentButton(experimentDAO, modelId)));
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
	}

	private SearchBox getSearchBox() {
		return new ExperimentSearchBox(experimentGrid, () -> getExperiments());
	}

	private ArchivesTabPanel getArchivesTabPanel() {
		return new ArchivesTabPanel<Experiment>(
				"Experiments",
				experimentGrid,
				this::getExperiments);
	}

	private void setupExperimentListPanel() {
		experimentGrid = new ExperimentGrid();
		experimentGrid.addSelectionListener(selectedExperiment ->
				UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(selectedExperiment.getFirstSelectedItem().get())));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("EXPERIMENTS");
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	@Override
	protected void loadData() throws InvalidDataException {
		experiments = experimentRepository.getExperimentsForModel(modelId);
		if(experiments == null || experiments.isEmpty())
			throw new InvalidDataException("Attempted to access Experiments for Model: " + modelId);
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException {
		experimentGrid.setItems(experiments);
	}

	@Override
	public void setParameter(BeforeEvent event, Long modelId)
	{
		this.modelId = modelId;
	}
}
