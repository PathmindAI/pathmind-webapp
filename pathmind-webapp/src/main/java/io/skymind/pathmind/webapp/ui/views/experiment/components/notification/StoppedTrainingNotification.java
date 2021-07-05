package io.skymind.pathmind.webapp.ui.views.experiment.components.notification;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import java.text.MessageFormat;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.*;

public class StoppedTrainingNotification extends Span implements ExperimentComponent {

    private static final String CSS_CLASSNAME = "reason-why-the-training-stopped";
    private static final String[] NOTIFICATION_CSS_CLASSNAMES = new String[]{SUCCESS_LABEL, WARNING_LABEL, ERROR_LABEL};

    private final String earlyStoppingUrl;

    public StoppedTrainingNotification(String earlyStoppingUrl) {
        super("");
        this.earlyStoppingUrl = earlyStoppingUrl;
        addClassNames(CSS_CLASSNAME);
    }

    private void showTheReasonWhyTheTrainingStopped(String text, String labelClass, boolean showEarlyStoppingLink) {
        removeClassNames(NOTIFICATION_CSS_CLASSNAMES);
        addClassName(labelClass);
        if (showEarlyStoppingLink) {
            getElement().setText(text);
            add(". Click ");
            Anchor earlyStopping = new Anchor(earlyStoppingUrl, "here");
            earlyStopping.setTarget("_blank");
            add(earlyStopping);
            add(" for more information.");
        } else {
            getElement().executeJs("this.innerHTML = $0", text);
        }
        setVisible(true);
    }

    public void clearErrorState() {
        setText(null);
        setVisible(false);
    }

    @Override
    public void setExperiment(Experiment experiment) {
        clearErrorState();

        if (RunStatus.isError(experiment.getTrainingStatusEnum())) {
            if(experiment.isTrainingError()) {
                String trainingError = experiment.getTrainingError();
                if (experiment.getSupportArticle() != null && trainingError.contains("Click here")) {
                    trainingError = trainingError.replace("Click here", "Click <a target=\"_blank\" href=\"{0}\">here</a>");
                    trainingError = MessageFormat.format(trainingError, experiment.getSupportArticle());
                }
                showTheReasonWhyTheTrainingStopped(trainingError, ERROR_LABEL, false);
            }
        } else if(experiment.isTrainingStoppedEarly()) {
            showTheReasonWhyTheTrainingStopped(experiment.getTrainingStoppedEarlyMessage(), SUCCESS_LABEL, true);
        }
    }
}