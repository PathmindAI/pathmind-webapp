package io.skymind.pathmind.webapp.ui.views.account;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.security.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Slf4j
@Tag("upgrade-done-view-content")
@JsModule("./src/pages/account/upgrade-done-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpgradeDoneViewContent extends LitTemplate {

    @Id("done")
    private Button done;

    private UserService userService;

    private PathmindUser user;

    @Autowired
    public UpgradeDoneViewContent(CurrentUser currentUser,
                                  UserService userService,
                                  @Value("${pathmind.contact-support.address}") String contactLink) {
        this.userService = userService;
        user = currentUser.getUser();
        getElement().setProperty("plan", "Professional");
        done.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
        setUpSessionIdHandler();
    }

    private void setUpSessionIdHandler() {
        getElement().addPropertyChangeListener("sessionId", "sessionidchange", event -> {
            try {
                Session session = Session.retrieve(event.getValue().toString());
                String customerId = session.getCustomer();
                user.setStripeCustomerId(customerId);
                userService.update(user);
            } catch (StripeException e) {
                log.error(e.getMessage());
            }
        });
    }

}
