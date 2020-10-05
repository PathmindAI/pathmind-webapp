package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("cookie-consent-box")
@CssImport("./styles/components/cookie-consent.css")
@JsModule("./src/components/molecules/cookie-consent-box.js")
public class CookieConsentBox extends PolymerTemplate<TemplateModel> {
    public CookieConsentBox() {
    }
}