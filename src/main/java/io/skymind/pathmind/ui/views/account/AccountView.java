package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Tag("account-view")
@JsModule("./src/account/account-view.js")
@Route(value="account", layout = MainLayout.class)
public class AccountView extends PolymerTemplate<AccountView.Model>
{
	@Id("editInfoBtn")
	private Button editInfoBtn;

	@Id("changePasswordBtn")
	private Button changePasswordBtn;

	@Id("upgradeBtn")
	private Button upgradeBtn;

	@Id("editPaymentBtn")
	private Button editPaymentBtn;

	private PathmindUser user;

	@Autowired
	public AccountView(CurrentUser currentUser)
	{
		user = currentUser.getUser();
	}

	@PostConstruct
	private void init() {
		initTabs();
		initContent();
		initBtns();
	}

	private void initBtns() {
		editInfoBtn.addClickListener(e -> UI.getCurrent().navigate(AccountEditView.class));
		changePasswordBtn.addClickListener(e -> UI.getCurrent().navigate(ChangePasswordView.class));
//		changePasswordBtn.setEnabled(false);
		upgradeBtn.setEnabled(false);
		editPaymentBtn.setEnabled(false);
	}

	private void initContent() {
		getModel().setEmail(user.getEmail());
		getModel().setFirstName(user.getFirstname());
		getModel().setLastName(user.getLastname());
		getModel().setSubscription("Early Access");
		getModel().setBillingInfo("Billing Information");
	}

	private void initTabs() {
		Tabs tabs = new Tabs();
		tabs.add(new Tab("Account information"));
		tabs.setWidth("100%");
	}

	public interface Model extends TemplateModel {
		void setEmail(String email);
		void setFirstName(String firstName);
		void setLastName(String lastName);
		void setSubscription(String subscription);
		void setBillingInfo(String billingInfo);
	}
}
