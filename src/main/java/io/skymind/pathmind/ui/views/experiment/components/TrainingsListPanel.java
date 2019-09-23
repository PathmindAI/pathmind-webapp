package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.NotificationUtils;

import java.util.List;
import java.util.function.Consumer;

public class TrainingsListPanel extends VerticalLayout
{
	private SearchBox searchBox = new SearchBox();
	private Grid<Policy> grid = new Grid<>();

	public TrainingsListPanel()
	{
		add(getTitleAndSearchBoxBar());
		add(getTrainingsGrid());

		setSizeFull();
	}

	private Grid<Policy> getTrainingsGrid()
	{
		// TODO -> Cases #83, #83, #85, and #86 -> Where do specific columns come from?
		NotificationUtils.showTodoNotification("Cases #83, #83, #85, and #86 -> Where do specific columns come from?");

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

		grid.addSelectionListener(selectedPolicy -> {

		});

		return grid;
	}

	public void addSelectionListener(Consumer<Policy> consumer) {
		grid.addSelectionListener(selectionPolicy ->
				consumer.accept(selectionPolicy.getFirstSelectedItem().get()));
	}

	private HorizontalLayout getTitleAndSearchBoxBar() {
		return GuiUtils.getTitleAndSearchBoxBar(
				"Trainings",
				searchBox);
	}

	public void update(List<Policy> policies) {
		grid.setItems(policies);
		if(!policies.isEmpty())
			grid.select(policies.get(0));
	}
}
