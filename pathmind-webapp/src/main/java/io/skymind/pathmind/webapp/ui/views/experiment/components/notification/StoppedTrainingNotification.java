package io.skymind.pathmind.webapp.ui.views.experiment.components.notification;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import java.text.MessageFormat;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SUCCESS_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.WARNING_LABEL;

public class StoppedTrainingNotification extends Span implements ExperimentComponent {

    private static final String CSS_CLASSNAME = "reason-why-the-training-stopped";
    private static final String[] NOTIFICATION_CSS_CLASSNAMES = new String[]{SUCCESS_LABEL, WARNING_LABEL, ERROR_LABEL};

    private final String earlyStoppingUrl;
    private final String alEngineErrorArticleUrl;

    public StoppedTrainingNotification(String earlyStoppingUrl, String alEngineErrorArticleUrl) {
        super("");
        this.earlyStoppingUrl = earlyStoppingUrl;
        this.alEngineErrorArticleUrl = alEngineErrorArticleUrl;
        addClassNames(CSS_CLASSNAME);
    }

    public void showTheReasonWhyTheTrainingStopped(String text, String labelClass, boolean showEarlyStoppingLink) {
        removeClassNames(NOTIFICATION_CSS_CLASSNAMES);
        addClassName(labelClass);
        getElement().setProperty("innerHTML", text);
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

        if (RunStatus.isError(experiment.getTrainingStatusEnum())) {
            if(experiment.isTrainingError()) {
                String trainingError = experiment.getTrainingError();
                if (ExperimentUtils.isAnyLogicEngineError(trainingError)) {
                    trainingError =
                            "AnyLogic engine has returned ERROR. Please follow " +
                                    "<a target=\"_blank\" href=\"{0}\">these instructions</a> " +
                                    "to reproduce the error back in AnyLogic";
                    trainingError = MessageFormat.format(trainingError, alEngineErrorArticleUrl);
                }
                showTheReasonWhyTheTrainingStopped(trainingError, ERROR_LABEL, false);
            }
        } else if(experiment.isTrainingStoppedEarly()) {
            showTheReasonWhyTheTrainingStopped(experiment.getTrainingStoppedEarlyMessage(), SUCCESS_LABEL, true);
        }
    }
}