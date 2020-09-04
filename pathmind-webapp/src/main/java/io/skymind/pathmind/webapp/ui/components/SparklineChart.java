package io.skymind.pathmind.webapp.ui.components;

import java.util.ArrayList;
import java.util.OptionalDouble;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;

public class SparklineChart extends VerticalLayout{

    private Button enlargeButton = new Button("Show");
    private Chart chart = new Chart(ChartType.AREASPLINE);
    
    private int WIDTH = 100;
    private int HEIGHT = 32;

    public SparklineChart() {
        setPadding(false);
        setSpacing(false);
        setupChart();
        setWidth(WIDTH + "px");
        setHeight(HEIGHT + "px");
        addClassName("sparkline");
        add(chart, enlargeButton);
    }

    public void setupButton(Command clickHandler) {
        enlargeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        enlargeButton.addClickListener(event -> clickHandler.execute());
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
        chart.setWidth(WIDTH + "px");
        chart.setHeight(HEIGHT + "px");
    }

    public void setSparkLine(double[] sparklineData, int index, RewardVariable rewardVariable) {
        ArrayList<Number> data = new ArrayList<>();
        for (int i = 0; i < sparklineData.length; i++) {
            data.add(sparklineData[i]);
        }
        OptionalDouble min = data.stream().mapToDouble(Number::doubleValue).min();
        OptionalDouble max = data.stream().mapToDouble(Number::doubleValue).max();

        double minVal = min.orElse(0);
        double maxVal = max.orElse(0);
        
        if (rewardVariable.getGoalValue() != null) {
            double goalValue = rewardVariable.getGoalValue();
            if (minVal > goalValue) {
                minVal = goalValue;
            }
            
            if (maxVal < goalValue) {
                maxVal = goalValue;
            }
        }
        
        chart.getConfiguration().getyAxis().setMin(minVal);
        chart.getConfiguration().getyAxis().setMax(maxVal);

        ListSeries series = new ListSeries(data);
        PlotOptionsSeries plotOptions = new PlotOptionsSeries();
        plotOptions.setMarker(new Marker(false));
        plotOptions.setColorIndex(index);

        series.setPlotOptions(plotOptions);
        chart.getConfiguration().addSeries(series);

        if (rewardVariable.getGoalValue() != null && rewardVariable.getGoalConditionTypeEnum() != null) {
            GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
            PlotBand target = new PlotBand();
            target.setFrom(rewardVariable.getGoalValue());
            if (goalCondition.equals(GoalConditionType.GREATER_THAN_OR_EQUAL)) {
                target.setTo(maxVal);
            } else {
                target.setTo(0);
            }
            target.setZIndex(10);
            target.setClassName("target");
            chart.getConfiguration().getyAxis().addPlotBand(target);
        }

        chart.drawChart(true);
    }
    
}
