package io.skymind.pathmind.ui.views.project.components.panels;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.ExperimentUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;

@Component
public class ExperimentListPanel extends VerticalLayout
{
	private Logger log = LogManager.getLogger(ExperimentListPanel.class);

	private Grid<Experiment> grid = new Grid<>();

	public ExperimentListPanel() {
		add(getTitleBar(),
			getRecentExperimentsGrid());
	}

	public ExperimentListPanel(Flux<PathmindBusEvent> consumer)
	{
		this();
		subscribeToEventBus(consumer);
	}

	private void subscribeToEventBus(Flux<PathmindBusEvent> consumer) {
		consumer
			.filter(busEvent -> busEvent.isEventType(BusEventType.ExperimentUpdate))
			// TODO -> DATA MODEL -> In case of new experiments for project
//			.filter(busEvent -> ((ExperimentUpdateBusEvent)busEvent).isForProject(project))
			.subscribe(busEvent ->
				update(((ExperimentUpdateBusEvent)busEvent).getExperiment()));

		// TODO -> We may need subscribe to the project as well depending on the requirements and possible
		// changes to a project over time. I don't know enough create a subscriber to make it worthwhile yet.
	}

	public void update(Experiment experiment) {
		// TODO -> Implement.
//		grid.getDataProvider().refreshItem(
	}

	public void update(List<Experiment> experiments) {
		grid.setItems(experiments);
		// TODO -> DATA MODEL
//		grid.setItems(project.getExperiments());
	}

	private HorizontalLayout getTitleBar() {
		return GuiUtils.getTitleAndSearchBoxBar(
				"Experiments",
				new SearchBox());
	}

	private Grid<Experiment> getRecentExperimentsGrid() {
		grid.addColumn(Experiment::getName)
				.setHeader("Experiment")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Experiment::getDateCreated)
				.setHeader("Date Created")
				.setAutoWidth(true)
				.setSortable(true);
//		grid.addColumn(experiment -> "#" + experiment.getModelId())
//				.setHeader("Model")
//				.setAutoWidth(true)
//				.setSortable(true);
		grid.addColumn(experiment -> "Todo")
				.setHeader("Discovery Run")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(experiment -> "Todo")
				.setHeader("Full Run")
				.setAutoWidth(true)
				.setSortable(true);
		// TODO -> Notes no longer exists on the Experiment level
//		grid.addColumn(Experiment::getNotes)
//				.setHeader("Notes")
//				.setAutoWidth(true)
//				.setSortable(true);

//		grid.addColumn(experiment -> DateTimeUtils.formatTime(ExperimentUtils.getElapsedTime(experiment)))
//				.setHeader("Duration")
//				.setAutoWidth(true)
//				.setSortable(true);
//		grid.addColumn(Experiment::getScore)
//				.setHeader("Score")
//				.setAutoWidth(true)
//				.setSortable(true);
//		grid.addColumn(getAdditionalRunButtonRenderer())
//				.setHeader("Additional Run")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
//		grid.addColumn(getConsoleOutputButtonRenderer())
//				.setHeader("Console Output")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
//		grid.addColumn(getEditRewardFunctionsButtonRenderer())
//				.setHeader("Edit Experiment")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);
//		grid.addColumn(getExportPolicyButtonRenderer())
//				.setHeader("Export Policy")
//				.setFlexGrow(0)
//				.setWidth(UIConstants.GRID_BUTTON_WIDTH);

		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.getElement().getStyle().set("padding-top", "20px");
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		return grid;
	}

	public void addSelectionListener(Consumer<Experiment> consumer) {
		grid.addSelectionListener(selectExperiment ->
			consumer.accept(selectExperiment.getFirstSelectedItem().get()));
	}

//	public void setExperiments(List<Experiment> experiments) {
//		grid.setItems(experiments);
//	}

	public void selectExperiment(Experiment experiment) {
		grid.select(experiment);
	}

//	private ComponentRenderer<HorizontalLayout, Experiment> getAdditionalRunButtonRenderer() {
//		return GridButtonFactory.getGridButtonRenderer(experiment -> {
//			// TODO -> Implement
//			log.info("Additional Run clicked");
//		});
//	}
//
//	private ComponentRenderer<HorizontalLayout, Experiment> getConsoleOutputButtonRenderer() {
//		return GridButtonFactory.getGridButtonRenderer(experiment -> {
//			UI.getCurrent().navigate(ConsoleView.class, experiment.getId());
//		});
//	}
//
//	private ComponentRenderer<HorizontalLayout, Experiment> getEditRewardFunctionsButtonRenderer() {
//		return GridButtonFactory.getGridButtonRenderer(experiment -> {
//			UI.getCurrent().navigate(ExperimentView.class, experiment.getId());
//		});
//	}
//
//	private ComponentRenderer<HorizontalLayout, Experiment> getExportPolicyButtonRenderer() {
//		return GridButtonFactory.getGridButtonRenderer(experiment -> {
//			// TODO -> Implement
//			log.info("Export Policy clicked");
//		});
//	}
}
