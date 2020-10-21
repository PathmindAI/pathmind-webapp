package io.skymind.pathmind.webapp.ui.views.account;

import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import javax.annotation.PostConstruct;

import com.stripe.model.Subscription;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.components.dialog.SubscriptionCancelDialog;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Tag("account-view-content")
@JsModule("./src/pages/account/account-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountViewContent extends PolymerTemplate<AccountViewContent.Model> {

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

	@Id("editPaymentBtn")
	private Button editPaymentBtn;

	@Id("rotateApiKeyBtn")
	private Button rotateApiKeyBtn;

    private StripeService stripeService;

    private SegmentIntegrator segmentIntegrator;

    private PathmindUser user;

	private final Duration keyValidityDuration;

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
        getModel().setContactLink(contactLink);
        getModel().setPrivacyLink(privacyPolicyLink);
        getModel().setTermsOfUseLink(termsOfUseLink);
        user = currentUser.getUser();
        this.featureManager = featureManager;
		this.userService = userService;
		this.keyValidityDuration = keyValidityDuration;
    }

    @PostConstruct
    private void init() {
        Subscription subscription = stripeService.getActiveSubscriptionOfUser(user.getEmail());
        initContent(subscription);
        initBtns(subscription);
    }

    private void initBtns(Subscription subscription) {
        editInfoBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountEditView.class)));
        changePasswordBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ChangePasswordView.class)));
        editPaymentBtn.setEnabled(false);
        upgradeBtn.setVisible(featureManager.isEnabled(Feature.ACCOUNT_UPGRADE) && subscription == null);
        cancelSubscriptionBtn.setVisible(subscription != null);
        cancelSubscriptionBtn.setEnabled(subscription != null && !subscription.getCancelAtPeriodEnd());

        upgradeBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class)));
        cancelSubscriptionBtn.addClickListener(evt -> cancelSubscription(subscription));
        rotateApiKeyBtn.addClickListener(evt -> rotateApiKey(subscription));
	}

    private void rotateApiKey(Subscription subscription) {

        userService.rotateApiKey(user);
        user = userService.getCurrentUser();
        getUI().ifPresent(ui -> initContent(subscription));

    }

    // This part will probably move to a separate view, but for now implementing it as a confirmation dialog
	private void cancelSubscription(Subscription subscription) {
		getUI().ifPresent(ui -> {
			SubscriptionCancelDialog subscriptionCancelDialog = new SubscriptionCancelDialog(ui, subscription.getCurrentPeriodEnd(), () -> {
				Subscription updatedSubscription = stripeService.cancelSubscription(user.getEmail(), true);
				segmentIntegrator.subscriptionCancelled();
				initContent(updatedSubscription);
				initBtns(updatedSubscription);
			});
			subscriptionCancelDialog.open();
		});
	}

    private void initContent(Subscription subscription) {
        getModel().setEmail(user.getEmail());
        getModel().setFirstName(user.getFirstname());
        getModel().setLastName(user.getLastname());
        getModel().setApiKey(user.getApiKey());
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
        getModel().setApiKeyExpiresPhrase(expiresPhrase);
        getModel().setSubscription(subscription != null ? "Professional" : "Early Access");
        if (subscription != null && subscription.getCancelAtPeriodEnd()) {
            getUI().ifPresent(ui -> VaadinDateAndTimeUtils.withUserTimeZoneId(ui, userTimeZoneId -> {
                getModel().setSubscriptionCancellationNote("Subscription will be cancelled on " +
                        DateAndTimeUtils.formatDateAndTimeShortFormatter(DateAndTimeUtils.fromEpoch(subscription.getCurrentPeriodEnd()), userTimeZoneId));
            }));
        }
        getModel().setBillingInfo("Billing Information");
    }

    public interface Model extends TemplateModel {
        void setEmail(String email);

        void setFirstName(String firstName);

        void setLastName(String lastName);
		void setApiKey(String apiKey);
		void setApiKeyExpiresPhrase(String apiKeyExpiresPhrase);
        void setSubscription(String subscription);

        void setSubscriptionCancellationNote(String cancellationNote);

        void setBillingInfo(String billingInfo);

        void setContactLink(String contactLink);

        void setPrivacyLink(String privacyPolicyLink);

        void setTermsOfUseLink(String termsOfUseLink);
    }
}
