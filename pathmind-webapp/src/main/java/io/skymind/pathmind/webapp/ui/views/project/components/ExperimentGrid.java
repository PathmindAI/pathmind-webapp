package io.skymind.pathmind.webapp.ui.views.project.components;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.flow.server.Command;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.components.atoms.StatusIcon;
import io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer.CodeViewer;
import org.apache.commons.collections4.CollectionUtils;

public class ExperimentGrid extends Grid<Experiment> {

    private Map<String, Column> additionalColumnList = new LinkedHashMap<>();

    private Map<String, Column> columnList = new LinkedHashMap<>();

    public ExperimentGrid(ExperimentDAO experimentDAO, PolicyDAO policyDAO, List<RewardVariable> rewardVariables) {
        Grid.Column<Experiment> favoriteColumn = addComponentColumn(experiment -> new FavoriteStar(experiment.isFavorite(), newIsFavorite -> {
            ExperimentGuiUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite);
        }))
                .setHeader(new Icon(VaadinIcon.STAR))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);
        Grid.Column<Experiment> nameColumn = addColumn(TemplateRenderer.<Experiment>of("[[item.name]] <tag-label size='small' text='[[item.draft]]'></tag-label>")
                .withProperty("name", Experiment::getName)
                .withProperty("draft", experiment -> experiment.isDraft() ? "Draft" : ""))
                .setSortProperty("name")
                .setHeader("#")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);
        Grid.Column<Experiment> createdColumn = addComponentColumn(experiment -> 
                new DatetimeDisplay(experiment.getDateCreated())
        )
                .setSortProperty("date_created")
                .setHeader("Created")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setAutoWidth(true)
                .setResizable(true);
        Grid.Column<Experiment> statusColumn = addComponentColumn(experiment -> new StatusIcon(experiment))
                .setHeader("Status")
                .setSortProperty("training_status")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);
        Grid.Column<Experiment> selectedObsColumn = addColumn(experiment ->
                CollectionUtils.emptyIfNull(experiment.getSelectedObservations()).stream().map(Observation::getVariable).collect(Collectors.joining(", ")))
                .setHeader("Selected Observations")
                .setWidth("16rem")
                .setFlexGrow(0)
                .setResizable(true)
                .setSortable(false);
        // addComponentColumn(experiment -> {
        //             Span goalIcons = new Span();
        //             String successClassName = "success-text";
        //             if (experiment.isHasGoals() && !experiment.isDraft()) {
        //                 // Get best policy
        //                 List<Policy> policies = policyDAO.getPoliciesForExperiment(experiment.getId());

        //                 if (policies != null && !policies.isEmpty()) {
        //                     // TODO: since we are hiding the feature, this is not dealt with for now.
        //                     // We'll have to add code here if we show the Goals feature again
        //                 }
        //             }
        //             goalIcons.setText("—");
        //             return goalIcons;
        //         })
        // 		.setComparator(Comparator.comparing(Experiment::isGoalsReached))
        //         .setHeader("Goals Reached")
        //         .setAutoWidth(true)
        //         .setFlexGrow(0)
        //         .setResizable(true)
        //         .setSortable(true);
        Grid.Column<Experiment> rewardFunctionColumn = addComponentColumn(experiment -> new CodeViewer(experiment, false, false))
                .setClassNameGenerator(column -> "grid-reward-fn-column")
                .setHeader("Reward Function")
                .setFlexGrow(1)
                .setResizable(true)
                .setSortable(false);

        // Sort by created by default
        sort(Arrays.asList(new GridSortOrder<>(createdColumn, SortDirection.DESCENDING)));
        addItemClickListener(event -> ExperimentGuiUtils.navigateToExperiment(getUI(), event.getItem()));
        setColumnReorderingAllowed(true);
        Grid.Column<Experiment> notesColumn = addColumn(experiment -> {
            String userNotes = experiment.getUserNotes();
            return userNotes.isEmpty() ? "—" : userNotes;
        })
                .setClassNameGenerator(column -> "grid-notes-column")
                .setHeader("Notes")
                .setFlexGrow(1)
                .setResizable(true)
                .setSortable(false);

        // Sort by created by default
        sort(Arrays.asList(new GridSortOrder<>(createdColumn, SortDirection.DESCENDING)));
        addItemClickListener(event -> ExperimentGuiUtils.navigateToExperiment(getUI(), event.getItem()));
        setColumnReorderingAllowed(true);

        columnList.put("Favorite", favoriteColumn);
        columnList.put("Id #", nameColumn);
        columnList.put("Created", createdColumn);
        columnList.put("Status", statusColumn);
        columnList.put("Selected Observations", selectedObsColumn);
        columnList.put("Reward Function", rewardFunctionColumn);
        columnList.put("Notes", notesColumn);
    }

    public Map<String, Column> getColumnList() {
        return columnList;
    }

    public Map<String, Column> getAdditionalColumnList() {
        return additionalColumnList;
    }

    public void addAdditionalColumn(RewardVariable rewardVar) {
        String rewardVariableName = rewardVar.getName();
        int rewardVarIndex = rewardVar.getArrayIndex();
        if (additionalColumnList.get(rewardVariableName) == null) {
            Grid.Column<Experiment> newColumn = addColumn(experiment -> {
                        if (experiment.getBestPolicy() != null) {
                            Policy bestPolicy = experiment.getBestPolicy();
                            // First conditional value is with uncertainty, second value is without uncertainty
                            return bestPolicy.getUncertainty() != null && !bestPolicy.getUncertainty().isEmpty()
                                    ? bestPolicy.getUncertainty().get(rewardVarIndex)
                                    : PathmindNumberUtils.formatNumber(bestPolicy.getSimulationMetrics().get(rewardVarIndex));
                        }
                        return "—";
                    })
                    .setSortProperty(Integer.toString(rewardVarIndex))
                    .setHeader(rewardVariableName)
                    .setAutoWidth(true)
                    .setFlexGrow(0)
                    .setResizable(true)
                    .setSortable(true);
            additionalColumnList.put(rewardVariableName, newColumn);
        }
    }

    public void removeAdditionalColumn(RewardVariable rewardVar) {
        Optional.ofNullable(rewardVar)
                .map(RewardVariable::getName)
                .map(additionalColumnList::remove)
                .ifPresent(this::removeColumn);
    }

}
