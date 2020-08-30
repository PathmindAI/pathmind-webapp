package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.webapp.ui.components.CloseableNotification;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NotificationUtils {

    public static void showNotification(String html, NotificationVariant variant) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(variant);
        notification.open();
    }

    public static void showSuccess(String html) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    public static void showError(String html) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(-1);
        notification.open();
    }

    public static void showPersistentNotification(String html, String buttonText, String componentId, Command buttonListenerHandler) {
        Button button = new Button(buttonText);
        button.getStyle().set("min-width", "fit-content");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> buttonListenerHandler.execute());

        CloseableNotification notification = new CloseableNotification(html, false, button);
        notification.setId(componentId);
        notification.open();
    }

	public static void showNewVersionAvailableNotification(UI ui) {
        String notificationDialogId = "new-version-notification";
        String text = "Pathmind has been updated. Please log in again to get the latest improvements.";
        if (VaadinUtils.getElementById(ui, notificationDialogId).isEmpty()) {
            showPersistentNotification(text, "Sign out", notificationDialogId, () -> VaadinUtils.signout(ui, true));
        };
	}

    public static void alertAndThen(Supplier<Optional<UI>> getUISupplier, String header, String text, Consumer<UI> consumer) {
        alertAndThen(getUISupplier.get(), header, text, consumer);
    }

    public static void alertAndThen(Optional<UI> optionalUI, String header, String text, Consumer<UI> consumer) {
        ConfirmDialog confirmDialog = new ConfirmDialog(
                header,
                text,
                "Ok", evt -> PushUtils.push(optionalUI, consumer::accept)
        );
        confirmDialog.open();
    }
}
