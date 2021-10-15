package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("account-upgrade-view-content")
@JsModule("./src/pages/account/account-upgrade-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountUpgradeViewContent extends LitTemplate {
    @Id("proBtn")
    private Button proBtn;

    @Autowired
    public AccountUpgradeViewContent(CurrentUser currentUser,
                                     @Value("${pathmind.pathmind-api.url}") String apiUrl,
                                     @Value("${pathmind.contact-support.address}") String contactLink,
                                     @Value("${pathmind.stripe.public.key}") String publicKey,
                                     SegmentIntegrator segmentIntegrator) {
        getElement().setProperty("contactLink", contactLink);
        getElement().setProperty("key", publicKey);
        getElement().setProperty("userApiKey", currentUser.getUser().getApiKey());
        getElement().setProperty("apiUrl", apiUrl);

        proBtn.addClickListener(e -> getUI().ifPresent(ui -> {
            segmentIntegrator.upgradeToProPlanClicked();
        }));
    }

}
