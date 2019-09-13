package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.ui.components.grid.GridButtonFactory;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.console.ConsoleView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.utils.DateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class ExperimentListPanel extends VerticalLayout
{
	private Logger log = LogManager.getLogger(ExperimentListPanel.class);

	private Grid<Experiment> grid = new Grid<>();

	public ExperimentListPanel()
	{
		add(
				getTitleBar(),
				getRecentExperimentsGrid()
		);
	}

	private HorizontalLayout getTitleBar() {
		return WrapperUtils.wrapLeftAndRightAligned(
				new H3("Experiments"),
				new TextField("Search")
		);
	}

	private Grid<Experiment> getRecentExperimentsGrid() {
		grid.addColumn(Experiment::getDate)
				.setHeader("Completed")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(experiment -> "#" + experiment.getModelId())
				.setHeader("Model")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Experiment::getName)
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Experiment::getRunTypeEnum)
				.setHeader("Run Type")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(experiment -> DateTimeUtils.formatTime(ExperimentUtils.getElapsedTime(experiment)))
				.setHeader("Duration")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Experiment::getScore)
				.setHeader("Score")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(getAdditionalRunButtonRenderer())
				.setHeader("Additional Run")
				.setFlexGrow(0)
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
		grid.addColumn(getConsoleOutputButtonRenderer())
				.setHeader("Console Output")
				.setFlexGrow(0)
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
		grid.addColumn(getEditRewardFunctionsButtonRenderer())
				.setHeader("Edit Experiment")
				.setFlexGrow(0)
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
		grid.addColumn(getExportPolicyButtonRenderer())
				.setHeader("Export Policy")
				.setFlexGrow(0)
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);

		grid.getElement().getStyle().set("padding-top", "20px");
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		return grid;
	}

	public void addSelectionListener(Consumer<Experiment> consumer) {
		grid.addSelectionListener(selectExperiment ->
			consumer.accept(selectExperiment.getFirstSelectedItem().get()));
	}

	public void setExperiments(List<Experiment> experiments) {
		grid.setItems(experiments);
	}

	public void selectExperiment(Experiment experiment) {
		grid.select(experiment);
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getAdditionalRunButtonRenderer() {
		return GridButtonFactory.getGridButtonRenderer(experiment -> {
			// TODO -> Implement
			log.info("Additional Run clicked");
		});
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getConsoleOutputButtonRenderer() {
		return GridButtonFactory.getGridButtonRenderer(experiment -> {
			UI.getCurrent().navigate(ConsoleView.class, experiment.getId());
		});
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getEditRewardFunctionsButtonRenderer() {
		return GridButtonFactory.getGridButtonRenderer(experiment -> {
			UI.getCurrent().navigate(ExperimentView.class, experiment.getId());
		});
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getExportPolicyButtonRenderer() {
		return GridButtonFactory.getGridButtonRenderer(experiment -> {
			// TODO -> Implement
			log.info("Export Policy clicked");
		});
	}
}
