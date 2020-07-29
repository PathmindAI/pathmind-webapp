package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.Command;

public class ConfirmationUtils {

	public static void archive(String entityName, Command confirmAction) {
		String header = String.format("Archive %s?", entityName);
		String text = String.format("This hides it from your main workspaces so you can stay organized. You can unarchive it anytime.");
		String confirmText = String.format("Archive");
		
		ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> confirmAction.execute());
		dialog.setCancelButton("Cancel", evt -> dialog.close(), UIConstants.DEFAULT_BUTTON_THEME);
		dialog.open();
	}
	
	public static void unarchive(String entityName, Command confirmAction) {
		String header = "Confirm Unarchive";
		String text = String.format("Are you sure you want to unarchive this %s?", entityName);
		String confirmText = String.format("Unarchive");
		
		ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> confirmAction.execute());
		dialog.setCancelButton("Cancel", evt -> dialog.close(), UIConstants.DEFAULT_BUTTON_THEME);
		dialog.open();
	}

	public static void emailUpdateConfirmation(String newEmailAddress, Command action) {
        String header = "Update Email";
        String text = String.format("Your email will be updated as %s and you will receive a verification email. You can start using your new email after verifying it.", newEmailAddress);
        String confirmText = "Update";
        ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> action.execute());
        dialog.setCancelButton("Cancel", evt -> dialog.close());
        dialog.open();
    }
	
	public static void emailUpdated(Command action) {
	    String header = "Email Updated";
	    String text = "Your email address has been updated. We sent a verification email to this address. Please follow the instructions in the email to verify your address and then sign in again.";
	    String confirmText = "OK";
	    ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> action.execute());
	    dialog.open();
	}
	
}
