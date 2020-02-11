package io.skymind.pathmind.webapp.ui.views.dashboard;

import java.time.LocalDateTime;

import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.dashboard.components.DashboardLine;
import io.skymind.pathmind.webapp.ui.views.dashboard.components.EmptyDashboardPlaceholder;
import io.skymind.pathmind.webapp.ui.views.dashboard.dataprovider.DashboardDataProvider;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.Stage;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.bus.EventBus;
import io.skymind.pathmind.db.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.db.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.db.data.DashboardItem;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.webapp.ui.views.dashboard.utils.DashboardUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;


@Route(value= Routes.DASHBOARD_URL, layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView implements RunUpdateSubscriber
{
	@Autowired
	private DashboardDataProvider dataProvider;

	@Autowired
	private ExperimentDAO experimentDAO;
	
	private Grid<DashboardItem> dashboardGrid;
	
	private EmptyDashboardPlaceholder placeholder;
	
	private long loggedUserId;

	@Override
	protected boolean isAccessAllowedForUser() {
		// Not needed since the loadData loads the data based on the user's id.
		return true;
	}

	public DashboardView(){
		super();
		addClassName("dashboard-view");
	}

	protected Component getMainContent(){
		placeholder = new EmptyDashboardPlaceholder();
		setupDashboardGrid();

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
			placeholder,
			dashboardGrid,
			WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton()));

		return gridWrapper;
	}

	private void setupDashboardGrid()
	{
		dashboardGrid = new Grid<>();
		dashboardGrid.addClassName("dashboard");
		dashboardGrid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER);
		dashboardGrid.addComponentColumn(item -> new DashboardLine(item, itm -> navigateFromDashboard(itm)));
		dashboardGrid.setSelectionMode(SelectionMode.NONE);
		dashboardGrid.setPageSize(10);
	}

	private void navigateFromDashboard(DashboardItem item) {
		Stage stage = DashboardUtils.calculateStage(item);
		switch (stage) {
			case SetUpSimulation :
				getUI().ifPresent(ui -> ui.navigate(UploadModelView.class, item.getProject().getId()));
				break;
			case WriteRewardFunction:
				var experimentId = item.getExperiment() == null ?
						experimentDAO.insertExperiment(item.getModel().getId(), LocalDateTime.now()) : item.getExperiment().getId();
				getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experimentId));
				break;
			default :
				getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, item.getExperiment().getId()));
				break;
		}
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("DASHBOARD");
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		loggedUserId = SecurityUtils.getUserId();
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		boolean emptyDashboard = dataProvider.isEmpty();
		placeholder.setVisible(emptyDashboard);
		dashboardGrid.setVisible(!emptyDashboard);
		VaadinDateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
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
