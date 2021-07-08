package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;

@Tag("status-icon")
@JsModule("./src/components/atoms/status-icon.ts")
public class StatusIcon extends LitTemplate {

    String statusIconType;

    public StatusIcon(Experiment experiment) {
        RunStatus overallExperimentStatus = experiment.getTrainingStatusEnum();
        statusIconType = getIconStatus(experiment, overallExperimentStatus);
        getElement().setProperty("status", statusIconType);
        getElement().setProperty("statustext", overallExperimentStatus.toString());
    }

    private String getIconStatus(Experiment experiment, RunStatus status) {
        return ExperimentGuiUtils.getIconStatus(experiment, status);
    }

    public String getStatusIconType() {
        return statusIconType;
    }
}