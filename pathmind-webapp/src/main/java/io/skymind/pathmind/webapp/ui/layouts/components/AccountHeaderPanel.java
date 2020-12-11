package io.skymind.pathmind.webapp.ui.layouts.components;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.security.VaadinSecurityUtils;
import io.skymind.pathmind.webapp.ui.components.SearchBox;
import io.skymind.pathmind.webapp.ui.layouts.components.subscribers.AccountHeaderUserUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.account.AccountView;
import io.skymind.pathmind.webapp.ui.views.settings.SettingsView;
import org.apache.commons.lang3.StringUtils;

public class AccountHeaderPanel extends HorizontalLayout {
    private Span usernameLabel = new Span();
    private PathmindUser user;
    private SearchBox searchBox;

    private Supplier<Optional<UI>> getUISupplier;

    public AccountHeaderPanel(Supplier<Optional<UI>> getUISupplier, PathmindUser user, FeatureManager featureManager) {
        this.getUISupplier = getUISupplier;
        this.user = user;
        addClassName("nav-account-links");

        if (featureManager.isEnabled(Feature.SEARCH)) {
            searchBox = new SearchBox();
            add(searchBox);
        }

        MenuBar menuBar = new MenuBar();
        add(menuBar);
        menuBar.setThemeName("tertiary");
        menuBar.addClassName("account-menu");

        MenuItem account = menuBar.addItem(createItem(new Icon(VaadinIcon.USER)));
        account.getSubMenu().addItem("Account", e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
        account.getSubMenu().addItem("Access Token", e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
        if (VaadinSecurityUtils.isAuthorityGranted(SettingsView.class)) {
            account.getSubMenu().addItem("Settings", e -> getUI().ifPresent(ui -> ui.navigate(SettingsView.class)));
        }
        account.getSubMenu().addItem("Sign out", e -> getUI().ifPresent(ui -> VaadinUtils.signout(ui, false)));
    }

    private HorizontalLayout createItem(Icon icon) {
        updateComponent();
        HorizontalLayout hl = WrapperUtils.wrapWidthFullHorizontal(icon, usernameLabel);
        return hl;
    }

    public void updateComponent() {
        if (usernameLabel != null) {
            usernameLabel.setText(getUsername(user));
        }
    }

    private String getUsername(PathmindUser user) {
        return StringUtils.isBlank(user.getName()) ? user.getEmail() : user.getName();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier,
                new AccountHeaderUserUpdateSubscriber(this));
    }

    public void clearSearchBoxValue() {
        if (searchBox != null) {
            searchBox.clearSearchValue();
        }
    }

    public void setSearchBoxValue(String text) {
        searchBox.setValue(text);
    }

    public String getSearchBoxValue() {
        return searchBox.getValue();
    }

    public void setUser(PathmindUser user) {
        this.user = user;
    }

    public PathmindUser getUser() {
        return user;
    }
}
