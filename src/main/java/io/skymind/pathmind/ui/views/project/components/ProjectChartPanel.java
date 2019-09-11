package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsSpline;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.data.Experiment;
import org.springframework.stereotype.Component;


@Component
public class ProjectChartPanel extends VerticalLayout
{
	private Chart chart = new Chart(ChartType.SPLINE);
	private ListSeries series = new ListSeries("Score");

	public ProjectChartPanel()
	{
		setupChart();
		add(chart);
	}

	private void setupChart() {
		series.setPlotOptions(new PlotOptionsSpline());
		series.setName("Score");
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Project chart");
		conf.setSeries(series);
	}

	public void update(Experiment experiment) {
		series.setData(experiment.getScores());
		chart.drawChart();
	}
}
