package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value="dashboard", layout = MainLayout.class)
public class DashboardView extends PathMindDefaultView
{
	@Autowired
	private PolicyDAO policyDAO;

	private Grid<Policy> policyGrid;

	public DashboardView()
	{
		super();
	}

	protected Component getMainContent()
	{
		policyGrid = new Grid<>();

		policyGrid.addColumn(policy -> policy.getRun().getStatusEnum())
				.setHeader("Status")
				.setSortable(true);
		policyGrid.addColumn(policy -> policy.getProject().getName())
				.setHeader("Project")
				.setSortable(true);
		policyGrid.addColumn(policy -> policy.getModel().getName())
				.setHeader("Model")
				.setSortable(true);
		policyGrid.addColumn(policy -> policy.getExperiment().getName())
				.setHeader("Experiment")
				.setSortable(true);
		policyGrid.addColumn(policy -> policy.getRun().getRunTypeEnum())
				.setHeader("Run Type")
				.setSortable(true);
		// TODO -> For now it's hardcoded as DQN since that's the only option.
		policyGrid.addColumn(policy -> Algorithm.DQN)
				.setHeader("Algorithm")
				.setSortable(true);
		policyGrid.addColumn(policy -> DateAndTimeUtils.formatTime(RunUtils.getElapsedTime(policy.getRun())))
				.setHeader("Duration")
				.setSortable(true);
		policyGrid.addColumn(
				new LocalDateTimeRenderer<>(policy -> policy.getRun().getStoppedAt(), DateAndTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Completed")
				.setSortable(true);

		policyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		policyGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedPolicy ->
						NotificationUtils.showTodoNotification("Where do we go when we click on this?")));
//						UI.getCurrent().navigate(RunView.class, selectedPolicy.getId())));

		// TODO -> CSS styles
		policyGrid.setWidth("80%");
		policyGrid.setMaxHeight("500px");
		policyGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		HorizontalLayout gridWrapper = WrapperUtils.wrapSizeFullCenterHorizontal(policyGrid);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("New Project", click ->
						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("DASHBOARD");
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) {
		policyGrid.setItems(policyDAO.getPoliciesForUser(SecurityUtils.getUserId()));
	}
}
