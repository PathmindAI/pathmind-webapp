package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class CompareMetricsChart extends DataChart {

    private Policy metricsPolicy;
    private Map<Integer, List<Double>> compareMetricsChartData;
    // This must be an array due to a number of issues. For example a list containing a null item at the end will result in the list being decreased in size. This guarantees
    // the size of the list. We also can't just dynamically get the RewardVariable at a certain index because other parts of the code rely on looping through the list.
    private RewardVariable[] rewardVariables;
    private String metric1AxisTitle;
    private String metric2AxisTitle;
    private String metric1Color;
    private String metric2Color;
    private List<String> colors = ChartUtils.colors();

    public CompareMetricsChart() {
        super();
    }

    private JsonObject createSeries() {
        JsonObject series = Json.createObject();
        RewardVariable firstNonNullVariable = Arrays.stream(rewardVariables).filter(rvar -> rvar != null).findFirst().get();
        for (RewardVariable rv: rewardVariables) {
            if (rv != null) {
                String seriesColor = colors.get(rv.getArrayIndex() % 10);
                Boolean isFirstNonNullVariable = firstNonNullVariable.equals(rv);
                Integer axisIndex = isFirstNonNullVariable ? 0 : 1;
                if (!isFirstNonNullVariable && firstNonNullVariable.getArrayIndex() % 10 == rv.getArrayIndex() % 10) {
                    seriesColor = "black";
                }
                series.put("" + rv.getArrayIndex(), Json.parse("{'color': '" + seriesColor + "', 'targetAxisIndex': "+axisIndex+"}"));
            }
        }
        return series;
    }

    private void createAxisTitlesAndColors() {
        metric1AxisTitle = null;
        metric2AxisTitle = null;
        for (int i = 0; i < rewardVariables.length; i++) {
            if (rewardVariables[i] != null) {
                Boolean isFirstNonNullVariable = Arrays.stream(rewardVariables).filter(rv -> rv != null).findFirst().get().equals(rewardVariables[i]);
                if (isFirstNonNullVariable) {
                    metric1AxisTitle = rewardVariables[i].getName() + " mean";
                    metric1Color = colors.get(rewardVariables[i].getArrayIndex() % 10);
                } else {
                    metric2AxisTitle = rewardVariables[i].getName() + " mean";
                    metric2Color = colors.get(rewardVariables[i].getArrayIndex() % 10);
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
        compareMetricsChartData.forEach((iteration, metricList) ->
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

    public Map<Integer, List<Double>> generateCompareMetricsChartData(Map<Integer, Map<Integer, Double>> sparklinesData) {
        Map<Integer, List<Double>> allLinesData = new LinkedHashMap<>();
        // Get first metric's sparkline data
        Map<Integer, Double> firstMetricSparklineData = sparklinesData.get(0);
        // Get first metric's last iteration number
        // all sparklines should have the same number of iterations
        List<Integer> iterationList = new ArrayList<>(firstMetricSparklineData.keySet());
        int maxIteration = iterationList.get(firstMetricSparklineData.size() - 1);

        // save a list of sparkline datum per metric per iteration
        for (int i = 0; i <= maxIteration; i++) {
            final int iterationNumber = i;
            List<Double> thisIterationMetricValues = new ArrayList<>();
            sparklinesData.forEach((metricIndex, sparklineData) ->
                    thisIterationMetricValues.add(sparklineData.get(iterationNumber)));
            allLinesData.put(i, thisIterationMetricValues);
        }

        return allLinesData;
    }

    public void updateData() {
        if (metricsPolicy == null) {
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
        rewardVariables = new RewardVariable[metricsPolicy.getSparklinesData().size()];
        // Insert the RewardVariables at their appropriate index values.
        selectedRewardVariables.stream().forEach(selectedRewardVariable -> rewardVariables[selectedRewardVariable.getArrayIndex()] = selectedRewardVariable);
    }

    private void updateBestPolicy(Policy bestPolicy) {
        metricsPolicy = bestPolicy.deepClone();
        compareMetricsChartData = generateCompareMetricsChartData(metricsPolicy.getSparklinesData());
    }

    public void setCompareMetricsChart(List<RewardVariable> selectedRewardVariables, Policy bestPolicy) {
        Boolean showEmptyChart = selectedRewardVariables == null || bestPolicy == null || bestPolicy.getSparklinesData().size() == 0;
        JsonObject series;
        if (showEmptyChart) {
            series = Json.createObject();
        } else {
            // updateBestPolicy must be done first as we're going to use the calculations in it to determine the size of the RewardVariables array.
            updateBestPolicy(bestPolicy);
            // HOTFIX
            selectedRewardVariables = RewardVariablesUtils.deepClone(selectedRewardVariables);
            updateSelectedRewardVariables(selectedRewardVariables);
            series = createSeries();
            createAxisTitlesAndColors();
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
                metric1Color,
                metric2Color,
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
