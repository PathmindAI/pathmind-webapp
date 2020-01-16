package io.skymind.pathmind.ui.views.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.dashboard.components.DashboardItem;
import io.skymind.pathmind.ui.views.dashboard.dataprovider.DashboardDataProvider;
import io.skymind.pathmind.utils.DateAndTimeUtils;


@Route(value= Routes.DASHBOARD_URL, layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView
{
	@Autowired
	private PolicyDAO policyDAO;
	
	@Autowired
	private DashboardDataProvider dataProvider;

	private Grid<Experiment> dashboardGrid;

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
		setupDashboardGrid();

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
			dashboardGrid,
			WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton()));

		return gridWrapper;
	}

	private void setupDashboardGrid()
	{
		dashboardGrid = new Grid<>();
		dashboardGrid.addClassName("dashboard");
		dashboardGrid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER);
		dashboardGrid.addComponentColumn(p -> new DashboardItem(p));
		dashboardGrid.setPageSize(10);
//		dashboardGrid.addItemClickListener(event -> {
//			getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(event.getItem())));
//		});
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("DASHBOARD");
	}

	@Override
	protected void initLoadData() throws InvalidDataException {
		// Do nothing, data is loaded by Dashboard Data Provider
	}

	@Override
	protected void initScreen(BeforeEnterEvent event) {
		DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
			// dashboardGrid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting data provider
			dashboardGrid.setDataProvider(dataProvider);
		});
	}
}
