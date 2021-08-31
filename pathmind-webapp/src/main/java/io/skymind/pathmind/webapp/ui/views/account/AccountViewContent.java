package io.skymind.pathmind.webapp.ui.views.account;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;

import com.stripe.model.Subscription;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.components.dialog.SubscriptionCancelDialog;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("account-view-content")
@JsModule("./src/pages/account/account-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountViewContent extends LitTemplate {

    private final FeatureManager featureManager;

    private final UserService userService;

    @Id("editInfoBtn")
    private Button editInfoBtn;

    @Id("changePasswordBtn")
    private Button changePasswordBtn;

    @Id("upgradeBtn")
    private Button upgradeBtn;

    @Id("cancelSubscriptionBtn")
    private Button cancelSubscriptionBtn;

    @Id("rotateApiKeyBtn")
    private Button rotateApiKeyBtn;

    private StripeService stripeService;

    private SegmentIntegrator segmentIntegrator;

    private PathmindUser user;

    private final Duration keyValidityDuration;

    private Subscription subscription;

    @Autowired
    public AccountViewContent(
            @Value("${pm.api.key-validity-duration}") Duration keyValidityDuration,
            CurrentUser currentUser, UserService userService,
            @Value("${pathmind.contact-support.address}") String contactLink,
            @Value("${pathmind.privacy-policy.url}") String privacyPolicyLink,
            @Value("${pathmind.terms-of-use.url}") String termsOfUseLink,
            StripeService stripeService,
            SegmentIntegrator segmentIntegrator, FeatureManager featureManager) {
        this.stripeService = stripeService;
        this.segmentIntegrator = segmentIntegrator;
        getElement().setProperty("contactLink", contactLink);
        getElement().setProperty("privacyLink", privacyPolicyLink);
        getElement().setProperty("termsOfUseLink", termsOfUseLink);
        user = currentUser.getUser();
        this.featureManager = featureManager;
        this.userService = userService;
        this.keyValidityDuration = keyValidityDuration;
    }

    @PostConstruct
    private void init() {
        subscription = stripeService.getActiveSubscriptionOfUser(user.getEmail()).getResult();
        initContent();
        initBtns();
    }

    private void initBtns() {
        editInfoBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountEditView.class)));
        changePasswordBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ChangePasswordView.class)));
        upgradeBtn.setVisible(featureManager.isEnabled(Feature.ACCOUNT_UPGRADE) && subscription == null && !UserRole.isInternalOrEnterpriseOrPartnerUser(user.getAccountType()));
        cancelSubscriptionBtn.setVisible(subscription != null);
        cancelSubscriptionBtn.setEnabled(subscription != null && !subscription.getCancelAtPeriodEnd());

        upgradeBtn.addClickListener(e -> getUI().ifPresent(ui -> {
            segmentIntegrator.navigatedToPricingFromAccountView();
            ui.navigate(AccountUpgradeView.class);
        }));
        cancelSubscriptionBtn.addClickListener(evt -> cancelSubscription());
        rotateApiKeyBtn.addClickListener(evt -> rotateApiKey());
    }

    private void rotateApiKey() {
        userService.rotateApiKey(user);
        user = userService.getCurrentUser();
        setApiKey(user.getApiKey());
    }

    private void setApiKey(String apiKey) {
        getElement().setProperty("apiKey", apiKey);
        String expiresPhrase;
        long daysToExpire = LocalDateTime.now().until(user.getApiKeyCreatedAt().plus(keyValidityDuration), ChronoUnit.DAYS);
        if (daysToExpire < 0) {
            expiresPhrase = "Expired. Please rotate.";
        } else if (daysToExpire == 0) {
            expiresPhrase = "Expires today";
        } else if (daysToExpire == 1) {
            expiresPhrase = "Expires tomorrow";
        } else {
            expiresPhrase = "Expires in " + daysToExpire + " days";
        }
        getElement().setProperty("apiKeyExpiresPhrase", expiresPhrase);
    }

    // This part will probably move to a separate view, but for now implementing it as a confirmation dialog
    private void cancelSubscription() {
        getUI().ifPresent(ui -> {
            SubscriptionCancelDialog subscriptionCancelDialog = new SubscriptionCancelDialog(ui, subscription.getCurrentPeriodEnd(), () -> {
                subscription = stripeService.cancelSubscription(user.getEmail(), true);
                segmentIntegrator.subscriptionCancelled();
                initContent();
                initBtns();
                setSubscriptionEndDate();
            });
            subscriptionCancelDialog.open();
        });
    }

    @ClientCallable
    private void setSubscriptionEndDate() {
        if (subscription != null && subscription.getCancelAtPeriodEnd()) {
            getUI().ifPresent(ui -> VaadinDateAndTimeUtils.withUserTimeZoneId(ui, userTimeZoneId -> {
                getElement().setProperty("subscriptionCancellationNote", "Subscription will be cancelled on " +
                        DateAndTimeUtils.formatDateAndTimeShortFormatter(DateAndTimeUtils.fromEpoch(subscription.getCurrentPeriodEnd()), userTimeZoneId));
            }));
        }
    }

    private void initContent() {
        getElement().setProperty("email", user.getEmail());
        getElement().setProperty("firstName", user.getFirstname());
        getElement().setProperty("lastName", user.getLastname());
        setApiKey(user.getApiKey());
        getElement().setProperty("subscription", user.getAccountType().equals(UserRole.Partner) ? "Professional" : user.getAccountType().toString());
    }
}
