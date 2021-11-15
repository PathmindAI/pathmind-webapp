package io.skymind.pathmind.webapp.ui.views.policyServer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;

@Route(value = Routes.POLICY_SERVERS, layout = MainLayout.class)
public class PolicyServersView extends PathMindDefaultView {

    @Autowired
    private PolicyServersGridDataProvider policyServersGridDataProvider;

    private Grid<Experiment> policyServersGrid;
    private ViewSection pageWrapper;
    private ConfigurableFilterDataProvider<Experiment, Void, Boolean> dataProvider;
    public PolicyServersView() {
        super();
        setupPolicyServersGrid();
    }
    protected Component getMainContent() {
        setupPolicyServersGrid();
        addClassName("policy-servers-view");
        Span projectsTitle = LabelFactory.createLabel("Policy Servers", CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.TRUNCATED_LABEL);
        pageWrapper = new ViewSection(projectsTitle, policyServersGrid);
        
        return pageWrapper;
    }
    private void setupPolicyServersGrid() {
        policyServersGrid = new Grid<Experiment>();
        policyServersGrid.addThemeName("projects");

        policyServersGrid.addColumn("TODO: Project name")
                .setHeader("Project")
                .setClassNameGenerator(column -> "align-right")
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("models");

        policyServersGrid.addColumn("TODO: Model name")
                .setHeader("Model")
                .setClassNameGenerator(column -> "align-right")
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("models");

        policyServersGrid.addColumn(Experiment::getName)
                .setHeader("Name")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("name");

        policyServersGrid.addComponentColumn(experiment ->
                new DatetimeDisplay(experiment.getDateCreated())
        )
                .setHeader("Created")
                .setClassNameGenerator(column -> "align-right")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("date_created");

        policyServersGrid.addColumn(experiment ->
                experiment.getTrainingStatusEnum().name()
        )
                .setHeader("Status")
                .setClassNameGenerator(column -> "align-right")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true)
                .setSortProperty("status");

        policyServersGrid.addColumn(experiment -> {
            String userNotes = experiment.getUserNotes();
            return userNotes.isEmpty() ? "—" : userNotes;
        })
                .setClassNameGenerator(column -> "grid-notes-column")
                .setHeader("Notes")
                .setResizable(true)
                .setSortable(false);

        policyServersGrid.addItemClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, "" + event.getItem().getId())));
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected void initLoadData(BeforeEnterEvent event) throws InvalidDataException {
    }

    @Override
    protected void initComponents() {
        dataProvider = policyServersGridDataProvider.withConfigurableFilter();
        dataProvider.setFilter(false);
        policyServersGrid.setPageSize(10);
        policyServersGrid.setDataProvider(dataProvider);

        recalculateGridColumnWidth(getUISupplier().get().get().getPage(), policyServersGrid);
    }
}
