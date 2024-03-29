package io.skymind.pathmind.webapp.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.exceptions.AccessDeniedException;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.errors.PathmindErrorHandler;
import io.skymind.pathmind.webapp.ui.views.login.LoginView;

import static io.skymind.pathmind.webapp.security.constants.VaadinSessionInfo.IS_OLD_VERSION;

/**
 * Adds before enter listener to check access to views.
 * Adds PathmindErrorHandler for Application level error handling
 */
@SpringComponent
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        // https://vaadin.com/docs/v14/flow/advanced/tutorial-service-init-listener
        event.addBootstrapListener(response -> {
            // BoostrapListener to change the bootstrap page
            CustomBootstrapListener customBootstrapListener = new CustomBootstrapListener();
            customBootstrapListener.modifyBootstrapPage(response);
        });
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            Object isOldVersion = ui.getSession().getAttribute(IS_OLD_VERSION);
            ui.addBeforeEnterListener(this::beforeEnter);
            ui.getSession().setErrorHandler(new PathmindErrorHandler());
            if (isOldVersion != null) {
                NotificationUtils.showNewVersionAvailableNotification(ui);
            }
        });
        event.getSource().setSystemMessagesProvider(systemMessagesInfo -> {
            CustomizedSystemMessages msgs = new CustomizedSystemMessages();
            msgs.setSessionExpiredNotificationEnabled(false);
            msgs.setSessionExpiredURL(String.format("/%s/%s", Routes.LOGIN, Routes.SESSION_EXPIRED));
            return msgs;
        });
    }

    /**
     * Reroutes the user if she is not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        final boolean accessGranted = VaadinSecurityUtils.isAccessGranted(event.getNavigationTarget());
        if (!accessGranted) {
            if (SecurityUtils.isUserLoggedIn()) {
                event.rerouteToError(AccessDeniedException.class);
            } else {
                event.rerouteTo(LoginView.class);
            }
        }
    }
}
