package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("elapsed-timer")
@JsModule("/src/experiment/elapsed-timer.js")
public class ElapsedTimer extends PolymerTemplate<TemplateModel> implements HasStyle {

    public void updateTimer(long time, boolean isRunning) {
        getElement().callJsFunction("updateTimer", String.valueOf(time), isRunning);
    }
}