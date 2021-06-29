package io.skymind.pathmind.webapp.ui.atoms;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

@SpringComponent
@UIScope
@Tag("animated-progress-bar")
@JsModule("./src/atoms/animated-progress-bar.js")
public class AnimatedProgressBar extends LitTemplate {

    public AnimatedProgressBar(int duration) {
    }

}
