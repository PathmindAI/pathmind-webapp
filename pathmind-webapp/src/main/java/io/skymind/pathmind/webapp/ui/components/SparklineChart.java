package io.skymind.pathmind.webapp.ui.components;

import java.util.ArrayList;
import java.util.OptionalDouble;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Accessibility;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Marker;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.RewardVariable;

public class SparklineChart extends VerticalLayout{

    private Button enlargeButton = new Button("Show");
    private Chart chart = new Chart(ChartType.AREA);
    
    private int WIDTH = 100;
    private int HEIGHT = 32;

    public SparklineChart() {
        setPadding(false);
        setSpacing(false);
        setupButton();
        setupChart();
        setWidth(WIDTH + "px");
        setHeight(HEIGHT + "px");
        addClassName("sparkline");
        add(chart, enlargeButton);
    }

    private void setupButton() {
        enlargeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
    }

    private void setupChart() {
        chart.getConfiguration().setAccessibility(new Accessibility(false));
        chart.getConfiguration().getLegend().setEnabled(false);
        chart.getConfiguration().getTitle().setText("");
        chart.getStyle().set("backgroundColor", null);
        chart.getStyle().set("borderWidth", "0");
        chart.getConfiguration().getChart().setSpacing(new Number[] {0,0,0,0});
        chart.getConfiguration().getChart().setMargin(0);
        chart.getStyle().set("overflow", "none");
        
        chart.getConfiguration().getxAxis().getLabels().setEnabled(false);
        chart.getConfiguration().getxAxis().getTitle().setText(null);
        chart.getConfiguration().getxAxis().setStartOnTick(false);
        chart.getConfiguration().getxAxis().setEndOnTick(false);
        chart.getConfiguration().getxAxis().setTickPositions(new Number[0]);
        chart.getConfiguration().getyAxis().getLabels().setEnabled(false);
        chart.getConfiguration().getyAxis().getTitle().setText(null);
        chart.getConfiguration().getyAxis().setStartOnTick(false);
        chart.getConfiguration().getyAxis().setEndOnTick(false);
        chart.getConfiguration().getyAxis().setTickPositions(new Number[0]);
        chart.setHeight(WIDTH + "px");
        chart.setHeight(HEIGHT + "px");
    }

    public void setSparkLine(double[] sparklineData, int index, RewardVariable rewardVariable) {
        ArrayList<Number> data = new ArrayList<>();
        for (int i = 0; i < sparklineData.length; i++) {
            data.add(sparklineData[i]);
        }
        OptionalDouble min = data.stream().mapToDouble(Number::doubleValue).min();
        OptionalDouble max = data.stream().mapToDouble(Number::doubleValue).max();

        double goal = rewardVariable.getGoalValue();

        double minVal = min.orElse(0);
        double maxVal = max.orElse(0);

        if (minVal > goal) {
            // if the value is equal to the goal value, the goal line doesn't show on the chart
            minVal = goal;
        }

        if (maxVal < goal) {
            // if the value is equal to the goal value, the goal line doesn't show on the chart
            maxVal = goal;
        }
        
        chart.getConfiguration().getyAxis().setMin(minVal);
        chart.getConfiguration().getyAxis().setMax(maxVal);

        ListSeries series = new ListSeries(data);
        PlotOptionsSeries plotOptions = new PlotOptionsSeries();
        plotOptions.setMarker(new Marker(false));
        plotOptions.setColorIndex(index);

        series.setPlotOptions(plotOptions);
        chart.getConfiguration().addSeries(series);

        PlotLine target = new PlotLine(goal);
        target.setZIndex(9999);
        target.setClassName("target");
        target.setValue(goal);
        chart.getConfiguration().getyAxis().addPlotLine(target);

        chart.drawChart(true);
    }
    
}
