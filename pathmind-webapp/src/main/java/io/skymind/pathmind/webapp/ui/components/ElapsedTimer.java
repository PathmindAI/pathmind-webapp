package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("elapsed-timer")
@JsModule("./src/experiment/elapsed-timer.ts")
public class ElapsedTimer extends LitTemplate {
    public void updateTimer(long time, boolean isRunning) {
        getElement().callJsFunction("updateTimer", String.valueOf(time), isRunning);
    }
}