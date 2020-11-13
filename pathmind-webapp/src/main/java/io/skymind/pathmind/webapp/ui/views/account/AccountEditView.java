package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.ACCOUNT_EDIT_URL, layout = MainLayout.class)
public class AccountEditView extends PathMindDefaultView {

    private final AccountEditViewContent accountEditViewContent;

    @Autowired
    public AccountEditView(AccountEditViewContent accountEditViewContent) {
        this.accountEditViewContent = accountEditViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Account", "Edit Account", AccountView.class);
    }

    @Override
    protected Component getMainContent() {
        return accountEditViewContent;
    }
}
