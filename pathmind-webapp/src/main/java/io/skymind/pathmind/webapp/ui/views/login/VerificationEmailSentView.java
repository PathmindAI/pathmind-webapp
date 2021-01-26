package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Tag("verification-email-sent-view")
@JsModule("./src/pages/account/verification-email-sent-view.js")
@Route(value = Routes.VERIFICATION_EMAIL_SENT)
public class VerificationEmailSentView extends PolymerTemplate<VerificationEmailSentView.Model> implements PublicView {

    @Id("backToLogin")
    private Button backToLogin;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    public VerificationEmailSentView(@Value("${pathmind.contact-support.address}") String contactLink) {
        getModel().setContactLink(contactLink);
        backToLogin.addClickListener(evt -> getUI().ifPresent(ui -> ui.navigate(ProjectsView.class)));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().appendChild(segmentIntegrator.getElement());
        segmentIntegrator.verificationEmailSent();
    }

    public interface Model extends TemplateModel {
        void setContactLink(String contactLink);
    }
}
