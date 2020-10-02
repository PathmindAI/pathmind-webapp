package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class ExperimentViewRunUpdateSubscriber extends RunUpdateSubscriber {

    private Experiment experiment;

    private ExperimentView experimentView;

    public ExperimentViewRunUpdateSubscriber(ExperimentView experimentView, Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
        this.experimentView = experimentView;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        // STEPH -> Do we actually need to do anything if it's not the same experiment?
        experiment.setTrainingStatusEnum(event.getRun().getExperiment().getTrainingStatusEnum());
        ExperimentUtils.addOrUpdateRun(experiment, event.getRun());
        ExperimentUtils.updatedRunForPolicies(experiment, event.getRun());
        PushUtils.push(getUiSupplier(), () -> {
            experimentView.setPolicyChartVisibility();
            experimentView.updateDetailsForExperiment();
        });
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return isSameExperiment(event) || (!experiment.isArchived() && ExperimentUtils.isSameModel(experiment, event.getModelId()));
    }

    private boolean isSameExperiment(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experiment, event.getRun().getExperiment());
    }
}
