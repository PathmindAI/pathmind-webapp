package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.security.Routes.PAYMENT_URL;

@Tag("payment-view")
@JsModule("./src/account/payment-view.js")
@Route(value=PAYMENT_URL, layout = MainLayout.class)
public class PaymentView extends PolymerTemplate<PaymentView.Model>
{
	@Id("header")
	private Div header;

	@Id("cancelSignUpBtn")
	private Button cancelSignUpBtn;

	@Id("signUp")
	private Button signUp;

	private PathmindUser user;

	@Autowired
	private UserService userService;


	@Autowired
	public PaymentView(CurrentUser currentUser,
					   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		user = currentUser.getUser();
		header.add(new ScreenTitlePanel("UPGRADE", "Subscription Plans"));

		getModel().setContactLink(contactLink);
		getModel().setPlan("Professional");

		cancelSignUpBtn.addClickListener(e -> UI.getCurrent().navigate(AccountUpgradeView.class));
	}



	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
		void setPlan(String plan);
	}
}
