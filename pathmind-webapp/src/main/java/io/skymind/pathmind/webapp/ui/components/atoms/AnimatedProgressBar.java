package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@Tag("animated-progress-bar")
@JsModule("./src/components/atoms/animated-progress-bar.js")
public class AnimatedProgressBar extends LitTemplate {

    public AnimatedProgressBar(int duration) {
        // duration is in seconds
        getElement().setProperty("duration", duration);
    }

}
