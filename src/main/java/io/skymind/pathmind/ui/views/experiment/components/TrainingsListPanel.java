package io.skymind.pathmind.ui.views.experiment.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.views.policy.components.PolicySearchBox;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class TrainingsListPanel extends VerticalLayout
{
	private Logger log = LogManager.getLogger(TrainingsListPanel.class);

	private PolicySearchBox searchBox;
	private Grid<Policy> grid;

	private Experiment experiment;
	private ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();

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

	private String getRunStatus(Policy policy) {
		if (policy.getRun().getRunTypeEnum().equals(RunType.DiscoveryRun) && policy.getRun().getStatusEnum().equals(RunStatus.Running)) {
			try {
				return objectMapper.readValue(policy.getProgress(), Progress.class).getStoppedAt() != null ? RunStatus.Completed.name() : RunStatus.Running.name();
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				return null;
			}
		} else {
			return policy.getRun().getStatusEnum().name();
		}
	}

	private void setupGrid()
	{
		grid = new Grid<>();

		// TODO -> DH -> Cases #83, #83, #85, and #86 -> Where do specific columns come from?
		grid.addColumn(policy -> getRunStatus(policy))
				.setHeader("Status")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> {
			if (!RunStatus.Completed.name().equalsIgnoreCase(getRunStatus(policy))) {
				return "--";
			}

			try {
				return objectMapper.readValue(policy.getProgress(), Progress.class).getStoppedAt();
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				return null;
			}
		})
				.setHeader("Completed")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> {
			try {
				return policy.getScores().get(policy.getScores().size() - 1);
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				return null;
			}
		})
				.setHeader("Score")
				.setAutoWidth(true)
				.setSortable(true);

		grid.addColumn(policy -> {
			try {
				return policy.getName().split("_")[2];
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
				return null;
			}
		})
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

	public void addSearchListener(Consumer<List<Policy>> searchConsumer) {
		searchBox.addSearchListener(searchConsumer);
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
			experiment.getPolicies().stream()
					.filter(policy -> policy.getId() == defaultSelectedPolicyId)
					.findAny()
					.ifPresent(policy -> grid.select(policy));
		}
	}
}
