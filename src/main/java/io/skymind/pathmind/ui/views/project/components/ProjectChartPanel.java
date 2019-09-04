package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;

import java.util.ArrayList;
import java.util.List;


public class ProjectChartPanel extends VerticalLayout
{
	// TODO -> I have no idea what these components should be or what they do.
	private Label inputLabel = new Label("Input");
	private Label arrowsLabel = new Label(" > < ");
	private Label outputLabel = new Label("Output");

	private Chart chart = new Chart(ChartType.SPLINE);
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
		series.setPlotOptions(new PlotOptionsSpline());
		series.setName("Score");
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Project chart");
		conf.setSeries(series);
	}

	public void setChartData(List<Number> data) {
		// TIP -> I'm converting to an Arraylist just in case we use Arrays.asList() to set the data at which point the addChartData will fail.
		series.setData(new ArrayList<Number>(data));
	}

	public void addChartData(Number data) {
		series.addData(data);
	}
}
