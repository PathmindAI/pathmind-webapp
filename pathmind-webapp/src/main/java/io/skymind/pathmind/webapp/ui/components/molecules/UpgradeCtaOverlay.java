package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import io.skymind.pathmind.webapp.ui.views.account.AccountUpgradeView;

@Tag("upgrade-cta-overlay")
@JsModule("./src/components/molecules/upgrade-cta-overlay.ts")
public class UpgradeCtaOverlay extends LitTemplate {
    
    @Id("upgradeBtn")
    private Button upgradeBtn;

    public UpgradeCtaOverlay(String featureName, SegmentIntegrator segmentIntegrator) {
        getElement().setProperty("featureName", featureName);
        upgradeBtn.addClickListener(click -> {
            segmentIntegrator.navigatedToPricingFromNewExpViewSimulationParameters();
            getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class));
        });
    }

    public void open() {
        getElement().setProperty("opened", true);
    }

    public void close() {
        getElement().setProperty("opened", false);
    }
}
