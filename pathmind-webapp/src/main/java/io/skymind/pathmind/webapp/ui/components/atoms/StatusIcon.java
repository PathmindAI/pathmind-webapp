package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;

@Tag("status-icon")
@JsModule("/src/components/atoms/status-icon.js")
public class StatusIcon extends PolymerTemplate<StatusIcon.Model> {
    public StatusIcon(Experiment experiment) {
        RunStatus overallExperimentStatus = experiment.getTrainingStatusEnum();
        getModel().setStatus(getIconStatus(experiment, overallExperimentStatus));
        getModel().setStatusText(overallExperimentStatus.toString());
    }

    private String getIconStatus(Experiment experiment, RunStatus status) {
        return ExperimentUtils.getIconStatus(experiment, status);
    }

    public interface Model extends TemplateModel {
        void setStatus(String iconStatus);

        void setStatusText(String statusText);
    }
}