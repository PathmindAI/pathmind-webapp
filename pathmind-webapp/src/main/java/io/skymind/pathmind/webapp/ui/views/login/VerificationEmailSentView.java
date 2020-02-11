package io.skymind.pathmind.webapp.ui.views.login;

import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.security.Routes;

@Tag("verification-email-sent-view")
@JsModule("./src/account/verification-email-sent-view.js")
@Route(value = Routes.VERIFICATION_EMAIL_SENT_URL)
public class VerificationEmailSentView extends PolymerTemplate<TemplateModel> implements PublicView {

	@Id("backToLogin")
	private Button backToLogin;
	
	@Autowired
	private SegmentIntegrator segmentIntegrator;
	
	public VerificationEmailSentView() {
		backToLogin.addClickListener(evt -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
	}
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		getElement().appendChild(segmentIntegrator.getElement());
	}
}
