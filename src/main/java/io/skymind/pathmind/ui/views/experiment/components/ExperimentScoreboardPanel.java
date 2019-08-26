package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.data.MockData;

public class ExperimentScoreboardPanel extends VerticalLayout
{
	private Grid<MockData> grid = new Grid<>();

	public ExperimentScoreboardPanel()
	{
		add(new Label("Title"));
		add(getGrid());
	}

	private Grid getGrid() {
		grid.addColumn(MockData::getName)
				.setHeader("Name")
				.setSortable(true);
		grid.addColumn(MockData::getValue)
				.setHeader("Value")
				.setSortable(true);

		grid.getElement().getStyle().set("padding-top", "20px");
		grid.setItems(MockData.FAKE_DATA);

		return grid;
	}
}
