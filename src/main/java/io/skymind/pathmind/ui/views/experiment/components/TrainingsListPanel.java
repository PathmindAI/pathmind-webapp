package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.views.policy.components.PolicySearchBox;

import java.util.function.Consumer;

public class TrainingsListPanel extends VerticalLayout
{
	private PolicySearchBox searchBox;
	private Grid<Policy> grid;

	private Experiment experiment;

	public TrainingsListPanel()
	{
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

		// TODO -> Paul -> Cases #83, #83, #85, and #86 -> Where do specific columns come from?
		grid.addColumn(policy -> policy.getRun().getStatusEnum().name())
				.setHeader("Status")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(policy -> "Completed TODO")
				.setHeader("Completed")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(policy -> "Score todo")
				.setHeader("Score")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(Policy::getName)
				.setHeader("Policy")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(policy -> policy.getRun().getRunTypeEnum().name())
				.setHeader("Run Type")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(policy -> Algorithm.DQN)
				.setHeader("Algorithm")
				.setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(policy -> "Notes TODO")
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
		searchBox = new PolicySearchBox(grid, () -> experiment.getPolicies(), true);
	}

	public void update(Experiment experiment, long defaultSelectedPolicyId)
	{
		this.experiment = experiment;

		grid.setItems(experiment.getPolicies());

		if(!experiment.getPolicies().isEmpty() && defaultSelectedPolicyId < 0) {
			grid.select(experiment.getPolicies().get(0));
		} else {
			Policy selectedPolicy = experiment.getPolicies().stream()
					.filter(policy -> policy.getId() == defaultSelectedPolicyId)
					.findAny()
					.get();
			grid.select(selectedPolicy);
		}
	}
}
