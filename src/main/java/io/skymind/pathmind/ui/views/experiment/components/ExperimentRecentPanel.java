package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.ui.components.grid.GridButtonFactory;
import io.skymind.pathmind.utils.UIConstants;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ExperimentRecentPanel extends VerticalLayout
{
	private Logger log = LogManager.getLogger(ExperimentRecentPanel.class);

	private Grid<Experiment> grid = new Grid<>();

	// TODO -> Hardcoded value to get fake experiment data.
	public ExperimentRecentPanel(List<Experiment> experiments)
	{
		add(
				getTitleBar(),
				getRecentExperimentsGrid(experiments)
		);
	}

	// TODO -> Need to decide between styled labels and header elements.
	private Component getTitleBar() {
		return WrapperUtils.wrapLeftAndRightAligned(
				new H3("Recent Experiments"),
				new TextField("Search")
		);
	}

	private Grid<Experiment> getRecentExperimentsGrid(List<Experiment> experiments) {
		grid.addColumn(Experiment::getName)
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Experiment::getDate)
				.setHeader("Date")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Experiment::getRunType)
				.setHeader("Run Type")
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
				.setHeader("Edit Reward Functions")
				.setFlexGrow(0)
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
		grid.addColumn(getExportPolicyButtonRenderer())
				.setHeader("Export Policy")
				.setFlexGrow(0)
				.setWidth(UIConstants.GRID_BUTTON_WIDTH);

		grid.getElement().getStyle().set("padding-top", "20px");
		grid.setItems(experiments);
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		return grid;
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getAdditionalRunButtonRenderer() {
		return getGridButtonRenderer(click -> {
					// TODO -> Implement
					log.info("Additional Run clicked");
				});
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getConsoleOutputButtonRenderer() {
		return getGridButtonRenderer(click -> {
					// TODO -> Implement
					log.info("Console output clicked");
				});
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getEditRewardFunctionsButtonRenderer() {
		return getGridButtonRenderer(click -> {
					// TODO -> Implement
					log.info("Edit Reward Functions clicked");
				});
	}

	private ComponentRenderer<HorizontalLayout, Experiment> getExportPolicyButtonRenderer() {
		return getGridButtonRenderer(click -> {
					// TODO -> Implement
					log.info("Export Policy clicked");
				});
	}

	// TODO -> Should be promoted to GridButtonFactory so that we use the same code but I'm skipping it due to limited time for now.
	private ComponentRenderer<HorizontalLayout, Experiment> getGridButtonRenderer(ComponentEventListener<ClickEvent<Button>> clickListener) {
		return new ComponentRenderer<>(experiment -> {
			return GridButtonFactory.getGridButton(">", clickListener);
		});
	}
}
