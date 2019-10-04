package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.ui.utils.ExperimentViewUtil;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.views.policy.filter.PolicyFilter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class TrainingsListPanel extends VerticalLayout
{
	private SearchBox<Policy> searchBox;
	private Grid<Policy> grid;

	private Experiment experiment;

	private Flux<PathmindBusEvent> consumer;

	public TrainingsListPanel(Flux<PathmindBusEvent> consumer)
	{
		this.consumer = consumer;

		setupGrid();
		setupSearchBox();

		add(getTitleAndSearchBoxBar());
		add(grid);

		// Always force at least one item to be selected.
		((GridSingleSelectionModel<Policy>)grid.getSelectionModel()).setDeselectAllowed(false);

		setSizeFull();
	}

	private void setupGrid()
	{
		grid = new Grid<>();
		grid.addColumn(policy -> ExperimentViewUtil.getRunStatus(policy))
				.setHeader("Status")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> ExperimentViewUtil.getRunCompletedTime(policy))
				.setHeader("Completed")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> ExperimentViewUtil.getLastScore(policy))
				.setHeader("Score")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> ExperimentViewUtil.getParsedPolicyName(policy))
				.setHeader("Policy")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> policy.getRun().getRunTypeEnum())
				.setHeader("Run Type")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> ProgressInterpreter.interpretKey(policy.getName()).getAlgorithm())
				.setHeader("Algorithm")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> ProgressInterpreter.interpretKey(policy.getName()).getHyperParameters().toString().replaceAll("(\\{|\\})", ""))
				.setHeader("Notes")
				.setAutoWidth(true)
				.setSortable(true);
	}

	public void addSelectionListener(Consumer<Policy> consumer) {
		grid.addSelectionListener(selectionPolicy ->
				selectionPolicy.getFirstSelectedItem().ifPresent(p -> consumer.accept(p)));
	}

	private HorizontalLayout getTitleAndSearchBoxBar() {
		return GuiUtils.getTitleAndSearchBoxBar(
				"Trainings",
				searchBox);
	}

	private void setupSearchBox() {
		searchBox = new SearchBox(grid, new PolicyFilter(), true);
	}

	public SearchBox getSearchBox() {
		return searchBox;
	}

	private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnExperiment(
				consumer,
				() -> getExperiment(),
				updatedPolicy -> PushUtils.push(ui, () -> updatedGrid(updatedPolicy)));
	}

	private void updatedGrid(Policy updatedPolicy)
	{
		experiment.getPolicies().stream()
				.filter(policy ->policy.getId() == updatedPolicy.getId())
				.findAny()
				.ifPresentOrElse(
						policy -> {
							replacePolicy(updatedPolicy);
							grid.getDataProvider().refreshItem(updatedPolicy);
						},
						() -> {
							experiment.getPolicies().add(updatedPolicy);
							// We need to refreshAll because otherwise the grid does not see the new row.
							grid.getDataProvider().refreshAll();
						});

		// TODO -> Re-select same policy ?
		// TODO -> refilter according to the search box. ?
	}

	private void replacePolicy(Policy updatedPolicy) {
		experiment.setPolicies(experiment.getPolicies().stream()
				.map(policy -> policy.getId() == updatedPolicy.getId() ? updatedPolicy : policy)
				.collect(Collectors.toList()));
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void update(Experiment experiment, long defaultSelectedPolicyId)
	{
		this.experiment = experiment;

		grid.setDataProvider(new ListDataProvider<Policy>(experiment.getPolicies()));

		if(!experiment.getPolicies().isEmpty() && defaultSelectedPolicyId < 0) {
			grid.select(experiment.getPolicies().get(0));
		} else {
			experiment.getPolicies().stream()
					.filter(policy -> policy.getId() == defaultSelectedPolicyId)
					.findAny()
					.ifPresent(policy -> grid.select(policy));
		}

		subscribeToEventBus(UI.getCurrent(), consumer);
	}
}
