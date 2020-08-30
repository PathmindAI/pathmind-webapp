package io.skymind.pathmind.webapp.ui.layouts.components;

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
import io.skymind.pathmind.webapp.bus.events.UserUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.UserUpdateSubscriber;
import io.skymind.pathmind.webapp.security.VaadinSecurityUtils;
import io.skymind.pathmind.webapp.ui.components.SearchBox;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.account.AccountView;

import io.skymind.pathmind.webapp.ui.views.settings.SettingsView;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Supplier;

public class AccountHeaderPanel extends HorizontalLayout
{
	private Span usernameLabel = new Span();
	private PathmindUser user;
	private SearchBox searchBox;

	public AccountHeaderPanel(PathmindUser user, FeatureManager featureManager) {
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

		MenuItem account = menuBar.addItem(createItem(new Icon(VaadinIcon.USER), user));
		account.getSubMenu().addItem("Account", e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
		if (VaadinSecurityUtils.isAuthorityGranted(SettingsView.class)) {
            account.getSubMenu().addItem("Settings", e -> getUI().ifPresent(ui -> ui.navigate(SettingsView.class)));
        }
		account.getSubMenu().addItem("Sign out", e -> getUI().ifPresent(ui -> VaadinUtils.signout(ui, false)));
	}

	private HorizontalLayout createItem(Icon icon, PathmindUser user) {
        updateData(user);
		HorizontalLayout hl = WrapperUtils.wrapWidthFullHorizontal(icon, usernameLabel);
		return hl;
	}

	private void updateData(PathmindUser pathmindUser) {
		if (usernameLabel != null) {
			usernameLabel.setText(getUsername(pathmindUser));
		}
	}

	private String getUsername(PathmindUser user){
		return StringUtils.isBlank(user.getName()) ? user.getEmail() : user.getName();
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		EventBus.unsubscribe(this);
	}

	@Override
	protected void onAttach(AttachEvent event) {
		EventBus.subscribe(this, new AccountHeaderUserUpdateSubscriber(() -> getUI()));
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

    class AccountHeaderUserUpdateSubscriber extends UserUpdateSubscriber {

        public AccountHeaderUserUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(UserUpdateBusEvent event) {
            user = event.getPathmindUser();
            PushUtils.push(getUiSupplier(), () -> updateData(event.getPathmindUser()));
        }

        @Override
        public boolean filterBusEvent(UserUpdateBusEvent event) {
            return user.getId() == event.getPathmindUser().getId();
        }
   }
}
