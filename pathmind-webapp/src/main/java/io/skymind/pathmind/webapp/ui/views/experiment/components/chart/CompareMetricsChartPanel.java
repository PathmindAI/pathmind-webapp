package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main.CompareMetricsChartPanelPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view.CompareMetricsChartPanelExperimentRewardVariableSelectedViewSubscriber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompareMetricsChartPanel extends VerticalLayout {
    private Object experimentLock = new Object();

    private CompareMetricsChart chart = new CompareMetricsChart();

    private Experiment experiment;
    private Map<Long, RewardVariable> rewardVariableFilters;

    private Supplier<Optional<UI>> getUISupplier;

    public CompareMetricsChartPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        rewardVariableFilters = new ConcurrentHashMap<>();
        add(hintMessage(), chart);
        setPadding(false);
        setSpacing(false);
    }

    private Paragraph hintMessage() {
        Paragraph hintMessage = new Paragraph(VaadinIcon.INFO_CIRCLE_O.create());
        hintMessage.add(
                "Select any two metrics on the simulation metric names above for comparison."
        );
        hintMessage.addClassName("hint-label");
        return hintMessage;
    }

    public void setupChart(Experiment experiment) {
        synchronized (experimentLock) {
            this.experiment = experiment;
            long numberOfSelectedRewardVariables = rewardVariableFilters.values().stream().filter(rv -> rv != null).count();
            if (numberOfSelectedRewardVariables == 0) {
                experiment.getRewardVariables().stream().forEach(rewardVariable -> {
                    if (rewardVariable.getArrayIndex() < 2) {
                        rewardVariableFilters.putIfAbsent(rewardVariable.getId(), rewardVariable);
                    }
                });
            }
            updateChart();
        }
    }

    public void redrawChart() {
        chart.redraw();
    }

    public Object getExperimentLock() {
        return experimentLock;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getExperimentId() {
        return experiment.getId();
    }

    public Map getRewardVariableFilters() {
        return rewardVariableFilters;
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier,
                new CompareMetricsChartPanelPolicyUpdateSubscriber(this),
                new CompareMetricsChartPanelExperimentRewardVariableSelectedViewSubscriber(this));
    }

    public void updateChart() {
        // Update chart data
        List<RewardVariable> filteredAndSortedList = new ArrayList<>(rewardVariableFilters.values());
        chart.setCompareMetricsChart(filteredAndSortedList, experiment.getBestPolicy());

        redrawChart();
    }
}

