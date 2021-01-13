package io.skymind.pathmind.webapp.ui.views.project.components;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.components.atoms.StatusIcon;

public class ExperimentGrid extends Grid<Experiment> {
    public ExperimentGrid(ExperimentDAO experimentDAO, PolicyDAO policyDAO, List<RewardVariable> rewardVariables) {
        addComponentColumn(experiment -> new FavoriteStar(experiment.isFavorite(), newIsFavorite -> {
            ExperimentUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite);
            Experiment refreshedExperiment = experiment;
            experiment.setFavorite(newIsFavorite);
            getDataProvider().refreshItem(refreshedExperiment);
        }))
                .setHeader(new Icon(VaadinIcon.STAR))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);
        addColumn(TemplateRenderer.<Experiment>of("[[item.name]] <tag-label size='small' text='[[item.draft]]'></tag-label>")
                .withProperty("name", Experiment::getName)
                .withProperty("draft", experiment -> experiment.isDraft() ? "Draft" : ""))
                .setComparator(Comparator.comparingLong(experiment -> Long.parseLong(experiment.getName())))
                .setHeader("#")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);
        Grid.Column<Experiment> createdColumn = addComponentColumn(experiment -> 
                new DatetimeDisplay(experiment.getDateCreated())
        )
                .setComparator(Comparator.comparing(Experiment::getDateCreated))
                .setHeader("Created")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setAutoWidth(true)
                .setResizable(true);
        addComponentColumn(experiment -> new StatusIcon(experiment))
                .setHeader("Status")
                .setComparator(Comparator.comparing(Experiment::getTrainingStatus))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortable(true);
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
        addColumn(experiment -> {
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
        addItemClickListener(event -> ExperimentUtils.navigateToExperiment(getUI(), event.getItem()));
    }
}
