package io.skymind.pathmind.ui.views.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.db.ModelRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="models", layout = MainLayout.class)
public class ModelsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ModelRepository modelRepository;

	private long projectId;

	private Grid<Model> modelGrid;

	public ModelsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		modelGrid = new Grid<>();

		modelGrid.addColumn(Model::getName)
				.setHeader("Name")
				.setSortable(true);
//				.setWidth("275px");
		modelGrid.addColumn(
				new LocalDateRenderer<>(Model::getDateCreated, DateTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Date Created")
				.setSortable(true);
//				.setWidth("275px");
		modelGrid.addColumn(
				new LocalDateRenderer<>(Model::getLastActivityDate, DateTimeUtils.STANDARD_DATE_TIME_FOMATTER))
				.setHeader("Last Activity")
				.setSortable(true);
//				.setWidth("275px");

		modelGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		modelGrid.addSelectionListener(event ->
				event.getFirstSelectedItem().ifPresent(selectedModel ->
						UI.getCurrent().navigate(ExperimentsView.class, selectedModel.getId())));

		modelGrid.setWidth("700px");
		modelGrid.setMaxWidth("700px");
		modelGrid.setMaxHeight("500px");
		modelGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		// BUG -> I didn't have to really investigate but it looks like we may need
		// to do something special to get the full size content in the AppLayout component which
		// is why the table is centered vertically: https://github.com/vaadin/vaadin-app-layout/issues/51
		// Hence the workaround below:
		HorizontalLayout gridWrapper = WrapperUtils.wrapSizeFullCenterHorizontal(modelGrid);
		gridWrapper.getElement().getStyle().set("padding-top", "100px");
		return gridWrapper;
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("New Model", click ->
						NotificationUtils.showTodoNotification()));
//						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("MODELS");
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) {
		modelGrid.setItems(modelRepository.getModelsForProject(projectId));
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId)
	{
		this.projectId = projectId;
	}
}
