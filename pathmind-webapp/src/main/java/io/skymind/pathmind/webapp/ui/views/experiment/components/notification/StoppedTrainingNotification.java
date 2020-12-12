package io.skymind.pathmind.webapp.ui.views.experiment.components.notification;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SUCCESS_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.WARNING_LABEL;

public class StoppedTrainingNotification extends Span implements ExperimentComponent {

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
            add(". Click ");
            Anchor earlyStopping = new Anchor(earlyStoppingUrl, "here");
            earlyStopping.setTarget("_blank");
            add(earlyStopping);
            add(" for more information.");
        }
        setVisible(true);
    }

    public void clearErrorState() {
        setText(null);
        setVisible(false);
    }


    @Override
    public void setExperiment(Experiment experiment) {

        // Clear everything so we have a clean slate.
        clearErrorState();

        if (experiment.getTrainingStatusEnum() == RunStatus.Error || experiment.getTrainingStatusEnum() == RunStatus.Killed) {
            // TODO -> STEPH -> getTrainingErrorAndMessage should be done only once and it should  be done before here.
            // TODO -> STEPH -> This appears to be shown even when an experiment is loaded but need to confirm in dev. Just not sure how. I'll try locally by forcing an
            // error in the debugger.
            // TODO -> STEPH -> Can we not combine the above logic into a single if statement? Why is ExperimentUtils.updateTrainingErrorAndMessage() also have filters
            // on RunStatus.Error but NOT on RunStatus.killed???
            if(experiment.isTrainingError()) {
                showTheReasonWhyTheTrainingStopped(experiment.getTrainingError(), ERROR_LABEL, false);
            }
        } else if(experiment.isTrainingStoppedEarly()) {
            // TODO -> STEPH -> Confirm there's no logic error here because the code is odd. We have a toggle for isSuccess but the logic looks like it can only be true...
            showTheReasonWhyTheTrainingStopped(experiment.getTrainingStoppedEarlyMessage(), SUCCESS_LABEL, true);
        }

    }
}