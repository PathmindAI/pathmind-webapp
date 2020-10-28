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
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.bus.subscribers.view.RewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
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

    private void selectBestPolicy() {
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

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this,
                new AllMetricsChartPanelPolicyUpdateSubscriber(getUISupplier),
                new AllMetricsChartPanelRewardVariableSelectedViewSubscriber(getUISupplier));
    }

    private void pushChartUpdate(Supplier<Optional<UI>> getUISupplier) {
        PushUtils.push(getUISupplier, () -> {
            updateChartData();
            redrawChart();
        });
    }

    class AllMetricsChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

        public AllMetricsChartPanelPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(PolicyUpdateBusEvent event) {
            synchronized (experimentLock) {
                // We need to check after the lock is acquired as changing experiments can take up to several seconds.
                if (event.getExperimentId() != experiment.getId())
                    return;

                ExperimentUtils.addOrUpdatePolicies(experiment, event.getPolicies());
                selectBestPolicy();
                // if(bestPolicy != null)
                pushChartUpdate(getUiSupplier());
            }
        }

        @Override
        public boolean filterBusEvent(PolicyUpdateBusEvent event) {
            return experiment.getId() == event.getExperimentId();
        }
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

