package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ModelRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="experiments", layout = MainLayout.class)
public class ExperimentsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ExperimentRepository experimentRepository;

	private long modelId;

	private Grid<Experiment> experimentGrid;

	public ExperimentsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		experimentGrid = new Grid<>();

		experimentGrid.addColumn(Experiment::getDate)
				.setHeader("Completed")
				.setAutoWidth(true)
				.setSortable(true);
		experimentGrid.addColumn(experiment -> "#" + experiment.getModelId())
				.setHeader("Model")
				.setAutoWidth(true)
				.setSortable(true);
		experimentGrid.addColumn(Experiment::getName)
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setSortable(true);
		experimentGrid.addColumn(Experiment::getRunTypeEnum)
				.setHeader("Run Type")
				.setAutoWidth(true)
				.setSortable(true);
		experimentGrid.addColumn(experiment -> DateTimeUtils.formatTime(ExperimentUtils.getElapsedTime(experiment)))
				.setHeader("Duration")
				.setAutoWidth(true)
				.setSortable(true);
		experimentGrid.addColumn(Experiment::getScore)
				.setHeader("Score")
				.setAutoWidth(true)
				.setSortable(true);
//		experimentGrid.addColumn(getAdditionalRunButtonRenderer())
//				.setHeader("Additional Run")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
//		experimentGrid.addColumn(getConsoleOutputButtonRenderer())
//				.setHeader("Console Output")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
//		experimentGrid.addColumn(getEditRewardFunctionsButtonRenderer())
//				.setHeader("Edit Experiment")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
//		experimentGrid.addColumn(getExportPolicyButtonRenderer())
//				.setHeader("Export Policy")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);

		experimentGrid.getElement().getStyle().set("padding-top", "20px");
		experimentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		experimentGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		experimentGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedExperiment ->
						UI.getCurrent().navigate(ExperimentView.class, selectedExperiment.getId())));

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
//		HorizontalLayout gridWrapper = WrapperUtils.wrapSizeFullCenterHorizontal(experimentGrid);
//		gridWrapper.getElement().getStyle().set("padding-top", "100px");
//		return gridWrapper;
		return experimentGrid;
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("New Experiment", click ->
						NotificationUtils.showTodoNotification()));
//						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("EXPERIMENTS");
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) {
		experimentGrid.setItems(experimentRepository.getExperimentsForModel(modelId));
	}

	@Override
	public void setParameter(BeforeEvent event, Long modelId)
	{
		this.modelId = modelId;
	}
}
