package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;

@Tag("status-icon")
@JsModule("./src/components/atoms/status-icon.js")
public class StatusIcon extends PolymerTemplate<StatusIcon.Model> {

    String statusIconType;

    public StatusIcon(Experiment experiment) {
        RunStatus overallExperimentStatus = experiment.getTrainingStatusEnum();
        statusIconType = getIconStatus(experiment, overallExperimentStatus);
        getModel().setStatus(statusIconType);
        getModel().setStatusText(overallExperimentStatus.toString());
    }

    private String getIconStatus(Experiment experiment, RunStatus status) {
        return ExperimentGuiUtils.getIconStatus(experiment, status);
    }

    public String getStatusIconType() {
        return statusIconType;
    }

    public interface Model extends TemplateModel {
        void setStatus(String iconStatus);

        void setStatusText(String statusText);
    }
}