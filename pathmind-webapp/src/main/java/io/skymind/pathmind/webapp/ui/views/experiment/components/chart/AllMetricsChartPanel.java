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
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AllMetricsChartPanel extends VerticalLayout
{
    private Object experimentLock = new Object();

    private AllMetricsChart chart = new AllMetricsChart();

    private Experiment experiment;

    private Supplier<Optional<UI>> getUISupplier;

    public AllMetricsChartPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
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

    // TODO -> STEPH -> The sparkline should maybe be calculated as part of selectBestPolicy? We almost always have to do it together anyways don't we?
    public void setupChart(Experiment experiment, List<RewardVariable> rewardVariables) {
        synchronized (experimentLock) {
            this.experiment = experiment;
            Policy bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);
            PolicyUtils.updateSimulationMetricsData(bestPolicy);
            chart.setAllMetricsChart(rewardVariables, bestPolicy);
        }
    }

    public void updateRewardVariables(List<RewardVariable> rewardVariables) {
        chart.updateSelectedRewardVariables(rewardVariables);
        chart.updateData();
    }

    private void updateBestPolicy(Policy bestPolicy) {
        chart.updateBestPolicy(bestPolicy);
        chart.updateData();
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
        EventBus.subscribe(this, new AllMetricsChartPanelPolicyUpdateSubscriber(getUISupplier));
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
                PolicyUtils.selectBestPolicy(experiment.getPolicies())
                        .ifPresent(bestPolicy -> PushUtils.push(getUiSupplier(), () -> updateBestPolicy(bestPolicy)));
            }
        }

        @Override
        public boolean filterBusEvent(PolicyUpdateBusEvent event) {
            return experiment.getId() == event.getExperimentId();
        }
    }
}

