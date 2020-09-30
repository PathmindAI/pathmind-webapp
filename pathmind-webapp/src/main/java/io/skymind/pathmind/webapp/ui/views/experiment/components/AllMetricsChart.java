package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class AllMetricsChart extends DataChart {

    private int bestPolicySeriesNumber = -1;

    public AllMetricsChart() {
        super();
    }

    private JsonObject createSeries(int numberOfSeries) {
        JsonObject series = Json.createObject();
        List<String> colors = Arrays.asList(
            "#7cb5ec",
            "#737378",
            "#90ed7d",
            "#f7a35c",
            "#8085e9",
            "#f15c80",
            "#e4d354",
            "#2b908f",
            "#f45b5b",
            "#91e8e1"
        );
        for (int i = 0; i < numberOfSeries; i++) {
            String seriesColor = colors.get(i%10);
            series.put(""+i, Json.parse("{'color': '"+seriesColor+"'}"));
        }
        return series;
    }

    private JsonArray createCols(List<Policy> policies) {
        JsonArray cols = Json.createArray();
        cols.set(0, Json.parse("{'label':'Iteration', 'type':'number'}"));
        policies.forEach((policy) -> {
            int index = cols.length();
            cols.set(index, Json.parse("{'label':'Policy "+policy.getName()+"', 'type':'number'}"));
            cols.set(index+1, Json.parse("{'role': 'tooltip', 'type':'string', 'p': {'html': true}}"));
        });
        return cols;
    }

    private JsonArray createRows(Map<Integer, List<RewardScore>> allLinesData) {
        JsonArray rows = Json.createArray();
        allLinesData.forEach((iteration, rewardScoreList) -> {
            rows.set(rows.length(), createRowItem(iteration, rewardScoreList));
        });
        return rows;
    }

    private JsonArray createRowItem(Integer iteration, List<RewardScore> thisIterationRewardScores) {
        JsonArray rowItem = Json.createArray();
        rowItem.set(0, iteration);
        thisIterationRewardScores.forEach(rewardScore -> {
            int index = rowItem.length();
            if (rewardScore != null) {
                Double meanRewardScoreValue = rewardScore.getMean();
                rowItem.set(index, meanRewardScoreValue);
                String meanRewardScoreValueFormatted = Math.abs(meanRewardScoreValue) > 1 ? String.format("%.2f", meanRewardScoreValue) : String.format("%.4f", meanRewardScoreValue);
                rowItem.set(index+1, "<div><b>"+"metric name"+"</b><br><b>Iteration #</b>"+iteration+"<br><b>Mean Metric</b> "+meanRewardScoreValueFormatted+"</div>");
            } else {
                rowItem.set(index, Json.createNull());
                rowItem.set(index+1, Json.createNull());
            }
        });
        return rowItem;
    }

    public Map<Integer, List<RewardScore>> generatePolicyChartData(List<Policy> policyData, Policy bestPolicy) {
        List<List<RewardScore>> allRewardScoresLists = new ArrayList<>();
        Map<Integer, List<RewardScore>> allLinesData = new HashMap<>();
        List<Integer> iterationNumbers = new ArrayList<>();
        int maxIteration = 0;
        policyData.forEach(policy -> {
            List<RewardScore> rewardScoresList = policy.getScores().stream()
                    .filter(score -> !Double.isNaN(score.getMean()))
                    .collect(Collectors.toList());
            allRewardScoresLists.add(rewardScoresList);
            if (bestPolicy != null && policy.getId() == bestPolicy.getId()) {
                bestPolicySeriesNumber = allRewardScoresLists.size()-1;
            }
            iterationNumbers.add(rewardScoresList.size());
        });
        maxIteration = Collections.max(iterationNumbers);

        // save a list of reward scores per iteration
        for (int i = 0; i < maxIteration; i++) {
            int index = i;
            List<RewardScore> thisIterationRewardScores = new ArrayList<>();
            allRewardScoresLists.stream().forEach(rewardScoresList -> {
                rewardScoresList.stream()
                        .filter(rewardScore -> rewardScore.getIteration().equals(index+1))
                        .findAny()
                        .ifPresentOrElse(rewardScore -> {
                                    thisIterationRewardScores.add(rewardScore);
                                },
                                () -> thisIterationRewardScores.add(null));
            });
            allLinesData.put(i+1, thisIterationRewardScores);
        }
        
        return allLinesData;
    }

    public void updateData(List<Policy> policies, Map<Integer, List<RewardScore>> allLinesData) {
        JsonArray cols = createCols(policies);
        JsonArray rows = createRows(allLinesData);
        setData(cols, rows);
    }

    public void setAllMetricsChart(List<Policy> updatedPolicies, Policy bestPolicy) {
        if (updatedPolicies == null) {
            setChartEmpty();
            return;
        }
        Map<Integer, List<RewardScore>> policyChartData = generatePolicyChartData(updatedPolicies, bestPolicy);

        String type = "line";
        Boolean showTooltip = true;
        String hAxisTitle = "Iteration";
        String vAxisTitle = "Mean Metric Value per Iteration";
        Boolean curveLines = true;
        String seriesType = null;
        Boolean stacked = null;
        JsonObject series = createSeries(updatedPolicies.size());
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
        updateData(updatedPolicies, policyChartData);
    }
    
}
