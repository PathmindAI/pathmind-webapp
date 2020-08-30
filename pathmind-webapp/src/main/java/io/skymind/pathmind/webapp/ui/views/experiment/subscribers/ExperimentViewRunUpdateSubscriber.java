package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ExperimentViewRunUpdateSubscriber extends RunUpdateSubscriber {

    private List<Experiment> experiments;
    private Experiment experiment;

    private ExperimentView experimentView;

    public ExperimentViewRunUpdateSubscriber(ExperimentView experimentView, Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
        this.experimentView = experimentView;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public void setExperiments(List<Experiment> experiments) {
        this.experiments = experiments;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        if (isSameExperiment(event)) {
            ExperimentUtils.addOrUpdateRun(experiment, event.getRun());
            ExperimentUtils.updatedRunForPolicies(experiment, event.getRun());
            PushUtils.push(getUiSupplier(), () -> {
                experimentView.setPolicyChartVisibility();
                experimentView.updateDetailsForExperiment();
            });
        } else if (ExperimentUtils.isNewExperimentForModel(event.getRun().getExperiment(), experiments, experiment.getModelId())) {
            experimentView.updateExperimentComponents();
        }
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return isSameExperiment(event) || (!experiment.isArchived() && ExperimentUtils.isSameModel(experiment, event.getModelId()));
    }

    private boolean isSameExperiment(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experiment, event.getRun().getExperiment());
    }
}
