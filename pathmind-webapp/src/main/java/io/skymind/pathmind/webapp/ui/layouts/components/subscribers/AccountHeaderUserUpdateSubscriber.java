package io.skymind.pathmind.webapp.ui.layouts.components.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.UserUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.UserUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.layouts.components.AccountHeaderPanel;

public class AccountHeaderUserUpdateSubscriber extends UserUpdateSubscriber {

    private AccountHeaderPanel accountHeaderPanel;

    public AccountHeaderUserUpdateSubscriber(AccountHeaderPanel accountHeaderPanel) {
        this.accountHeaderPanel = accountHeaderPanel;
    }

    @Override
    public void handleBusEvent(UserUpdateBusEvent event) {
        accountHeaderPanel.setUser(event.getPathmindUser());
        accountHeaderPanel.updateComponent();
    }

    @Override
    public boolean filterBusEvent(UserUpdateBusEvent event) {
        return accountHeaderPanel.getUser().getId() == event.getPathmindUser().getId();
    }
}
