package io.skymind.pathmind.webapp.ui.views.project.components;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
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

    private Map<String, Column<Experiment>> additionalColumnList = new LinkedHashMap<>();

    private Map<String, Column<Experiment>> columnList = new LinkedHashMap<>();

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
        Grid.Column<Experiment> rewardFunctionColumn = addComponentColumn(experiment -> new CodeViewer(experiment, false, false))
                .setClassNameGenerator(column -> "grid-reward-fn-column")
                .setHeader("Reward Function")
                .setFlexGrow(1)
                .setResizable(true)
                .setSortable(false);
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

    public Map<String, Column<Experiment>> getColumnList() {
        return columnList;
    }

    public Map<String, Column<Experiment>> getAdditionalColumnList() {
        return additionalColumnList;
    }

    public void addAdditionalColumn(RewardVariable rewardVar) {
        String rewardVariableName = rewardVar.getName();
        int rewardVarIndex = rewardVar.getArrayIndex();
        if (additionalColumnList.get(rewardVariableName) == null) {
            // there's no way to get the values of a particular grid column
            // because vaadin grid is designed to deal with a large number of rows
            // need to get the list from the data source and then compare
            Grid.Column<Experiment> newColumn = addComponentColumn(experiment -> {
                        Span columnSpan = new Span();
                        Policy bestPolicy = experiment.getBestPolicy();
                        if (bestPolicy != null) {
                            System.out.println(bestPolicy.getMetricDisplayValues().get(rewardVarIndex));
                            columnSpan.add(bestPolicy.getMetricDisplayValues().get(rewardVarIndex));
                        } else {
                            columnSpan.add("—");
                        }
                        return columnSpan;
                    })
                    .setSortProperty("reward_var_"+Integer.toString(rewardVarIndex))
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
