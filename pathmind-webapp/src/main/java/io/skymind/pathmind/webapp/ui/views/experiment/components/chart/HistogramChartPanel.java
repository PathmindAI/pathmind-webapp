package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import elemental.json.Json;
import elemental.json.JsonArray;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.atoms.HistogramChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view.HistogramChartPanelRewardVariableSelectedViewSubscriber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.skymind.pathmind.webapp.ui.utils.UIConstants.DEFAULT_SELECTED_METRICS_FOR_CHART;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@Slf4j
public class HistogramChartPanel extends VerticalLayout {
    private Object experimentLock = new Object();

    private HistogramChart chart = new HistogramChart();

    private Experiment experiment;

    private Supplier<Optional<UI>> getUISupplier;
    @Getter
    private Map<Long, RewardVariable> metricsData;
//    private Map<Long, RewardVariable> rewardVariableFilters;

    List<RewardVariable> rewardVariables;

    public HistogramChartPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        this.metricsData = new ConcurrentHashMap<>();
        add(chart);
        setPadding(false);
        setSpacing(false);

        chart.setupChart("value", "frequency", null);
        chart.setChartEmpty();
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier,
//            new CompareMetricsChartPanelPolicyUpdateSubscriber(this),
            new HistogramChartPanelRewardVariableSelectedViewSubscriber(this));
    }

    public void setupChart(Experiment newExperiment, List<RewardVariable> rewardVariables) {
        synchronized (experimentLock) {
            this.experiment = newExperiment.deepClone();
            this.rewardVariables = rewardVariables;

            long numberOfSelectedRewardVariables = metricsData.values().stream().filter(rv -> rv != null).count();
            if (numberOfSelectedRewardVariables == 0) {
                rewardVariables.stream().forEach(rewardVariable -> {
                    if (rewardVariable.getArrayIndex() < DEFAULT_SELECTED_METRICS_FOR_CHART) {
                        metricsData.putIfAbsent(rewardVariable.getId(), rewardVariable.deepClone());
                    }
                });
            }

            updateChart();
        }
    }

    public void updateChart() {
//        chart.setPolicyChart(experiment);

        log.info("kepricondebug histogram panel : {}", this.experiment.getId());
        if (metricsData.size() > 0) {
            Policy policy = PolicyUtils.selectBestPolicy(this.experiment).get();

            List<MetricsRaw> metricsRawList = policy.getMetricsRaws();

            Map<Integer, List<Double>> uncertaintyMap = null;
            if (metricsRawList != null && metricsRawList.size() > 0) {
                // (k: index, v: meanValueList)
                uncertaintyMap = policy.getMetricsRaws().stream()
                    .collect(groupingBy(MetricsRaw::getIndex,
                        mapping(MetricsRaw::getValue, Collectors.toList())
                        )
                    );

                policy.setUncertainty(uncertaintyMap.values().stream()
                    .map(list -> PathmindNumberUtils.calculateUncertainty(list))
                    .collect(Collectors.toList()));
            }

            JsonArray cols = createCols();
            JsonArray rows = createRows(uncertaintyMap);

            chart.setData(cols, rows);
        } else {
            chart.setChartEmpty();
        }

        redrawChart();
    }

    private JsonArray createCols() {
        JsonArray cols = Json.createArray();
        metricsData.values().forEach(r -> {
            cols.set(cols.length(), Json.parse("{\"label\":\"" + r.getName() + "\", \"type\":\"number\"}"));
        });

        return cols;
    }

    private JsonArray createRows(Map<Integer, List<Double>> uncertaintyMap) {
        JsonArray rows = Json.createArray();
        List<Integer> keys = metricsData.values().stream().map(RewardVariable::getArrayIndex).collect(Collectors.toList());

        List<List<Double>> histogramData = uncertaintyMap.entrySet().stream()
            .filter(e -> keys.contains(e.getKey()))
            .map(e -> e.getValue())
            .collect(Collectors.toList());


        List<Double> values = histogramData.get(0);
        log.info("kepricondebug histogramData : {}", histogramData);
        log.info("kepricondebug values : {}", values);

//        values.forEach(v -> rows.set(rows.length(), createRowItem(v)));
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

    public void redrawChart() {
        log.info("kepricondebug redraw : {}", this.metricsData);
        chart.redraw();
    }


}
