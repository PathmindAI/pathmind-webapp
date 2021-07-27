package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("cookie-consent-box")
@CssImport("./styles/components/cookie-consent.css")
@JsModule("./src/components/molecules/cookie-consent-box.ts")
public class CookieConsentBox extends LitTemplate {
}