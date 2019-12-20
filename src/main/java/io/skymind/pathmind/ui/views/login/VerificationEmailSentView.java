package io.skymind.pathmind.ui.views.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;

@Tag("verification-email-sent-view")
@JsModule("./src/account/verification-email-sent-view.js")
@Route(value = Routes.VERIFICATION_EMAIL_SENT_URL)
public class VerificationEmailSentView extends PolymerTemplate<SignUpView.Model> implements PublicView {

	@Id("backToLogin")
	private Button backToLogin;
	
	@Autowired
	private SegmentIntegrator segmentIntegrator;
	
	public VerificationEmailSentView() {
		backToLogin.addClickListener(evt -> UI.getCurrent().navigate(LoginView.class));
	}
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		getElement().appendChild(segmentIntegrator.getElement());
	}
	
}
