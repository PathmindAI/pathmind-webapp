package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Label;

@Tag("elapsed-timer")
@JsModule("/src/experiment/elapsed-timer.js")
public class ElapsedTimer extends Label{

    public void updateTimer(long time, boolean isRunning) {
        getElement().callJsFunction("updateTimer", String.valueOf(time), isRunning);
    }
}