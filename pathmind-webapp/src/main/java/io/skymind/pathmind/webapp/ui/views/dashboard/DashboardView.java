package io.skymind.pathmind.webapp.ui.views.dashboard;

import java.util.List;

import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.Experiment;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.dashboard.components.DashboardLine;
import io.skymind.pathmind.webapp.ui.views.dashboard.components.EmptyDashboardPlaceholder;
import io.skymind.pathmind.webapp.ui.views.dashboard.dataprovider.DashboardDataProvider;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.Stage;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

@Route(value= Routes.DASHBOARD_URL, layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView implements RunUpdateSubscriber
{
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

    private Grid<DashboardItem> dashboardGrid;

    private EmptyDashboardPlaceholder placeholder;

    private HorizontalLayout newProjectButtonWrapper;

    private ScreenTitlePanel titlePanel = new ScreenTitlePanel("Dashboard");

    private long loggedUserId;

    public DashboardView(){
        super();
        addClassName("dashboard-view");
    }

    protected Component getMainContent(){
        newProjectButtonWrapper = WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton());
        placeholder = new EmptyDashboardPlaceholder();
        setupDashboardGrid();

        VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
            placeholder,
            dashboardGrid,
            newProjectButtonWrapper);

        return gridWrapper;
    }

    private void setupDashboardGrid()
    {
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
            return new DashboardLine(item, itm -> navigateFromDashboard(itm), itm -> archiveItem(itm));
        });
        dashboardGrid.setSelectionMode(SelectionMode.NONE);
        dashboardGrid.setPageSize(10);
    }

    private void navigateFromDashboard(DashboardItem item) {
        Stage stage = DashboardUtils.calculateStage(item);
        switch (stage) {
            case SetUpSimulation :
                getUI().ifPresent(ui -> {
                    if (item.getModel() != null && item.getModel().isDraft()) {
                        ui.navigate(UploadModelView.class, UploadModelView.createResumeUploadTarget(item.getProject(), item.getModel()));
                    }
                    else {
                        ui.navigate(UploadModelView.class, String.valueOf(item.getProject().getId()));
                    }
                });
                break;
            case WriteRewardFunction:
                if (item.getExperiment() == null) {
                    getUI().ifPresent(ui -> ExperimentViewNavigationUtils.createAndNavigateToNewExperiment(ui, experimentDAO, item.getModel().getId()));
                } else {
                    getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, item.getExperiment().getId()));
                }
                break;
            default :
                getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, item.getExperiment().getId()));
                break;
        }
    }

    private void archiveItem(DashboardItem item) {
        Stage stage = DashboardUtils.calculateStage(item);
        switch (stage) {
            case SetUpSimulation :
                if (item.getModel() == null) {
                    archiveProject(item);
                }
                else {
                    archiveModel(item);
                }
                break;
            case WriteRewardFunction:
                if (item.getExperiment() == null) {
                    archiveModel(item);
                }
                else {
                    archiveExperiment(item);
                }
                break;
            default :
                archiveExperiment(item);
                break;
        }
    }

    private void archiveExperiment(DashboardItem item) {
        ConfirmationUtils.archive("this experiment", () -> {
            experimentDAO.archive(item.getExperiment().getId(), true);
            dataProvider.refreshAll();
        });
    }

    private void archiveModel(DashboardItem item) {
        ConfirmationUtils.archive("this model", () -> {
            modelDAO.archive(item.getModel().getId(), true);
            dataProvider.refreshAll();
        });
    }

    private void archiveProject(DashboardItem item) {
        ConfirmationUtils.archive("this project", () -> {
            projectDAO.archive(item.getProject().getId(), true);
            dataProvider.refreshAll();
        });
    }

    @Override
    protected Component getTitlePanel() {
        return titlePanel;
    }

    @Override
    protected void initLoadData() throws InvalidDataException {
        loggedUserId = SecurityUtils.getUserId();
    }

    @Override
    protected void initScreen(BeforeEnterEvent event) {
        boolean emptyDashboard = dataProvider.isEmpty();
        placeholder.setVisible(emptyDashboard);
        titlePanel.setVisible(!emptyDashboard);
        dashboardGrid.setVisible(!emptyDashboard);
        newProjectButtonWrapper.setVisible(!emptyDashboard);
        VaadinDateAndTimeUtils.withUserTimeZoneId(event.getUI(), timeZoneId -> {
            // dashboardGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting data provider
            dashboardGrid.setDataProvider(dataProvider);
        });
    }

    @Override
     protected void onAttach(AttachEvent attachEvent) {
         EventBus.subscribe(this);
     }

     @Override
     protected void onDetach(DetachEvent detachEvent) {
         EventBus.unsubscribe(this);
     }

     @Override
     public void handleBusEvent(RunUpdateBusEvent event) {
         PushUtils.push(this, () -> dataProvider.refreshItemByExperiment(event.getRun().getExperimentId()));
     }

     @Override
     public boolean filterBusEvent(RunUpdateBusEvent event) {
         return event.getRun().getProject().getPathmindUserId() == loggedUserId;
     }

    @Override
    public boolean isAttached() {
        return getUI().isPresent();
    }
}
