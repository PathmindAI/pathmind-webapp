package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class CompareMetricsChart extends DataChart {
    private Policy bestPolicy;
    // This must be an array due to a number of issues. For example a list containing a null item at the end will result in the list being decreased in size. This guarantees
    // the size of the list. We also can't just dynamically get the RewardVariable at a certain index because other parts of the code rely on looping through the list.
    private RewardVariable[] rewardVariables;
    private String metric1AxisTitle;
    private String metric1Color;
    private List<String> colors = ChartUtils.colors();

    public CompareMetricsChart() {
        super();
    }

    private JsonObject createSeries() {
        JsonObject series = Json.createObject();
        RewardVariable firstNonNullVariable = Arrays.stream(rewardVariables)
                .filter(rvar -> rvar != null)
                .map(Optional::ofNullable).findFirst()
                .flatMap(Function.identity())
                .orElse(null);
        for (RewardVariable rv : rewardVariables) {
            if (rv != null) {
                String seriesColor = colors.get(rv.getArrayIndex() % 10);
                Boolean isFirstNonNullVariable = firstNonNullVariable.equals(rv);
                Integer axisIndex = isFirstNonNullVariable ? 0 : 1;
                if (!isFirstNonNullVariable && firstNonNullVariable.getArrayIndex() % 10 == rv.getArrayIndex() % 10) {
                    seriesColor = "black";
                }
                series.put("" + rv.getArrayIndex(), Json.parse("{'color': '" + seriesColor + "', 'targetAxisIndex': " + axisIndex + "}"));
            }
        }
        return series;
    }

    private void createAxisTitlesAndColors() {
        metric1AxisTitle = null;
        for (int i = 0; i < rewardVariables.length; i++) {
            if (rewardVariables[i] != null) {
                Boolean isFirstNonNullVariable = Arrays.stream(rewardVariables).filter(rv -> rv != null).findFirst().get().equals(rewardVariables[i]);
                if (isFirstNonNullVariable) {
                    metric1AxisTitle = rewardVariables[i].getName() + " mean";
                    metric1Color = colors.get(rewardVariables[i].getArrayIndex() % 10);
                }
            }
        }
    }

    private JsonArray createCols() {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        for (RewardVariable rewardVariable : rewardVariables) {
            int index = cols.length();
            cols.set(index, Json.parse("{'label':'reward variable " + index + "', 'type':'number'}"));
            cols.set(index + 1, Json.parse("{'role': 'tooltip', 'type':'string', 'p': {'html': true}}"));
        }
        return cols;
    }

    private JsonArray createRows() {
        JsonArray rows = Json.createArray();
        bestPolicy.getMetricsLinesData().forEach((iteration, metricList) ->
                rows.set(rows.length(), createRowItem(iteration, metricList)));
        return rows;
    }

    private JsonArray createRowItem(Integer iteration, List<Double> thisIterationMetricValues) {
        JsonArray rowItem = Json.createArray();
        rowItem.set(0, iteration);
        for (int i = 0; i < thisIterationMetricValues.size(); i++) {
            Double metricValue = thisIterationMetricValues.get(i);
            int index = rowItem.length();
            if (rewardVariables[i] != null && metricValue != null) {
                RewardVariable rewardVariable = rewardVariables[i];
                rowItem.set(index, metricValue);
                String meanMetricValueFormatted = Math.abs(metricValue) > 1 ? String.format("%.2f", metricValue) : String.format("%.4f", metricValue);
                String tooltip = "<div><b>" + rewardVariable.getName() + "</b><br>";
                if (rewardVariable.getGoalConditionTypeEnum() != null && rewardVariable.getGoalValue() != null) {
                    tooltip += "<b>Goal</b> " + rewardVariable.getGoalConditionTypeEnum().toString() + rewardVariable.getGoalValue() + "<br>";
                }
                tooltip += "<b>Iteration #</b>" + iteration + "<br><b>Mean Metric</b> " + meanMetricValueFormatted + "</div>";

                rowItem.set(index + 1, tooltip);
            } else {
                rowItem.set(index, Json.createNull());
                rowItem.set(index + 1, Json.createNull());
            }
        }
        return rowItem;
    }

    public void updateData() {
        if (bestPolicy == null) {
            setChartEmpty();
            return;
        }
        JsonArray cols = createCols();
        JsonArray rows = createRows();
        setData(cols, rows);
    }

    private void updateSelectedRewardVariables(List<RewardVariable> selectedRewardVariables) {
        // Using the selectedRewardVariables create a new List with null's for all empty arrayIndex values so that the chart colors remain the same.
        rewardVariables = new RewardVariable[bestPolicy.getSparklinesData().size()];
        // Insert the RewardVariables at their appropriate index values.
        selectedRewardVariables.stream().forEach(selectedRewardVariable -> rewardVariables[selectedRewardVariable.getArrayIndex()] = selectedRewardVariable);
    }

    public void setCompareMetricsChart(List<RewardVariable> selectedRewardVariables, Policy bestPolicy) {
        this.bestPolicy = bestPolicy;
        Boolean showEmptyChart = selectedRewardVariables == null || bestPolicy == null || bestPolicy.getSparklinesData().size() == 0;
        JsonObject series;
        if (showEmptyChart) {
            series = Json.createObject();
        } else {
            updateSelectedRewardVariables(selectedRewardVariables);
            series = createSeries();
            createAxisTitlesAndColors();
        }
        String type = "line";
        Boolean showTooltip = true;
        String hAxisTitle = "Iteration";
        String vAxisTitle = "Mean Metric Value per Iteration";
        Boolean curveLines = true;
        String seriesType = "";
        Boolean stacked = false;
        JsonObject viewWindow = Json.createObject();

        setupChart(
                type,
                showTooltip,
                hAxisTitle,
                vAxisTitle,
                metric1AxisTitle,
                metric1Color,
                curveLines,
                seriesType,
                series,
                stacked,
                viewWindow
        );

        if (showEmptyChart) {
            setChartEmpty();
        } else {
            updateData();
        }
    }

}
