package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

import static io.skymind.pathmind.shared.security.Routes.ACCOUNT_UPGRADE;

@Route(value = ACCOUNT_UPGRADE, layout = MainLayout.class)
public class AccountUpgradeView extends PathMindDefaultView {
    private final AccountUpgradeViewContent accountUpgradeViewContent;

    private final FeatureManager featureManager;

    @Autowired
    public AccountUpgradeView(AccountUpgradeViewContent accountUpgradeViewContent,
                              FeatureManager featureManager) {
        this.accountUpgradeViewContent = accountUpgradeViewContent;
        this.featureManager = featureManager;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // if user has an ongoing subscription this view shouldn't be shown

        // reroute all users - slin
        // if (!featureManager.isEnabled(Feature.ACCOUNT_UPGRADE)) {
        event.rerouteTo(AccountView.class);
        // }

        super.beforeEnter(event);
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Account", "Upgrade Subscription Plans", AccountView.class);
    }

    @Override
    protected Component getMainContent() {
        return accountUpgradeViewContent;
    }
}
