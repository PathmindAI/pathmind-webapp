package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

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
import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.RewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.AllMetricsChartPanelPolicyUpdateSubscriber;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
public class AllMetricsChartPanel extends VerticalLayout
{
    private Object experimentLock = new Object();

    private AllMetricsChart chart = new AllMetricsChart();

    private Experiment experiment;
    private Policy bestPolicy;
    private Map<Long, RewardVariable> rewardVariableFilters;

    private Supplier<Optional<UI>> getUISupplier;

    public AllMetricsChartPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        rewardVariableFilters = new ConcurrentHashMap<>();
        add(hintMessage(), chart);
        setPadding(false);
        setSpacing(false);
    }

    private Paragraph hintMessage() {
        Paragraph hintMessage = new Paragraph(VaadinIcon.INFO_CIRCLE_O.create());
        hintMessage.add(
            "You can click on the simulation metric names above to toggle the lines on this chart."
        );
        hintMessage.addClassName("hint-label");
        return hintMessage;
    }

    public void setupChart(Experiment experiment, List<RewardVariable> rewardVariables) {
        synchronized (experimentLock) {
            this.experiment = experiment.deepClone();
            rewardVariables.stream().forEach(rewardVariable ->
                    rewardVariableFilters.putIfAbsent(rewardVariable.getId(), rewardVariable.deepClone()));
            selectBestPolicy();
            updateChartData();
            redrawChart();
        }
    }

    public void selectBestPolicy() {
        bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);
        PolicyUtils.updateSimulationMetricsData(bestPolicy);
    }

    private void updateChartData() {
        List<RewardVariable> filteredAndSortedList = new ArrayList<>(rewardVariableFilters.values());
        chart.setAllMetricsChart(filteredAndSortedList, bestPolicy);
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

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this,
                new AllMetricsChartPanelPolicyUpdateSubscriber(getUISupplier, this),
                new AllMetricsChartPanelRewardVariableSelectedViewSubscriber(getUISupplier));
    }

    public void pushChartUpdate(Supplier<Optional<UI>> getUISupplier) {
        PushUtils.push(getUISupplier, () -> {
            updateChartData();
            redrawChart();
        });
    }

    class AllMetricsChartPanelRewardVariableSelectedViewSubscriber extends RewardVariableSelectedViewSubscriber {

        public AllMetricsChartPanelRewardVariableSelectedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(RewardVariableSelectedViewBusEvent event) {
            if(event.isShow()) {
                rewardVariableFilters.putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
            } else {
                rewardVariableFilters.remove(event.getRewardVariable().getId());
            }

            pushChartUpdate(getUiSupplier());
        }
    }
}

