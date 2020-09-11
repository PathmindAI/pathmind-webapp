package io.skymind.pathmind.webapp.ui.components;

import java.util.ArrayList;
import java.util.OptionalDouble;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class SparklineChartNew extends DataChart {

    private Button enlargeButton = new Button("Show");
    
    private int WIDTH = 100;
    private int HEIGHT = 32;

    public SparklineChartNew() {
        String type = "combo";
        Boolean showTooltip = null;
        String hAxisTitle = null;
        String vAxisTitle = null;
        Boolean curveLines = null;
        String seriesType = "area";
        Boolean stacked = true;
        JsonObject viewWindow = calculateViewWindow();
        JsonObject series = createSeries();
        JsonArray cols = createCols();
        JsonArray rows = createRows();
        setupChart(
            type,
            showTooltip,
            hAxisTitle,
            vAxisTitle,
            curveLines,
            seriesType,
            series,
            stacked,
            viewWindow,
            cols,
            rows
        );
        addClassName("sparkline");
    }

    private JsonObject createSeries() {
        JsonObject series = Json.createObject();
        series.put("0", Json.parse("{'type': 'line','enableInteractivity': false}"));
        series.put("1", Json.parse("{'lineWidth': 0,'enableInteractivity': false,'color': 'transparent'}"));
        series.put("2", Json.parse("{'lineWidth': 0,'enableInteractivity': false,'color': 'green'}"));
        return series;
    }

    private JsonObject calculateViewWindow() {
        JsonObject viewWindow = Json.createObject();
        viewWindow.put("max", 5500); // have to calculate from the actual dataset
        viewWindow.put("min", 5120); // have to calculate from the actual dataset
        return viewWindow;
    }

    private JsonArray createCols() {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        cols.set(1, Json.parse("{'label':'Mean Metric Value', 'type':'number'}"));
        cols.set(2, Json.parse("{'label':'goal base', 'type': 'number'}"));
        cols.set(3, Json.parse("{'label':'goal', 'type': 'number'}"));
        return cols;
    }

    private JsonArray createRows() {
        JsonArray rows = Json.createArray();
        // dummy data. will need to move and reformat the sparkline data to make it work
        rows.set(0, createRowItem(1, 5340.9, 5200, 300));
        rows.set(1, createRowItem(2, 5340, 5200, 300));
        rows.set(2, createRowItem(3, 5210, 5200, 300));
        rows.set(3, createRowItem(4, 5150, 5200, 300));
        rows.set(4, createRowItem(5, 5325, 5200, 300));
        rows.set(5, createRowItem(6, 5123, 5200, 300));
        rows.set(6, createRowItem(7, 5500, 5200, 300));
        rows.set(7, createRowItem(8, 5400, 5200, 300));
        return rows;
    }

    private JsonArray createRowItem(int iteration, double metricValue, double goalBase, double goal) {
        JsonArray rowItem = Json.createArray();
        rowItem.set(0, iteration);
        rowItem.set(1, metricValue);
        rowItem.set(2, goalBase);
        rowItem.set(3, goal);
        return rowItem;
    }

    public void setupButton(Command clickHandler) {
        enlargeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        enlargeButton.addClickListener(event -> clickHandler.execute());
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
        
        // chart.getConfiguration().getyAxis().setMin(minVal);
        // chart.getConfiguration().getyAxis().setMax(maxVal);

        // ListSeries series = new ListSeries(data);
        // PlotOptionsSeries plotOptions = new PlotOptionsSeries();
        // plotOptions.setMarker(new Marker(false));
        // plotOptions.setColorIndex(index);

        // series.setPlotOptions(plotOptions);
        // chart.getConfiguration().addSeries(series);

        // if (rewardVariable.getGoalValue() != null && rewardVariable.getGoalConditionTypeEnum() != null) {
        //     GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
        //     PlotBand target = new PlotBand();
        //     target.setFrom(rewardVariable.getGoalValue());
        //     if (goalCondition.equals(GoalConditionType.GREATER_THAN_OR_EQUAL)) {
        //         target.setTo(maxVal);
        //     } else {
        //         target.setTo(0);
        //     }
        //     target.setZIndex(10);
        //     target.setClassName("target");
        //     chart.getConfiguration().getyAxis().addPlotBand(target);
        // }

        // chart.drawChart(true);
    }
    
}
