package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("elapsed-timer")
@JsModule("/src/experiment/elapsed-timer.js")
public class ElapsedTimer extends PolymerTemplate<TemplateModel> implements HasStyle {

	@Id("time-text")
	private Span timeText;
	
    public void updateTimer(long time, boolean isRunning) {
        getElement().callJsFunction("updateTimer", String.valueOf(time), isRunning);
    }
    
    public void setTextAlignment(String alignment) {
    	timeText.getStyle().set("text-align", alignment);
    }
}