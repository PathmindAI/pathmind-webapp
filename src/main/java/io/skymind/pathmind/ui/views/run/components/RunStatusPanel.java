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
import com.vaadin.flow.data.provider.CallbackDataProvider;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.ui.utils.WrapperUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunStatusPanel extends VerticalLayout
{
	private Chart chart = new Chart(ChartType.SPLINE);
	private ListSeries series = new ListSeries("Score");

	private Label scoreLabel = new Label("0");

	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runTypeLabel = new Label();
	private Label elapsedTimeLabel = new Label("");
	private Label completedLabel = new Label();
	private Label algorithmLabel = new Label();

	private long startTime;

	public RunStatusPanel(RunType runType)
	{
		setSizeFull();

		add(WrapperUtils.wrapCenteredFormVertical(
				getTitleBar(),
				new Hr(),
				WrapperUtils.wrapCenteredFormVerticalBordered(
					getScoreDisplay(),
					getScoreChart()),
				getStatusDetailsPanel(runType)
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

	public void setRunStatus(RunStatus runStatus) {
		statusLabel.setText(runStatus.toString());
	}

	public void setAlgorithmType(Algorithm algorithmType) {
		algorithmLabel.setText(algorithmType.toString());
	}

	// TODO -> Better naming once I understand what this does exactly. As in what other value can it have then in progress or done, and assuming
	// that's the case then why not just use the status field.
	public void setCompleted(String completed) {
		completedLabel.setText(completed);
	}

	// TODO -> Implement
	// TODO -> Elapsed time should show seconds then minutes and seconds, and so on.
	public void update(Number newChartData, long elapsedTime) {
		series.addData(newChartData);
		scoreLabel.setText(newChartData.toString());
		elapsedTimeLabel.setText(Long.toString(elapsedTime) + " seconds");
	}

	private Component getStatusDetailsPanel(RunType runType)
	{
		Label[] labels = Arrays.asList(
			getElementLabel("Status"),
			getElementLabel("Run Type"),
			getElementLabel("Elapsed"),
			getElementLabel("Completed"),
			getElementLabel("Algorithm"))
				.stream().toArray(Label[]::new);

		runTypeLabel.setText(runType.toString());

		removeTopMargins(labels);
		removeTopMargins(statusLabel, runTypeLabel, elapsedTimeLabel, completedLabel, algorithmLabel);

		VerticalLayout leftVerticalLayout = new VerticalLayout(labels);
		leftVerticalLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		leftVerticalLayout.setWidthFull();
		leftVerticalLayout.setPadding(false);

		VerticalLayout rightVerticalLayout = new VerticalLayout(
				statusLabel,
				runTypeLabel,
				elapsedTimeLabel,
				completedLabel,
				algorithmLabel);
		rightVerticalLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
		rightVerticalLayout.setPadding(false);

		HorizontalLayout wrapper = WrapperUtils.wrapCenteredFormHorizontal(
				leftVerticalLayout,
				rightVerticalLayout);
		wrapper.getStyle().set("padding-top", "10px");
		return wrapper;
	}

	// TODO -> Move style to CSS
	private Label getElementLabel(String label) {
		Label fieldLabel = new Label(label + " : ");
		fieldLabel.getStyle().set("font-weight", "bold");
		return fieldLabel;
	}

	private void removeTopMargins(Label... labels) {
		Arrays.stream(labels).forEach(label ->
				label.getStyle().set("margin-top", "0px"));
	}
}
