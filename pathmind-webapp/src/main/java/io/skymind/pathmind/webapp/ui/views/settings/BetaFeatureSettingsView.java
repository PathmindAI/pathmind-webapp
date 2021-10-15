package io.skymind.pathmind.webapp.ui.views.settings;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.BETA_SETTINGS, layout = MainLayout.class)
public class BetaFeatureSettingsView extends PathMindDefaultView {

    private final BetaFeatureSettingsViewContent betaFeatureSettingsViewContent;

    @Autowired
    public BetaFeatureSettingsView(BetaFeatureSettingsViewContent betaFeatureSettingsViewContent) {
        this.betaFeatureSettingsViewContent = betaFeatureSettingsViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected Component getMainContent() {
        return betaFeatureSettingsViewContent;
    }
}
