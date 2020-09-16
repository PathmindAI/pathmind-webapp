package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.webapp.ui.components.atoms.DataChart;

public class PolicyChart extends DataChart {

    public PolicyChart() {
        super();
    }

    private JsonObject createSeries() {
        JsonObject series = Json.createObject();
        series.put("0", Json.parse("{'type': 'line','enableInteractivity': true,'color': '#1a2949'}"));
        series.put("1", Json.parse("{'lineWidth': 0,'enableInteractivity': false,'color': 'transparent'}"));
        series.put("2", Json.parse("{'lineWidth': 0,'enableInteractivity': false,'color': 'green'}"));
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
            int index = rows.length();
            rows.set(index, createRowItem(iteration, rewardScoreList));
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
                int episodeCount = rewardScore.getEpisodeCount();
                rowItem.set(index, meanRewardScoreValue);
                String meanRewardScoreValueFormatted = meanRewardScoreValue > 1 ? String.format("%.2f", meanRewardScoreValue) : String.format("%.4f", meanRewardScoreValue);
                rowItem.set(index+1, "<div><b>Iteration #</b>"+iteration+"<br><b>Mean Reward</b> "+meanRewardScoreValueFormatted+"<br><b>Episode Count</b> "+episodeCount+"</div>");
            } else {
                rowItem.set(index, Json.createNull());
                rowItem.set(index+1, Json.createNull());
            }
        });
        return rowItem;
    }

    public Map<Integer, List<RewardScore>> generatePolicyChartData(List<Policy> policyData) {
        List<List<RewardScore>> allRewardScoresLists = new ArrayList<>();
        Map<Integer, List<RewardScore>> allLinesData = new LinkedHashMap<>();
        List<Integer> iterationNumbers = new ArrayList<>();
        int maxIteration = -1;
        policyData.forEach(policy -> {
            List<RewardScore> rewardScoresList = policy.getScores().stream()
                    .filter(score -> !Double.isNaN(score.getMean()))
                    .collect(Collectors.toList());
            allRewardScoresLists.add(rewardScoresList);
            iterationNumbers.add(rewardScoresList.size());
        });
        maxIteration = Collections.max(iterationNumbers);

        // save a list of reward scores per iteration
        for (int i = 0; i < maxIteration; i++) {
            int index = i;
            List<RewardScore> thisIterationRewardScores = new ArrayList<>();
            allRewardScoresLists.stream().forEach(rewardScoresList -> {
                if (rewardScoresList.size() > index) {
                    thisIterationRewardScores.add(rewardScoresList.get(index));
                } else {
                    thisIterationRewardScores.add(null);
                }
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

    public void setPolicyChart(List<Policy> updatedPolicies) {
        if (updatedPolicies == null) {
            return;
        }

        String type = "line";
        Boolean showTooltip = true;
        String hAxisTitle = "Iteration";
        String vAxisTitle = "Mean Reward Score over All Episodes";
        Boolean curveLines = true;
        String seriesType = null;
        Boolean stacked = null;
        JsonObject series = null; // createSeries();
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
        updateData(updatedPolicies, generatePolicyChartData(updatedPolicies));
    }
    
}
