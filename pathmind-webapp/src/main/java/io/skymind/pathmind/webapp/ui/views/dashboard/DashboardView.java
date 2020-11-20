package io.skymind.pathmind.webapp.ui.views.dashboard;

import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.dashboard.components.DashboardLine;
import io.skymind.pathmind.webapp.ui.views.dashboard.components.EmptyDashboardPlaceholder;
import io.skymind.pathmind.webapp.ui.views.dashboard.dataprovider.DashboardDataProvider;
import io.skymind.pathmind.webapp.ui.views.dashboard.subscribers.main.DashboardViewRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.Stage;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.DASHBOARD_URL, layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView {
    @Autowired
    private DashboardDataProvider dataProvider;
    @Autowired
    private ExperimentDAO experimentDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private RunDAO runDAO;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private Grid<DashboardItem> dashboardGrid;

    private EmptyDashboardPlaceholder placeholder;

    private HorizontalLayout newProjectButtonWrapper;

    private Span title;

    private long loggedUserId;

    public DashboardView() {
        super();
        addClassName("dashboard-view");
    }

    protected Component getMainContent() {
        title = LabelFactory.createLabel("Recent", CssPathmindStyles.SECTION_TITLE_LABEL);
        newProjectButtonWrapper = WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton());
        placeholder = new EmptyDashboardPlaceholder(segmentIntegrator);
        setupDashboardGrid();

        VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
                title,
                placeholder,
                dashboardGrid,
                newProjectButtonWrapper);
        gridWrapper.setPadding(false);

        return gridWrapper;
    }

    private void setupDashboardGrid() {
        dashboardGrid = new Grid<>();
        dashboardGrid.addClassName("dashboard");
        dashboardGrid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER);
        dashboardGrid.addComponentColumn(item -> {
            if (item.getExperiment() != null) {
                Experiment currentExperiment = item.getExperiment();
                List<Run> runsForExperiment = runDAO.getRunsForExperiment(currentExperiment);
                if (runsForExperiment != null) {
                    currentExperiment.setRuns(runsForExperiment);
                }
            }
            return new DashboardLine(experimentDAO, item, itm -> archiveItem(itm));
        });
        dashboardGrid.setSelectionMode(SelectionMode.NONE);
        dashboardGrid.setPageSize(10);
    }

    private void archiveItem(DashboardItem item) {
        Stage stage = DashboardUtils.calculateStage(item);
        switch (stage) {
            case SetUpSimulation:
                if (item.getModel() == null) {
                    archiveProject(item);
                } else {
                    archiveModel(item);
                }
                break;
            case WriteRewardFunction:
                if (item.getExperiment() == null) {
                    archiveModel(item);
                } else {
                    archiveExperiment(item);
                }
                break;
            default:
                archiveExperiment(item);
                break;
        }
    }

    private void archiveExperiment(DashboardItem item) {
        ConfirmationUtils.archive("this experiment", () -> {
            ExperimentUtils.archiveExperiment(experimentDAO, item.getExperiment(), true);
            segmentIntegrator.archived(Experiment.class, true);
            dataProvider.refreshAll();
        });
    }

    private void archiveModel(DashboardItem item) {
        ConfirmationUtils.archive("this model", () -> {
            modelDAO.archive(item.getModel().getId(), true);
            segmentIntegrator.archived(Model.class, true);
            dataProvider.refreshAll();
        });
    }

    private void archiveProject(DashboardItem item) {
        ConfirmationUtils.archive("this project", () -> {
            projectDAO.archive(item.getProject().getId(), true);
            segmentIntegrator.archived(Project.class, true);
            dataProvider.refreshAll();
        });
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        loggedUserId = SecurityUtils.getUserId();
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        boolean emptyDashboard = dataProvider.isEmpty();
        title.setVisible(!emptyDashboard);
        placeholder.setVisible(emptyDashboard);
        dashboardGrid.setVisible(!emptyDashboard);
        newProjectButtonWrapper.setVisible(!emptyDashboard);
        VaadinDateAndTimeUtils.withUserTimeZoneId(event.getUI(), timeZoneId -> {
            // dashboardGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting data provider
            dashboardGrid.setDataProvider(dataProvider);
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this, () -> getUI(),
                new DashboardViewRunUpdateSubscriber(this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public void refreshExperiment(long experimentId) {
        dataProvider.refreshItemByExperiment(experimentId);
    }

    public long getLoggedUserId() {
        return loggedUserId;
    }
}
