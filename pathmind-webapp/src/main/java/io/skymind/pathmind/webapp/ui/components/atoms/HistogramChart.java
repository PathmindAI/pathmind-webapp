package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import elemental.json.Json;
import elemental.json.JsonArray;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ChartUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@Tag("histogram-chart")
@JsModule("./src/components/atoms/histogram-chart.ts")
public class HistogramChart extends LitTemplate implements HasStyle {
    private List<RewardVariable> selectedRewardVariables;
    private Policy bestPolicy;

    private List<String> colors = ChartUtils.colors();

    public HistogramChart() {
        super();
    }

    public void setupChart(
            String title,
            String hAxisTitle,
            String vAxisTitle,
            List<String> colors
    ) {
        getElement().setProperty("title", title);
        getElement().setProperty("haxistitle", hAxisTitle);
        getElement().setProperty("vaxistitle", vAxisTitle);
        getElement().setPropertyJson("colors", createJsonStringArray(colors));
    }

    private void setData(JsonArray cols, JsonArray rows) {// JsonObject and JsonArray are not allowed types for TemplateModel methods
        // JsonObject and JsonArray are not allowed types for TemplateModel methods
        // So we have to set it through calling the JS function
        getElement().callJsFunction("setData", cols, rows);
    }

    public void setChartEmpty() {
        this.setupChart("", "Value", "Count", List.of("navy"));
        getElement().callJsFunction("setChartEmpty");
    }

    public void setHistogramData(List<RewardVariable> selectedRewardVariables, Policy bestPolicy, Boolean showDetails) {
        this.selectedRewardVariables = selectedRewardVariables;
        this.bestPolicy = bestPolicy;

        updateData(showDetails);
    }

    private void updateData(Boolean showDetails) {
        PolicyUtils.updateSimulationMetricsData(bestPolicy);
        List<MetricsRaw> metricsRawList = bestPolicy.getMetricsRaws();

        if (selectedRewardVariables.size() > 0 && (metricsRawList != null && metricsRawList.size() > 0)) {
            // (k: index, v: meanValueList)
            Map<Integer, List<Double>> uncertaintyMap = bestPolicy.getMetricsRaws().stream()
                .collect(groupingBy(MetricsRaw::getIndex,
                    mapping(MetricsRaw::getValue, Collectors.toList())
                    )
                );

            JsonArray cols = createCols();
            JsonArray rows = createRows(uncertaintyMap);

            List<String> selectedColors = selectedRewardVariables.stream()
                .map(r -> colors.get(r.getArrayIndex() % 10))
                .collect(Collectors.toList());

            if (showDetails) {
                this.setupChart(null, "Value", "Count", selectedColors);
            } else {
                this.setupChart(null, null, null, selectedColors);
            }
            this.setData(cols, rows);
        } else {
            this.setChartEmpty();
        }
    }

    private JsonArray createCols() {
        JsonArray cols = Json.createArray();
        selectedRewardVariables.forEach(r -> {
            cols.set(cols.length(), Json.parse("{\"label\":\"" + r.getName() + "\", \"type\":\"number\"}"));
        });

        return cols;
    }

    private JsonArray createRows(Map<Integer, List<Double>> uncertaintyMap) {
        JsonArray rows = Json.createArray();
        Set<Integer> keys = selectedRewardVariables.stream().map(RewardVariable::getArrayIndex).collect(Collectors.toCollection(HashSet::new));

        List<List<Double>> histogramData = uncertaintyMap.entrySet().stream()
            .filter(e -> keys.contains(e.getKey()))
            .map(e -> e.getValue())
            .collect(Collectors.toList());

        List<Double> values = histogramData.get(0);
        for (int i = 0; i < values.size(); i++) {
            int finalI = i;
            List<Double> arr = new ArrayList<>();
            histogramData.forEach(list -> arr.add(list.get(finalI)));
            rows.set(rows.length(), createJsonDoubleArray(arr));
        }
        return rows;
    }

    private JsonArray createJsonStringArray(List<String> list) {
        JsonArray jsonArray = Json.createArray();
        for (int i = 0; i < list.size(); i++) {
            jsonArray.set(i, list.get(i));
        }
        return jsonArray;
    }

    private JsonArray createJsonDoubleArray(List<Double> list) {
        JsonArray jsonArray = Json.createArray();
        for (int i = 0; i < list.size(); i++) {
            jsonArray.set(i, list.get(i));
        }
        return jsonArray;
    }

    public void redraw() {
        getElement().callJsFunction("redraw");
    }
}
