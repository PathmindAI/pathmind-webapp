package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.utils.WrapperUtils;

import java.util.List;


public class ProjectChartPanel extends VerticalLayout
{
	// TODO -> I have no idea what these components should be or what they do.
	private Label inputLabel = new Label("Input");
	private Label arrowsLabel = new Label(" > < ");
	private Label outputLabel = new Label("Output");

	private Chart chart = new Chart(ChartType.LINE);
	private ListSeries series = new ListSeries("Score");

	public ProjectChartPanel()
	{
		setupChart();

		add(
				WrapperUtils.wrapCenteredFormHorizontal(
					inputLabel,
					arrowsLabel,
					outputLabel),
				chart);
	}

	private void setupChart() {
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Project chart");
		conf.setSeries(series);
	}

	// TODO -> This will probably need to dynamically push update the chart panel.
	// TODO -> As well right now I'm just pushing fake data.
	public void setChartData(List<Number> data) {
		series.setData(data);
	}
}
