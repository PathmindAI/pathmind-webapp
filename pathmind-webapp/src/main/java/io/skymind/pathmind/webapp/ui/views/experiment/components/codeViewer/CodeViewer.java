package io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

@Tag("code-viewer")
@JsModule("./src/experiment/code-viewer.js")
public class CodeViewer extends PolymerTemplate<CodeViewer.Model> implements HasStyle, ExperimentComponent {

    public CodeViewer() {
        super();
    }

    public CodeViewer(Experiment experiment) {
        this(experiment, false, true);
    }

    public CodeViewer(Experiment experiment, Boolean showCopyButton, Boolean showBorder) {
        super();
        setExperiment(experiment);
        getModel().setShowCopyButton(showCopyButton);
        getModel().setShowBorder(showBorder);
    }

    public void setExperiment(Experiment experiment) {
        setValue(experiment.getRewardFunction());
    }

    public void setValue(String rewardFunction) {
        getModel().setCodeSnippet(rewardFunction);
    }

    public interface Model extends TemplateModel {
        void setCodeSnippet(String codeSnippet);

        void setShowCopyButton(Boolean showCopyButton);

        void setShowBorder(Boolean showBorder);
    }
}