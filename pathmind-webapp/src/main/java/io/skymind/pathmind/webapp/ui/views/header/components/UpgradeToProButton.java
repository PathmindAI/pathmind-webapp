package io.skymind.pathmind.webapp.ui.views.header.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("upgrade-to-pro-button")
@JsModule("./src/components/header/upgrade-to-pro-button.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpgradeToProButton extends LitTemplate {
}
