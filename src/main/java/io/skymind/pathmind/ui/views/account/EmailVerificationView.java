package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.views.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Tag("email-verification-view")
@JsModule("./src/account/email-verification-view.js")
@Route(value="email-verification-view")
public class EmailVerificationView extends PolymerTemplate<EmailVerificationView.Model>
		implements HasUrlParameter<String>, AfterNavigationObserver
{
	@Id("backToApp")
	private Button backToApp;

	@Autowired
	private UserService userService;

	private String token;

	@Override
	public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
		token = param;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
		backToApp.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

		try {
			PathmindUser user = userService.findByToken(token);
			if (user == null) {
				getModel().setError(true);
				return;
			}

			user.setEmailVerifiedAt(LocalDateTime.now());
			userService.update(user);
			getModel().setError(false);
		} catch(IllegalArgumentException e) {
			getModel().setError(true);
		}
	}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}
}
