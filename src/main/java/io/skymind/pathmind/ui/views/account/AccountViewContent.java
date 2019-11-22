package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@Tag("account-view-content")
@JsModule("./src/account/account-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountViewContent extends PolymerTemplate<AccountViewContent.Model> {

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
	public AccountViewContent(CurrentUser currentUser, @Value("${pathmind.contact-support.address}") String contactLink) {
        getModel().setContactLink(contactLink);
		user = currentUser.getUser();
	}

	@PostConstruct
	private void init() {
		initContent();
		initBtns();
	}

	private void initBtns() {
		editInfoBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountEditView.class)));
		changePasswordBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ChangePasswordView.class)));
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

	public interface Model extends TemplateModel {
		void setEmail(String email);
		void setFirstName(String firstName);
		void setLastName(String lastName);
		void setSubscription(String subscription);
		void setBillingInfo(String billingInfo);
        void setContactLink(String contactLink);
	}
}
