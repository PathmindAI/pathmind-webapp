package io.skymind.pathmind.ui.views.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.dashboard.components.DashboardSearchBox;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value="dashboard", layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView
{
	@Autowired
	private PolicyDAO policyDAO;

	private DashboardSearchBox searchBox;
	private Grid<Policy> dashboardGrid;

	private List<Policy> policies;

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
				dashboardGrid);

		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	private void setupSearchBox() {
		searchBox = new DashboardSearchBox(dashboardGrid, () -> getPolicies());
	}

	private void setupDashboardGrid()
	{
		dashboardGrid = new Grid<>();

		dashboardGrid.addColumn(policy -> policy.getRun().getStatusEnum())
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
		// TODO -> For now it's hardcoded as DQN since that's the only option.
		dashboardGrid.addColumn(Policy::getAlgorithm)
				.setHeader("Algorithm")
				.setSortable(true);
		dashboardGrid.addColumn(policy -> PolicyUtils.getDuration(policy))
				.setHeader("Duration")
				.setSortable(true);
		dashboardGrid.addColumn(new LocalDateTimeRenderer<>(policy -> policy.getRun().getStoppedAt(), DateAndTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Completed")
				.setSortable(true);

		// TODO -> CSS styles
		dashboardGrid.setWidthFull();
		dashboardGrid.setMaxHeight("500px");
		dashboardGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		dashboardGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		dashboardGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedPolicy ->
						UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(selectedPolicy))));
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
