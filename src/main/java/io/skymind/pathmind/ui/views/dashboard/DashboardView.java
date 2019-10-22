package io.skymind.pathmind.ui.views.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.dashboard.filter.DashboardFilter;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


@Route(value="dashboard", layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView
{
	@Autowired
	private PolicyDAO policyDAO;

	private SearchBox<Policy> searchBox;
	private Grid<Policy> dashboardGrid;

	private List<Policy> policies;

	@Override
	protected boolean isAccessAllowedForUser() {
		// Not needed since the loadData loads the data based on the user's id.
		return true;
	}

	public DashboardView()
	{
		super();
	}

	protected Component getMainContent()
	{
		setupDashboardGrid();
		setupSearchBox();

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		VerticalLayout gridWrapper = WrapperUtils.wrapSizeFullVertical(
				WrapperUtils.wrapWidthFullRightHorizontal(searchBox),
				dashboardGrid,
				WrapperUtils.wrapWidthFullCenterHorizontal(new NewProjectButton()));

		return gridWrapper;
	}

	private void setupSearchBox() {
		searchBox = new SearchBox<Policy>(dashboardGrid, new DashboardFilter());
	}

	private void setupDashboardGrid()
	{
		dashboardGrid = new Grid<>();

		Grid.Column<Policy> statusColumn = dashboardGrid.addColumn(policy -> policy.getRun().getStatusEnum())
				.setHeader("Status")
				.setSortable(true);
		dashboardGrid.addColumn(policy -> policy.getProject().getName())
				.setHeader("Project")
				.setSortable(true);
		dashboardGrid.addColumn(policy -> policy.getModel().getName())
				.setHeader("Model")
				.setSortable(true);
		dashboardGrid.addColumn(policy -> policy.getExperiment().getName())
				.setHeader("Experiment")
				.setSortable(true);
		dashboardGrid.addColumn(policy -> policy.getRun().getRunTypeEnum())
				.setHeader("Run Type")
				.setSortable(true);
		dashboardGrid.addColumn(Policy::getAlgorithm)
				.setHeader("Algorithm")
				.setSortable(true);
		dashboardGrid.addColumn(new NumberRenderer<>(policy -> RunUtils.getElapsedTime(policy.getRun()), DateAndTimeUtils.getElapsedTimeNumberFormat()))
				.setComparator(Comparator.comparing(policy -> RunUtils.getElapsedTime(policy.getRun())))
				.setHeader("Duration")
				.setSortable(true);
		Grid.Column<Policy> startedColumn = dashboardGrid.addColumn(new LocalDateTimeRenderer<>(policy -> PolicyUtils.getRunStartTime(policy), DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(policy -> PolicyUtils.getRunStartTime(policy), Comparator.nullsFirst(Comparator.naturalOrder())))
				.setHeader("Started")
				.setAutoWidth(true)
				.setSortable(true);
		Grid.Column<Policy> completedColumn = dashboardGrid.addColumn(new LocalDateTimeRenderer<>(policy -> policy.getRun().getStoppedAt(), DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
				.setComparator(Comparator.comparing(policy -> policy.getRun().getStoppedAt()))
				.setHeader("Completed")
				.setComparator(getCompletedComparator())
				.setSortable(true);

		dashboardGrid.sort(Arrays.asList(
				new GridSortOrder<Policy>(startedColumn, SortDirection.DESCENDING)));
		dashboardGrid.addItemClickListener(event -> {
			getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(event.getItem())));
		});
	}

	private Comparator<Policy> getCompletedComparator() {
		return (p1, p2) ->  {
			if(p1.getRun() == null || p1.getRun().getStoppedAt() == null)
				return -1;
			if(p2.getRun() == null || p2.getRun().getStoppedAt() == null)
				return 1;
			return p1.getRun().getStoppedAt().compareTo(p2.getRun().getStoppedAt());
		};
	}

	private List<Policy> getPolicies() {
		return policies;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("DASHBOARD");
	}

	@Override
	protected void loadData() throws InvalidDataException {
		// Policies can never be null since it's not a url generated query.
		policies = policyDAO.getPoliciesForUser(SecurityUtils.getUserId());
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) {
		dashboardGrid.setItems(policies);
	}
}
