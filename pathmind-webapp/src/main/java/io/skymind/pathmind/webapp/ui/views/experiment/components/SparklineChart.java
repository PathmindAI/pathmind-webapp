package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class SparklineChart extends DataChart {

    public SparklineChart() {
        super();
    }

    private JsonObject createSeries(Boolean showDetails, Integer index) {
        JsonObject series = Json.createObject();
        List<String> colors = Arrays.asList(
            "#17a747",
            "#214e96",
            "#318c81",
            "#7550e5",
            "#62a540",
            "#e0667d",
            "#5f8fd6",
            "#931901",
            "#bd52a3",
            "#887100"
        );
        String color = index == null ? "#1a2949" : colors.get(index%10);
        series.put("0", Json.parse("{'type': 'line','enableInteractivity': "+showDetails+",'color': '"+color+"'}"));
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

    private JsonArray createCols(Boolean hasGoal, Boolean showDetails) {
        JsonArray cols = Json.createArray();
        int i = -1;
        cols.set(++i, Json.parse("{'label':'Iteration', 'type':'number'}"));
        cols.set(++i, Json.parse("{'label':'Mean Metric Value', 'type':'number'}"));
        if (showDetails) {
            cols.set(++i, Json.parse("{'role': 'tooltip', 'type':'string', 'p': {'html': true}}"));
        }
        if (hasGoal) {
            cols.set(++i, Json.parse("{'label':'goal base', 'type': 'number'}"));
            cols.set(++i, Json.parse("{'label':'goal', 'type': 'number'}"));
        }
        return cols;
    }

    private JsonArray createRows(Boolean hasGoal, Boolean showDetails, List<Integer> iterationList, List<Double> sparklineData, double maxValue, double minValue, RewardVariable rewardVariable, double metricRange) {
        JsonArray rows = Json.createArray();
        Double goalValue = rewardVariable.getGoalValue();
        GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
        Double goalLowerBound = null;
        Double goalRange = null;
        if (goalValue != null && goalCondition != null) {
            Boolean isGreaterThan = goalCondition.equals(GoalConditionType.GREATER_THAN_OR_EQUAL);
            goalLowerBound = isGreaterThan ? goalValue : minValue;
            goalRange = isGreaterThan ? maxValue - goalLowerBound : goalValue - goalLowerBound;
        }
        for (int i = 0; i < iterationList.size(); i++) {
            rows.set(i, createRowItem(iterationList.get(i), sparklineData.get(i), goalLowerBound, goalRange, showDetails, metricRange));
        }
        return rows;
    }

    private JsonArray createRowItem(int iteration, double metricValue, Double goalBase, Double goal, Boolean showDetails, double metricRange) {
        JsonArray rowItem = Json.createArray();
        int i = -1;
        rowItem.set(++i, iteration);
        rowItem.set(++i, metricValue);
        String metricValueFormatted = metricRange > 10 ? String.format("%.0f", metricValue) : String.format("%.4f", metricValue);
        if (showDetails) {
            rowItem.set(++i, "<div><b>Iteration #</b>"+iteration+"<br><b>Mean Metric</b> "+metricValueFormatted+"</div>");
        }
        if (goalBase != null) {
            rowItem.set(++i, goalBase);
        }
        if (goal != null) {
            rowItem.set(++i, goal);
        }
        return rowItem;
    }

    public void updateData(List<Integer> iterationList, List<Double> sparklineData, Boolean hasGoal, Boolean showDetails, RewardVariable rewardVariable, double maxValue, double minValue) {
        JsonArray cols = createCols(hasGoal, showDetails);
        JsonArray rows = createRows(hasGoal, showDetails, iterationList, sparklineData, maxValue, minValue, rewardVariable, maxValue - minValue);
        setData(cols, rows);
    }

    public void setSparkLine(Map<Integer, Double> sparklineMap, RewardVariable rewardVariable, Boolean showDetails) {
        setSparkLine(sparklineMap, rewardVariable, showDetails, null);
    }

    public void setSparkLine(Map<Integer, Double> sparklineMap, RewardVariable rewardVariable, Boolean showDetails, Integer index) {
        if (sparklineMap == null) {
            return;
        }
        List<Integer> iterationList = sparklineMap.keySet().stream().collect(Collectors.toList());
        List<Double> sparklineData = sparklineMap.values().stream().collect(Collectors.toList());
        OptionalDouble min = sparklineData.stream().mapToDouble(v -> v).min();
        OptionalDouble max = sparklineData.stream().mapToDouble(v -> v).max();

        double minVal = min.orElse(0);
        double maxVal = max.orElse(0);
        
        Double goalValue = rewardVariable.getGoalValue();
        GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
        if (goalValue != null && goalCondition != null) {
            double valuesRange = maxVal - minVal > 0 ? maxVal - minVal : goalValue*0.1;
            Boolean isGreaterThan = goalCondition.equals(GoalConditionType.GREATER_THAN_OR_EQUAL);
            if (minVal > goalValue) {
                if (isGreaterThan) {
                    minVal = goalValue;
                } else {
                    minVal = goalValue - valuesRange <= 0 ? 0 : goalValue - valuesRange;
                }
            }

            if (maxVal < goalValue) {
                if (isGreaterThan) {
                    maxVal = goalValue + valuesRange;
                } else {
                    maxVal = goalValue;
                }
            }
        }
        
        String type = "combo";
        Boolean showTooltip = showDetails ? true : null;
        String hAxisTitle = showDetails ? "Iteration" : null;
        String vAxisTitle = showDetails ? "Mean Metric Value" : null;
        Boolean curveLines = null;
        String seriesType = "area";
        Boolean stacked = true;
        JsonObject viewWindow = calculateViewWindow(maxVal, minVal);
        JsonObject series = createSeries(showDetails, index);

        Boolean hasGoal = rewardVariable.getGoalValue() != null && rewardVariable.getGoalConditionTypeEnum() != null;
        setupChart(
            type,
            showTooltip,
            hAxisTitle,
            vAxisTitle,
            curveLines,
            seriesType,
            series,
            stacked,
            viewWindow
        );
        updateData(iterationList, sparklineData, hasGoal, showDetails, rewardVariable, maxVal, minVal);
    }
    
}
