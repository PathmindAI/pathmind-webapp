package io.skymind.pathmind.webapp.ui.components;

import java.util.ArrayList;
import java.util.Arrays;
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
        super();
        addClassName("sparkline");
    }

    private JsonObject createSeries() {
        JsonObject series = Json.createObject();
        series.put("0", Json.parse("{'type': 'line','enableInteractivity': false}"));
        series.put("1", Json.parse("{'lineWidth': 0,'enableInteractivity': false,'color': 'transparent'}"));
        series.put("2", Json.parse("{'lineWidth': 0,'enableInteractivity': false,'color': 'green'}"));
        return series;
    }

    private JsonObject calculateViewWindow(double maxValue, double minValue) {
        JsonObject viewWindow = Json.createObject();
        viewWindow.put("max", maxValue);
        viewWindow.put("min", minValue);
        return viewWindow;
    }

    private JsonArray createCols(Boolean hasGoal) {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        cols.set(1, Json.parse("{'label':'Mean Metric Value', 'type':'number'}"));
        if (hasGoal) {
            cols.set(2, Json.parse("{'label':'goal base', 'type': 'number'}"));
            cols.set(3, Json.parse("{'label':'goal', 'type': 'number'}"));
        }
        return cols;
    }

    private JsonArray createRows(Boolean hasGoal, double[] sparklineData, double maxValue, double minValue, RewardVariable rewardVariable) {
        JsonArray rows = Json.createArray();
        Double goalValue = rewardVariable.getGoalValue();
        GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
        Double goalLowerBound = null;
        Double goalRange = null;
        if (goalValue != null && goalCondition != null) {
            Boolean isGreaterThan = goalCondition.equals(GoalConditionType.GREATER_THAN_OR_EQUAL);
            goalLowerBound = isGreaterThan ? goalValue : minValue;
            goalRange = isGreaterThan ? maxValue - goalLowerBound : goalValue;
        }
        for (int i = 0; i < sparklineData.length; i++) {
            rows.set(i, createRowItem(i, sparklineData[i], goalLowerBound, goalRange));
        }
        return rows;
    }

    private JsonArray createRowItem(int iteration, double metricValue, Double goalBase, Double goal) {
        JsonArray rowItem = Json.createArray();
        rowItem.set(0, iteration);
        rowItem.set(1, metricValue);
        if (goalBase != null) {
            rowItem.set(2, goalBase);
        }
        if (goal != null) {
            rowItem.set(3, goal);
        }
        return rowItem;
    }

    public void setupButton(Command clickHandler) {
        enlargeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        enlargeButton.addClickListener(event -> clickHandler.execute());
    }

    public void setSparkLine(double[] sparklineData, int index, RewardVariable rewardVariable) {
        OptionalDouble min = Arrays.stream(sparklineData).min();
        OptionalDouble max = Arrays.stream(sparklineData).max();
        
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
        
        String type = "combo";
        Boolean showTooltip = null;
        String hAxisTitle = null;
        String vAxisTitle = null;
        Boolean curveLines = null;
        String seriesType = "area";
        Boolean stacked = true;
        JsonObject viewWindow = calculateViewWindow(maxVal, minVal);
        JsonObject series = createSeries();

        Boolean hasGoal = rewardVariable.getGoalValue() != null && rewardVariable.getGoalConditionTypeEnum() != null;
        JsonArray cols = createCols(hasGoal);
        JsonArray rows = createRows(hasGoal, sparklineData, maxVal, minVal, rewardVariable);
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
    }
    
}
