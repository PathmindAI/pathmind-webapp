package io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

@Tag("code-viewer")
@JsModule("./src/experiment/code-viewer.ts")
public class CodeViewer extends LitTemplate implements HasStyle, ExperimentComponent {
    public CodeViewer() {
        super();
    }

    public CodeViewer(Experiment experiment) {
        this(experiment, false, true);
    }

    public CodeViewer(Experiment experiment, Boolean showCopyButton, Boolean showBorder) {
        super();
        setExperiment(experiment);
        getElement().setProperty("showCopyButton", showCopyButton);
        getElement().setProperty("showBorder", showBorder);
    }

    public void setExperiment(Experiment experiment) {
        setValue(experiment.getRewardFunction());
    }

    public void setComparisonModeTheOtherRewardFunction(String comparisonModeTheOtherRewardFunction) {
        getElement().setProperty("comparisonCodeSnippet", comparisonModeTheOtherRewardFunction);
    }

    public void setValue(String rewardFunction) {
        getElement().setProperty("codeSnippet", rewardFunction);
    }
}