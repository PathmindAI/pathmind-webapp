package io.skymind.pathmind.webapp.ui.components.codeViewer;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.codeViewer.subscribers.CodeViewerExperimentSwitchedViewSubscriber;

@Tag("code-viewer")
@JsModule("./src/experiment/code-viewer.js")
public class CodeViewer extends PolymerTemplate<TemplateModel> implements HasStyle {

    private Supplier<Optional<UI>> getUISupplier;

    public CodeViewer(Supplier<Optional<UI>> getUISupplier, Experiment experiment) {
        super();
        this.getUISupplier = getUISupplier;
        setExperiment(experiment);
    }

    public void setExperiment(Experiment experiment) {
        setValue(experiment.getRewardFunction());
    }

    public void setValue(String rewardFunction) {
        getElement().callJsFunction("setValue", rewardFunction);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this, getUISupplier,
                new CodeViewerExperimentSwitchedViewSubscriber(this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }
}