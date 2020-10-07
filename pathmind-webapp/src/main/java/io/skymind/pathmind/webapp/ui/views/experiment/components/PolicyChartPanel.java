package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;

import java.util.Optional;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class PolicyChartPanel extends VerticalLayout
{
    private Object experimentLock = new Object();

    private PolicyChart chart = new PolicyChart();

    private Experiment experiment;

    public PolicyChartPanel() {
        add(LabelFactory.createLabel("Learning Progress", BOLD_LABEL), chart);
        setPadding(false);
        setSpacing(false);
    }

    // REFACTOR -> STEPH -> hotfix until it's properly fixed as part of 2155: https://github.com/SkymindIO/pathmind-webapp/issues/2155
    public void updateChart(Experiment experiment, Policy bestPolicy) {
        setExperiment(experiment, bestPolicy);
    }

    public void setExperiment(Experiment experiment, Policy bestPolicy) {
        synchronized (experimentLock) {
            this.experiment = experiment;
            if (experiment.getPolicies().size() > 0) {
                chart.setPolicyChart(experiment.getPolicies(), bestPolicy);
            } else {
                chart.setChartEmpty();
            }
        }
    }
    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, new PolicyChartPanelPolicyUpdateSubscriber(() -> getUI()));
    }

    class PolicyChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

        public PolicyChartPanelPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(PolicyUpdateBusEvent event) {
            synchronized (experimentLock) {
                // We need to check after the lock is acquired as changing experiments can take up to several seconds.
                if (event.getExperimentId() != experiment.getId())
                    return;
                ExperimentUtils.addOrUpdatePolicies(experiment, event.getPolicies());
                Policy bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies());
                PushUtils.push(getUiSupplier(), () -> updateChart(experiment, bestPolicy));
            }
        }

        @Override
        public boolean filterBusEvent(PolicyUpdateBusEvent event) {
            return experiment.getId() == event.getExperimentId();
        }

    }
}

