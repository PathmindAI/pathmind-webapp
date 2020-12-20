package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private String metric2AxisTitle;

    public CompareMetricsChart() {
        super();
    }

    private JsonObject createSeries() {
        JsonObject series = Json.createObject();
        List<String> colors = ChartUtils.colors();
        for (int i = 0; i < rewardVariables.length; i++) {
            if (rewardVariables[i] != null) {
                String seriesColor = colors.get(rewardVariables[i].getArrayIndex() % 10);
                Boolean isFirstNonNullVariable = Arrays.stream(rewardVariables).filter(rv -> rv != null).findFirst().get().equals(rewardVariables[i]);
                Integer axisIndex = isFirstNonNullVariable ? 0 : 1;
                series.put("" + i, Json.parse("{'color': '" + seriesColor + "', 'targetAxisIndex': "+axisIndex+"}"));
            }
        }
        return series;
    }

    private void createAxisTitles() {
        metric1AxisTitle = null;
        metric2AxisTitle = null;
        for (int i = 0; i < rewardVariables.length; i++) {
            if (rewardVariables[i] != null) {
                Boolean isFirstNonNullVariable = Arrays.stream(rewardVariables).filter(rv -> rv != null).findFirst().get().equals(rewardVariables[i]);
                if (isFirstNonNullVariable) {
                    metric1AxisTitle = "<" + rewardVariables[i].getName() + "> mean";
                } else {
                    metric2AxisTitle = "<" + rewardVariables[i].getName() + "> mean";
                }
            }
        }
    }

    private JsonArray createCols() {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        // TODO -> STEPH -> Is rewardVariables.length the same as cols.length? If so then we can just do IntStream.range(0, rewardVariables.length) because
        // right now I keep thinking we need the rewardVariable but it's highlighted in my IDE as red meaning it's not used...
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
        // Make sure the list is sorted by arrayIndex as there are no guarantees it will be.
        Collections.sort(selectedRewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
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
            createAxisTitles();
        }
        String type = "line";
        Boolean showTooltip = true;
        String hAxisTitle = "Iteration";
        String vAxisTitle = "Mean Metric Value per Iteration";
        Boolean curveLines = true;
        String seriesType = null;
        Boolean stacked = null;
        JsonObject viewWindow = null;

        setupChart(
                type,
                showTooltip,
                hAxisTitle,
                vAxisTitle,
                metric1AxisTitle,
                metric2AxisTitle,
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
