package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;

@Tag("elapsed-timer")
@JsModule("/src/experiment/elapsed-timer.js")
public class ElapsedTimer extends Span{

    public void updateTimer(long time, boolean isRunning) {
        getElement().callJsFunction("updateTimer", String.valueOf(time), isRunning);
    }
}