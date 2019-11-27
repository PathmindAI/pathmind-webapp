package io.skymind.pathmind.ui.components.dialog;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.utils.DateAndTimeUtils;

public class SubscriptionCancelDialog extends ConfirmDialog {

	public SubscriptionCancelDialog(Long periodEndEpoch, Command cancelEvent) {
        this.setHeader("Cancel Your Subscription?");
        this.setText(String.format("Cancellation will be effective at the end of your current billing period on %s.", 
        		DateAndTimeUtils.formatDateAndTimeShortFormatter(DateAndTimeUtils.fromEpoch(periodEndEpoch))));
        this.setConfirmButton("Cancel Subscription", confirmEvent -> cancelEvent.execute(), "error");
        this.setCancelButton("Cancel", evt -> close());
    }
}
