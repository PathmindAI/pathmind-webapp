package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.UserService;

import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.UserUpdateBusEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Tag("email-verification-view")
@JsModule("./src/account/email-verification-view.js")
@Route(value = Routes.EMAIL_VERIFICATION_URL)
public class EmailVerificationView extends PolymerTemplate<EmailVerificationView.Model>
		implements PublicView, HasUrlParameter<String>, AfterNavigationObserver
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
		backToApp.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));

		try {
			PathmindUser user = userService.findByToken(token);
			if (user == null) {
				getModel().setError(true);
				return;
			}

			user.setEmailVerifiedAt(LocalDateTime.now());
			userService.update(user);
			EventBus.post(new UserUpdateBusEvent(user));
			getModel().setError(false);
		} catch(IllegalArgumentException e) {
			getModel().setError(true);
		}
	}
	
	public interface Model extends TemplateModel {
		void setError(boolean error);
	}
}
