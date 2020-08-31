package io.skymind.pathmind.webapp.ui.views.experiment.components.notification;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.*;

public class StoppedTrainingNotification extends Span {

    private static final String CSS_CLASSNAME = "reason-why-the-training-stopped";
    private static final String[] NOTIFICATION_CSS_CLASSNAMES = new String[]{SUCCESS_LABEL, WARNING_LABEL, ERROR_LABEL};

    private String earlyStoppingUrl;

    public StoppedTrainingNotification(String earlyStoppingUrl) {
        super("");
        this.earlyStoppingUrl = earlyStoppingUrl;
        addClassNames(CSS_CLASSNAME);
    }

    public void showTheReasonWhyTheTrainingStopped(String text, String labelClass, boolean showEarlyStoppingLink) {
        removeClassNames(NOTIFICATION_CSS_CLASSNAMES);
        addClassName(labelClass);
        setText(text);
        if (showEarlyStoppingLink) {
            add(". See more info at ");
            Anchor earlyStopping = new Anchor(earlyStoppingUrl, "Early Stopping");
            earlyStopping.setTarget("_blank");
            add(earlyStopping);
            add(".");
        }
        setVisible(true);
    }

    public void clearErrorState() {
        setText(null);
        setVisible(false);
    }
}