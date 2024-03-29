package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.ACCOUNT, layout = MainLayout.class)
public class AccountView extends PathMindDefaultView {

    private final AccountViewContent accountViewContent;

    @Autowired
    public AccountView(AccountViewContent accountViewContent) {
        this.accountViewContent = accountViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected Component getMainContent() {
        return accountViewContent;
    }
}
