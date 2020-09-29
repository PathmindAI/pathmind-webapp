package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("goals-reached-status")
@JsModule("./src/components/atoms/goals-reached-status.js")
public class GoalsReachedStatus extends PolymerTemplate<TemplateModel> implements HasStyle {
    public GoalsReachedStatus(boolean goalsReached) {
        super();
        setValue(goalsReached);
    }

    public void setValue(boolean goalsReached) {
        getElement().setProperty("reached", goalsReached);
    }

    public void setSize(String size) {
        getElement().setAttribute("size", size);
    }
}