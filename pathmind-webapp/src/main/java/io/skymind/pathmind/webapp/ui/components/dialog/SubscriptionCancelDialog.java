package io.skymind.pathmind.webapp.ui.components.dialog;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.components.molecules.ConfirmPopup;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

public class SubscriptionCancelDialog extends ConfirmPopup {

    public SubscriptionCancelDialog(UI ui, Long periodEndEpoch, Command cancelEventHandler) {
        this.setHeader("Cancel Your Subscription?");
        this.setConfirmButton("Yes, Cancel", cancelEventHandler, "error primary");
        this.setCancelButtonText("Keep My Subscription");
        VaadinDateAndTimeUtils.withUserTimeZoneId(ui, userTimeZoneId -> {
            this.setMessage(String.format("Cancellation will be effective at the end of your current billing period on %s.",
                    DateAndTimeUtils.formatDateAndTimeShortFormatter(DateAndTimeUtils.fromEpoch(periodEndEpoch), userTimeZoneId)));
        });
    }
}
