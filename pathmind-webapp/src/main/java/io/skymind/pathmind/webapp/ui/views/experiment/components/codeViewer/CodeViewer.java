package io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

@Tag("code-viewer")
@JsModule("./src/experiment/code-viewer.js")
public class CodeViewer extends PolymerTemplate<TemplateModel> implements HasStyle, ExperimentComponent {

    private Supplier<Optional<UI>> getUISupplier;

    public CodeViewer(Supplier<Optional<UI>> getUISupplier) {
        super();
        this.getUISupplier = getUISupplier;
    }

    public void setExperiment(Experiment experiment) {
        setValue(experiment.getRewardFunction());
    }

    public void setValue(String rewardFunction) {
        getElement().callJsFunction("setValue", rewardFunction);
    }
}