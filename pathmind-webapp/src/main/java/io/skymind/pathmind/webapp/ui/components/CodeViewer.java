package io.skymind.pathmind.webapp.ui.components;

import java.util.Optional;
import java.util.function.Supplier;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("code-viewer")
@JsModule("./src/experiment/code-viewer.js")
public class CodeViewer extends PolymerTemplate<TemplateModel> implements HasStyle {

    public CodeViewer(Experiment experiment) {
        super();
        setExperiment(experiment);
    }

    public void setExperiment(Experiment experiment) {
        setValue(experiment.getRewardFunction());
    }

    public void setValue(String rewardFunction) {
        getElement().callJsFunction("setValue", rewardFunction);
    }

    class CodeViewerExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

        public CodeViewerExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentChangedViewBusEvent event) {
            PushUtils.push(getUiSupplier().get(), ui -> {
                    setExperiment(event.getExperiment());
            });
        }
    }

}