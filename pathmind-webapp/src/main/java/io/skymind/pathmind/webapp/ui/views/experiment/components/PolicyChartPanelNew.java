package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.utils.ChartUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class PolicyChartPanelNew extends VerticalLayout
{
    private Object experimentLock = new Object();

    private PolicyChart chart = new PolicyChart();

    private Experiment experiment;

    private Policy bestPolicy;

    public PolicyChartPanelNew() {
        add(LabelFactory.createLabel("Learning Progress", BOLD_LABEL), chart);
        setPadding(false);
        setSpacing(false);
    }

    public void setExperiment(Experiment experiment, Policy bestPolicy) {
        synchronized (experimentLock) {
            this.experiment = experiment;
            updateChart(experiment.getPolicies(), bestPolicy);
        }
    }

    private void updateChart(List<Policy> policies, Policy bestPolicy) {
        // TODO -> Do we need to keep experiment up to date if there are new policies, etc.? I don't believe it's necessary
        // but we should confirm it.
        // During a training run, additional policies will be created, i.e. for a discovery run, the policies will
        // be created as they actually start training. -- pdubs, 20190927
        if (bestPolicy != null) {
            this.bestPolicy = bestPolicy;
        }
        chart.setPolicyChart(policies, bestPolicy);
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/129 -> Does not seem possible yet: https://vaadin.com/forum/thread/17856633/is-it-possible-to-highlight-a-series-in-a-chart-programmatically
    public void highlightPolicy(Policy policy) {
    	// chart.getConfiguration().getSeries().stream().forEach(series -> {
    	// 	if (series.getId().equals(Long.toString(policy.getId()))) {
    	// 		series.setPlotOptions(createActiveSeriesPlotOptions());
    	// 	}
    	// });
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
                PushUtils.push(getUiSupplier(), () -> updateChart(event.getPolicies(), null));
            }
        }

        @Override
        public boolean filterBusEvent(PolicyUpdateBusEvent event) {
            return experiment.getId() == event.getExperimentId();
        }

    }
}

