package io.skymind.pathmind.webapp.ui.views.settings;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("beta-feature-settings-view-content")
@JsModule("./src/settings/beta-feature-settings-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class BetaFeatureSettingsViewContent extends LitTemplate {

    private final UserService userService;
    private final PathmindUser user;
    private final SegmentIntegrator segmentIntegrator;

    @Autowired
    public BetaFeatureSettingsViewContent(
            UserService userService,
            SegmentIntegrator segmentIntegrator) {
        this.userService = userService;
        this.user = userService.getCurrentUser();
        this.segmentIntegrator = segmentIntegrator;
        setPreferences();
    }

    private void setPreferences() {
        getElement().setProperty("withRewardTerms", user.isRewardTermsOn());
    }

    @ClientCallable
    private void toggleRewardTerms() {
        String header;
        String message;
        String confirmText;
        Command confirmHandler = () -> {
            Boolean newRewardTermsOn = !user.isRewardTermsOn();
            user.setRewardTermsOn(newRewardTermsOn);
            userService.update(user);
            getElement().setProperty("withRewardTerms", newRewardTermsOn);
            if (newRewardTermsOn) {
                segmentIntegrator.enabledRewardTermsToggle();
            } else {
                segmentIntegrator.disabledRewardTermsToggle();
            }
        };
        if (user.isRewardTermsOn()) {
            header = "Disable Reward Terms";
            message = "By disabling the Reward Terms feature, you will use the reward function interface for your new experiments. You can re-enable the reward terms toggle on this page.";
            confirmText = "Disable";
        } else {
            header = "Enable Reward Terms";
            message = "By enabling the Reward Terms feature, a toggle button will show on your new experiment page. You can choose between the old Reward Function UI and the new Reward Terms UI. You can disable the toggle on this page.";
            confirmText = "Enable";
        }
        ConfirmationUtils.confirmationPopupDialog(header, message, confirmText, confirmHandler);
    }
}
