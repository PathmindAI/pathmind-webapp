package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class AllMetricsChart extends DataChart {

    private Policy metricsPolicy;
    private List<RewardVariable> selectedRewardVariables;
    private Map<Integer, List<Double>> allMetricsChartData;

    public AllMetricsChart() {
        super();
    }

    private JsonObject createSeries() {
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
        for (int i = 0; i < selectedRewardVariables.size(); i++) {
            if (selectedRewardVariables.get(i) != null) {
                String seriesColor = colors.get(selectedRewardVariables.get(i).getArrayIndex()%10);
                series.put(""+i, Json.parse("{'color': '"+seriesColor+"'}"));
            }
        }
        return series;
    }

    private JsonArray createCols() {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        selectedRewardVariables.forEach((rewardVariable) -> {
            int index = cols.length();
            cols.set(index, Json.parse("{'label':'reward variable "+index+"', 'type':'number'}"));
            cols.set(index+1, Json.parse("{'role': 'tooltip', 'type':'string', 'p': {'html': true}}"));
        });
        return cols;
    }

    private JsonArray createRows() {
        JsonArray rows = Json.createArray();
        allMetricsChartData.forEach((iteration, metricList) -> {
            rows.set(rows.length(), createRowItem(iteration, metricList));
        });
        return rows;
    }

    private JsonArray createRowItem(Integer iteration, List<Double> thisIterationMetricValues) {
        JsonArray rowItem = Json.createArray();
        rowItem.set(0, iteration);
        for (int i = 0; i < thisIterationMetricValues.size(); i++) {
            Double metricValue = thisIterationMetricValues.get(i);
            int index = rowItem.length();
            if (selectedRewardVariables.get(i) != null && metricValue != null) {
                RewardVariable rewardVariable = selectedRewardVariables.get(i);
                rowItem.set(index, metricValue);
                String meanMetricValueFormatted = Math.abs(metricValue) > 1 ? String.format("%.2f", metricValue) : String.format("%.4f", metricValue);
                String tooltip = "<div><b>"+rewardVariable.getName()+"</b><br>";
                if (rewardVariable.getGoalConditionTypeEnum() != null && rewardVariable.getGoalValue() != null) {
                    tooltip += "<b>Goal</b> "+rewardVariable.getGoalConditionTypeEnum().toString()+rewardVariable.getGoalValue()+"<br>";
                }
                tooltip += "<b>Iteration #</b>"+iteration+"<br><b>Mean Metric</b> "+meanMetricValueFormatted+"</div>";

                rowItem.set(index+1, tooltip);
            } else {
                rowItem.set(index, Json.createNull());
                rowItem.set(index+1, Json.createNull());
            }
        }
        return rowItem;
    }

    public Map<Integer, List<Double>> generateAllMetricsChartData(Map<Integer, Map<Integer, Double>> sparklinesData) {
        Map<Integer, List<Double>> allLinesData = new LinkedHashMap<>();
        // Get first metric's sparkline data
        Map<Integer, Double> firstMetricSparklineData = sparklinesData.get(0);
        // Get first metric's last iteration number
        // all sparklines should have the same number of iterations
        List<Integer> iterationList = new ArrayList<>(firstMetricSparklineData.keySet());
        int maxIteration = iterationList.get(firstMetricSparklineData.size()-1);

        // save a list of sparkline datum per metric per iteration
        for (int i = 0; i <= maxIteration; i++) {
            final int iterationNumber = i;
            List<Double> thisIterationMetricValues = new ArrayList<>();
            sparklinesData.forEach((metricIndex, sparklineData) -> {
                thisIterationMetricValues.add(sparklineData.get(iterationNumber));
            });
            allLinesData.put(i, thisIterationMetricValues);
        }
        
        return allLinesData;
    }

    public void updateData() {
        if (metricsPolicy == null) return;
        JsonArray cols = createCols();
        JsonArray rows = createRows();
        setData(cols, rows);
    }
    private void updateSelectedRewardVariables(List<RewardVariable> rewardVariables) {
        selectedRewardVariables = rewardVariables;
    }
    private void updateBestPolicy(Policy bestPolicy) {
        metricsPolicy = bestPolicy;
        allMetricsChartData = generateAllMetricsChartData(metricsPolicy.getSparklinesData());
    }

    public void setAllMetricsChart(List<RewardVariable> rewardVariables, Policy bestPolicy) {
        if (rewardVariables == null || bestPolicy == null || bestPolicy.getSparklinesData().size() == 0) {
            setChartEmpty();
            return;
        }
        updateSelectedRewardVariables(rewardVariables);
        updateBestPolicy(bestPolicy);

        String type = "line";
        Boolean showTooltip = true;
        String hAxisTitle = "Iteration";
        String vAxisTitle = "Mean Metric Value per Iteration";
        Boolean curveLines = true;
        String seriesType = null;
        Boolean stacked = null;
        JsonObject series = createSeries();
        JsonObject viewWindow = null;

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
        updateData();
    }
    
}
