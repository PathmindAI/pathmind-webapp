package io.skymind.pathmind.webapp.ui.views.account;

import io.skymind.pathmind.webapp.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.data.PathmindUser;

@Tag("account-upgrade-view-content")
@JsModule("./src/pages/account/account-upgrade-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountUpgradeViewContent extends PolymerTemplate<AccountUpgradeViewContent.Model>
{
	@Id("proBtn")
	private Button proBtn;

	private PathmindUser user;

	@Autowired
	public AccountUpgradeViewContent(CurrentUser currentUser,
                              @Value("${pathmind.contact-support.address}") String contactLink)
	{
		getModel().setContactLink(contactLink);
		user = currentUser.getUser();

		proBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(PaymentView.class)));
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
	}

}
