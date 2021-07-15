package io.skymind.pathmind.webapp.ui.views.experiment.components.policy;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("policy-server-live-content")
@JsModule("./src/experiment/policy-server-live-content.ts")
public class PolicyServerLiveContent extends LitTemplate {
    public PolicyServerLiveContent(String url) {
        super();
        getElement().setProperty("url", url);
    }

    public void setUserApiKey(String apiKey) {
        getElement().setProperty("userApiKey", apiKey);
    }
}