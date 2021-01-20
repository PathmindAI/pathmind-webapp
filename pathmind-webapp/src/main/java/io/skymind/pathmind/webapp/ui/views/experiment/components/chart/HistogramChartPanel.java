package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.atoms.HistogramChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main.HistogramChartPanelPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view.HistogramChartPanelRewardVariableSelectedViewSubscriber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.utils.UIConstants.DEFAULT_SELECTED_METRICS_FOR_CHART;

@Slf4j
public class HistogramChartPanel extends VerticalLayout {
    @Getter
    private Object experimentLock = new Object();

    private HistogramChart chart = new HistogramChart();

    @Getter
    private Experiment experiment;

    private Supplier<Optional<UI>> getUISupplier;
    @Getter
    private Map<Long, RewardVariable> rewardVariableFilters;

    List<RewardVariable> rewardVariables;

    public HistogramChartPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        this.rewardVariableFilters = new ConcurrentHashMap<>();
        add(chart);
        setPadding(false);
        setSpacing(false);
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier,
            new HistogramChartPanelPolicyUpdateSubscriber(this),
            new HistogramChartPanelRewardVariableSelectedViewSubscriber(this));
    }

    public void setupChart(Experiment newExperiment, List<RewardVariable> rewardVariables) {
        synchronized (experimentLock) {
            this.experiment = newExperiment.deepClone();
            this.rewardVariables = rewardVariables;

            long numberOfSelectedRewardVariables = rewardVariableFilters.values().stream().filter(rv -> rv != null).count();
            if (numberOfSelectedRewardVariables == 0) {
                rewardVariables.stream().forEach(rewardVariable -> {
                    if (rewardVariable.getArrayIndex() < DEFAULT_SELECTED_METRICS_FOR_CHART) {
                        rewardVariableFilters.putIfAbsent(rewardVariable.getId(), rewardVariable.deepClone());
                    }
                });
            }

            updateChart();
        }
    }

    public void updateChart() {
        Optional<Policy> opt = PolicyUtils.selectBestPolicy(this.experiment);
        if (opt.isPresent()) {
            chart.setHistogramData(new ArrayList<>(rewardVariableFilters.values()), opt.get());
        } else {
            chart.setChartEmpty();
        }

        redrawChart();
    }

    public void redrawChart() {
        chart.redraw();
    }
}
