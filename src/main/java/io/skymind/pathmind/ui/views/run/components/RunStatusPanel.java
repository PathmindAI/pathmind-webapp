package io.skymind.pathmind.ui.views.run.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsSpline;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentStatusDetailsPanel;

public class RunStatusPanel extends VerticalLayout
{
	private Experiment experiment;

	private Chart chart = new Chart(ChartType.SPLINE);
	private ListSeries series = new ListSeries("Score");

	private Label scoreLabel = new Label("0");
	private ExperimentStatusDetailsPanel experimentStatusDetailsPanel = new ExperimentStatusDetailsPanel();

	public RunStatusPanel(RunType runType)
	{
		setSizeFull();

		add(WrapperUtils.wrapCenteredFormVertical(
				getTitleBar(),
				new Hr(),
				WrapperUtils.wrapCenteredFormVerticalBordered(
					getScoreDisplay(),
					getScoreChart()),
				experimentStatusDetailsPanel
		));
	}

	private Component getTitleBar()
	{
		return WrapperUtils.wrapLeftAndRightAligned(
				new HorizontalLayout(
						new Label("Model #1 (TODO)"),
						new Label("Experiment #1 (TODO)")),
				new Label("Discovert Run (TODO)")
		);
	}

	// TODO -> Most likely this will be pushed to CSS with an image behind the label but for now I'm putting it in panels since I don't
	// the image asset and it is subject to change.
	private Component getScoreDisplay()
	{
		VerticalLayout verticalLayout = new VerticalLayout(
				scoreLabel,
				new Label("Score"));
		verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		verticalLayout.getStyle().set("border", "solid 1px #ccc");

		return new HorizontalLayout(verticalLayout);
	}

	private Component getScoreChart()
	{
		series.setPlotOptions(new PlotOptionsSpline());
		series.setName("Score");
		Configuration conf = chart.getConfiguration();
		conf.setSeries(series);

		return chart;
	}

	// TODO -> Better naming once I understand what this does exactly. As in what other value can it have then in progress or done, and assuming
	// that's the case then why not just use the status field.
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
		experimentStatusDetailsPanel.update(experiment);
	}

	// TODO -> Implement
	// TODO -> Elapsed time should show seconds then minutes and seconds, and so on.
	public void update() {
//		series.addData(newChartData);
		series.setData(experiment.getScores());
//		scoreLabel.setText(newChartData.toString());
		scoreLabel.setText(experiment.getLastScore().toString());
//		experimentStatusDetailsPanel.setExperiment(experiment);
		experimentStatusDetailsPanel.update(experiment);
	}
}
