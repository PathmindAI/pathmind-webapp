package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("account-upgrade-view-content")
@JsModule("./src/pages/account/account-upgrade-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountUpgradeViewContent extends PolymerTemplate<AccountUpgradeViewContent.Model> {
    @Id("proBtn")
    private Button proBtn;

    private PathmindUser user;

    @Autowired
    public AccountUpgradeViewContent(CurrentUser currentUser,
                                     @Value("${pathmind.pathmind-api.url}") String apiUrl,
                                     @Value("${pathmind.contact-support.address}") String contactLink,
                                     @Value("${pathmind.stripe.public.key}") String publicKey,
                                     SegmentIntegrator segmentIntegrator) {
        getModel().setContactLink(contactLink);
        user = currentUser.getUser();
        getModel().setKey(publicKey);
        getModel().setUserApiKey(user.getApiKey());
        getModel().setApiUrl(apiUrl);

        proBtn.addClickListener(e -> getUI().ifPresent(ui -> {
            segmentIntegrator.upgradeToProPlanClicked();
        }));
    }

    public interface Model extends TemplateModel {
        void setContactLink(String contactLink);
        void setKey(String key);
        void setUserApiKey(String key);
        void setApiUrl(String pathmindApiUrl);
    }

}
