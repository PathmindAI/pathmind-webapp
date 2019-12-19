package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@UIScope
@Tag("elapsed-timer")
@JsModule("/src/experiment/elapsed-timer.js")
@Slf4j
class ElapsedTimer extends Label {

    void updateTimer(String functionName, long time) {
        getElement().callJsFunction(functionName, String.valueOf(time));
    }
}