package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
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
@JsModule("./src/components/atoms/histogram-chart.js")
public class HistogramChart extends PolymerTemplate<HistogramChart.Model> implements HasStyle {
    private List<RewardVariable> selectedRewardVariables;
    private Policy bestPolicy;

    private List<String> colors = ChartUtils.colors();

    public HistogramChart() {
        super();
    }

    public void setupChart(
            String hAxisTitle,
            String vAxisTitle,
            List<String> colors,
            JsonObject viewWindow
    ) {
        getModel().setHaxistitle(hAxisTitle);
        getModel().setVaxistitle(vAxisTitle);
        getModel().setColors(colors);
    }

    public void setData(JsonArray cols, JsonArray rows) {
        // JsonObject and JsonArray are not allowed types for TemplateModel methods
        // So we have to set it through calling the JS function
        getElement().callJsFunction("setData", cols, rows);
        redraw();
    }

    public void setChartEmpty() {
        this.setupChart("Value", "Count", List.of("navy"), null);
        getElement().callJsFunction("setChartEmpty");
        redraw();
    }

    public void setHistogramData(List<RewardVariable> selectedRewardVariables, Policy bestPolicy) {
        this.selectedRewardVariables = selectedRewardVariables;
        this.bestPolicy = bestPolicy;

        updateData();
    }

    private void updateData() {
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

            this.setupChart("Value", "Count", selectedColors, null);
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
            rows.set(rows.length(), createRowItem(arr));
        }
        return rows;
    }

    private JsonArray createRowItem(List<Double> rowValues) {
        JsonArray rowItem = Json.createArray();
        for (int i = 0; i < rowValues.size(); i++) {
            rowItem.set(i, rowValues.get(i));
        }
        return rowItem;
    }

    public void redraw() {
        getElement().callJsFunction("redraw");
    }

    public interface Model extends TemplateModel {

        void setHaxistitle(String hAxisTitle);

        void setVaxistitle(String vAxisTitle);

        void setColors(List<String> colors);

    }
}
