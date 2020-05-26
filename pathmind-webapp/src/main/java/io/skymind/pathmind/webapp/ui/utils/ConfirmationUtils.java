package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.shared.utils.PathmindStringUtils;

public class ConfirmationUtils {

	public static void archive(String entityName, Command confirmAction) {
		String header = String.format("Archive this %s?", entityName);
		String text = String.format("This hides it from your main workspaces so you can stay organized. You can always unarchive %ss.", entityName);
		String confirmText = String.format("Archive %s", PathmindStringUtils.toCamelCase(entityName));
		
		ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> confirmAction.execute());
		dialog.setCancelButton("Cancel", evt -> dialog.close());
		dialog.open();
	}
	
	public static void unarchive(String entityName, Command confirmAction) {
		String header = "Confirm Unarchive";
		String text = String.format("Are you sure you want to unarchive this %s?", entityName);
		String confirmText = String.format("Unarchive %s", PathmindStringUtils.toCamelCase(entityName));
		
		ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> confirmAction.execute());
		dialog.setCancelButton("Cancel", evt -> dialog.close());
		dialog.open();
	}

	public static void leavePage(Command cancelAction) {
		String header = "Before you leave....";
		String text = "You have unsaved changes on this page that cannot be automatically saved. Please check and fix the invalid fields.";
		String confirmText = "Stay";
		ConfirmDialog dialog = new ConfirmDialog(header, text, confirmText, evt -> evt.getSource().close());
		dialog.setCancelButton("Leave", evt -> cancelAction.execute());
		dialog.open();
	}
}
