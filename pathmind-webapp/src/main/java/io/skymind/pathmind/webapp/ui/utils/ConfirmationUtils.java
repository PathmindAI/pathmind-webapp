package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;

public class ConfirmationUtils {

    public static void archive(String entityName, Command confirmAction) {
        String header = String.format("Archive %s?", entityName);
        String text = String.format("This hides it from your main workspaces so you can stay organized. You can unarchive it anytime.");

        ConfirmPopup popup = new ConfirmPopup(header, text);
        popup.setConfirmButton("Archive", confirmAction);
        popup.setCancelButtonText("Cancel");
        popup.open();
    }

    public static void unarchive(String entityName, Command confirmAction) {
        String header = "Confirm Unarchive";
        String text = String.format("Are you sure you want to unarchive this %s?", entityName);

        ConfirmPopup popup = new ConfirmPopup(header, text);
        popup.setConfirmButton("Unarchive", confirmAction);
        popup.setCancelButtonText("Cancel");
        popup.open();
    }

    public static void emailUpdateConfirmation(String newEmailAddress, Command action) {
        String header = "Update Email";
        String text = String.format("Your email will be updated as %s and you will receive a verification email. You can start using your new email after verifying it.", newEmailAddress);

        ConfirmPopup popup = new ConfirmPopup(header, text);
        popup.setConfirmButton("Update", action);
        popup.setCancelButtonText("Cancel");
        popup.open();
    }

    public static void emailUpdated(Command action) {
        String header = "Email Updated";
        String text = "Your email address has been updated. We sent a verification email to this address. Please follow the instructions in the email to verify your address and then sign in again.";

        ConfirmPopup popup = new ConfirmPopup(header, text);
        popup.setConfirmButton("OK", action);
        popup.open();
    }

    public static void confirmationPopupDialog(String header, String message, String confirmText, Command confirmHandler) {
        ConfirmPopup confirmPopup = new ConfirmPopup(header, message);
        confirmPopup.setConfirmButton(
                confirmText,
                confirmHandler
        );
        confirmPopup.setCancelButtonText("Cancel");
        confirmPopup.open();
    }

    public static void showStopTrainingConfirmationPopup(Command confirmHandler) {
        ConfirmPopup confirmPopup = new ConfirmPopup();
        confirmPopup.setHeader("Stop Training");
        confirmPopup.setMessage(new Html(
                "<div>"
                        + "<p>Are you sure you want to stop training?</p>"
                        + "<p>If you stop the training before it completes, you won't be able to download the policy. "
                        + "<b>If you decide you want to start the training again, you can start a new experiment and "
                        + "use the same reward function.</b>"
                        + "</p>"
                        + "</div>"));
        confirmPopup.setConfirmButton("Stop Training", confirmHandler, ButtonVariant.LUMO_ERROR.getVariantName() + " " + ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmPopup.setCancelButtonText("Cancel");
        confirmPopup.open();
    }
}
