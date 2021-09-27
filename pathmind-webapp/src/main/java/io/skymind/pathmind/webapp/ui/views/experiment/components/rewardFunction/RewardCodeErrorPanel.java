package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("reward-code-error-panel")
@JsModule("./src/experiment/reward-code-error-panel.ts")
public class RewardCodeErrorPanel extends LitTemplate {
    public RewardCodeErrorPanel() {
    }

    public void setErrors(String errors) {
        getElement().setProperty("errorText", errors);
    }

    public void clearErrors(){
        getElement().setProperty("errorText", "");
    }

}
