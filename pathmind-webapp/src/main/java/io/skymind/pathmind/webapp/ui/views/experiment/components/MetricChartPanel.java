package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;

import java.util.ArrayList;
import java.util.OptionalDouble;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class MetricChartPanel extends VerticalLayout
{

    private Chart chart = new Chart(ChartType.AREASPLINE);
    private Span chartLabel = LabelFactory.createLabel("", BOLD_LABEL);
    private Paragraph description = new Paragraph("This chart is a screenshot at the time of opening. It does not update automatically.");

    public MetricChartPanel() {
        setupChart();
        add(chartLabel, description, chart);
        setPadding(false);
        setSpacing(false);
        addClassName("metric-chart-panel");
    }

    private void setupChart() {
        XAxis xAxis = new XAxis();
        xAxis.setTitle("Iteration");
        xAxis.setAllowDecimals(true);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Mean Metric Value");

        chart.getConfiguration().setAccessibility(new Accessibility(false));
        chart.getConfiguration().getLegend().setEnabled(false);
        chart.getConfiguration().addxAxis(xAxis);
        chart.getConfiguration().addyAxis(yAxis);
        chart.getConfiguration().getTooltip().setFormatter(
                "return "
                + "'<b>Mean Metric </b>' + this.y.toFixed(Math.abs(this.y) > 1 ? 1 : 6)");
        chart.setSizeFull();
    }

    public void setLines(double[] sparklineData, int index, RewardVariable rewardVariable) {
        chartLabel.setText(rewardVariable.getName());
        ArrayList<Number> data = new ArrayList<>();
        for (int i = 0; i < sparklineData.length; i++) {
            data.add(sparklineData[i]);
        }
        OptionalDouble max = data.stream().mapToDouble(Number::doubleValue).max();

        double maxVal = max.orElse(0);

        ListSeries series = new ListSeries(data);
        PlotOptionsSeries plotOptions = new PlotOptionsSeries();
        plotOptions.setMarker(new Marker(false));
        plotOptions.setColorIndex(index);

        series.setPlotOptions(plotOptions);
        chart.getConfiguration().addSeries(series);

        if (rewardVariable.getGoalValue() != null && rewardVariable.getGoalConditionTypeEnum() != null) {
            GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
            PlotBand target = new PlotBand();
            Label targetLabel = new Label("Goal: "+goalCondition.toString()+rewardVariable.getGoalValue());
            targetLabel.setAlign(HorizontalAlign.CENTER);
            target.setFrom(rewardVariable.getGoalValue());
            if (goalCondition.equals(GoalConditionType.GREATER_THAN_OR_EQUAL)) {
                target.setTo(maxVal);
            } else {
                target.setTo(0);
            }
            target.setZIndex(10);
            target.setClassName("target");
            target.setLabel(targetLabel);
            chart.getConfiguration().getyAxis().addPlotBand(target);
        }

        chart.drawChart(true);
    }
}

