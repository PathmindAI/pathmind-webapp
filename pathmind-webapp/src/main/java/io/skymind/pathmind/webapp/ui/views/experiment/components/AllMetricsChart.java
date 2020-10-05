package io.skymind.pathmind.webapp.ui.views.experiment.components;

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

    public AllMetricsChart() {
        super();
    }

    private JsonObject createSeries(int numberOfSeries) {
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
        for (int i = 0; i < numberOfSeries; i++) {
            String seriesColor = colors.get(i%10);
            series.put(""+i, Json.parse("{'color': '"+seriesColor+"'}"));
        }
        return series;
    }

    private JsonArray createCols(List<RewardVariable> rewardVariables) {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        rewardVariables.forEach((rewardVariable) -> {
            int index = cols.length();
            cols.set(index, Json.parse("{'label':'"+rewardVariable.getName()+"', 'type':'number'}"));
            cols.set(index+1, Json.parse("{'role': 'tooltip', 'type':'string', 'p': {'html': true}}"));
        });
        return cols;
    }

    private JsonArray createRows(List<RewardVariable> rewardVariables, Map<Integer, List<Double>> allLinesData) {
        JsonArray rows = Json.createArray();
        allLinesData.forEach((iteration, metricList) -> {
            rows.set(rows.length(), createRowItem(rewardVariables, iteration, metricList));
        });
        return rows;
    }

    private JsonArray createRowItem(List<RewardVariable> rewardVariables, Integer iteration, List<Double> thisIterationMetricValues) {
        JsonArray rowItem = Json.createArray();
        rowItem.set(0, iteration);
        for (int i = 0; i < thisIterationMetricValues.size(); i++) {
            Double metricValue = thisIterationMetricValues.get(i);
            int index = rowItem.length();
            if (metricValue != null) {
                rowItem.set(index, metricValue);
                String meanMetricValueFormatted = Math.abs(metricValue) > 1 ? String.format("%.2f", metricValue) : String.format("%.4f", metricValue);
                rowItem.set(index+1, "<div><b>"+rewardVariables.get(i).getName()+"</b><br><b>Iteration #</b>"+iteration+"<br><b>Mean Metric</b> "+meanMetricValueFormatted+"</div>");
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
        int maxIteration = new ArrayList<>(firstMetricSparklineData.keySet()).get(firstMetricSparklineData.size()-1);

        // save a list of sparkline datum per metric per iteration
        for (int i = 0; i < maxIteration; i++) {
            int iterationNumber = i;
            List<Double> thisIterationMetricValues = new ArrayList<>();
            sparklinesData.forEach((metricIndex, sparklineData) -> {
                thisIterationMetricValues.add(sparklineData.get(iterationNumber));
            });
            allLinesData.put(i, thisIterationMetricValues);
        }
        
        return allLinesData;
    }

    public void updateData(List<RewardVariable> rewardVariables, Map<Integer, List<Double>> allLinesData) {
        JsonArray cols = createCols(rewardVariables);
        JsonArray rows = createRows(rewardVariables, allLinesData);
        setData(cols, rows);
    }

    public void setAllMetricsChart(List<RewardVariable> rewardVariables, Policy bestPolicy) {
        if (rewardVariables == null || bestPolicy == null || bestPolicy.getSparklinesData().size() == 0) {
            setChartEmpty();
            return;
        }
        Map<Integer, List<Double>> allMetricsChartData = generateAllMetricsChartData(bestPolicy.getSparklinesData());

        String type = "line";
        Boolean showTooltip = true;
        String hAxisTitle = "Iteration";
        String vAxisTitle = "Mean Metric Value per Iteration";
        Boolean curveLines = true;
        String seriesType = null;
        Boolean stacked = null;
        JsonObject series = createSeries(bestPolicy.getSparklinesData().size());
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
        updateData(rewardVariables, allMetricsChartData);
    }
    
}
