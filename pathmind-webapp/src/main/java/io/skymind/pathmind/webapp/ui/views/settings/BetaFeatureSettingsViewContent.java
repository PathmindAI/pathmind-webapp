package io.skymind.pathmind.webapp.ui.views.settings;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
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

    private final PathmindUser user;
    private final SegmentIntegrator segmentIntegrator;

    // TODO -> get user account's preference from DB
    private boolean userRewardTermsSetting = false;

    @Autowired
    public BetaFeatureSettingsViewContent(CurrentUser currentUser, SegmentIntegrator segmentIntegrator) {
        this.user = currentUser.getUser();
        this.segmentIntegrator = segmentIntegrator;
        setPreferences();
    }

    private void setPreferences() {
        getElement().setProperty("withRewardTerms", userRewardTermsSetting);
    }

    @ClientCallable
    private void toggleRewardTerms() {
        String header;
        String message;
        String confirmText;
        Command confirmHandler = () -> {};
        if (userRewardTermsSetting) {
            header = "Disable Reward Terms";
            message = "By disabling the Reward Terms feature, you will use the reward function interface for your new experiments. You can re-enable the reward terms on this page.";
            confirmText = "Disable";
        } else {
            header = "Enable Reward Terms";
            message = "By enabling the Reward Terms feature, it will replace the reward function interface for your new experiments. You can disable the reward terms on this page.";
            confirmText = "Enable";
        }
        ConfirmationUtils.confirmationPopupDialog(header, message, confirmText, confirmHandler);
    }
}
