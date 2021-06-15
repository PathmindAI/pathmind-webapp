package io.skymind.pathmind.webapp.ui.views.experiment.components.notification;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import java.text.MessageFormat;
import java.util.List;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SUCCESS_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.WARNING_LABEL;

public class StoppedTrainingNotification extends Span implements ExperimentComponent {

    private static final String CSS_CLASSNAME = "reason-why-the-training-stopped";
    private static final String[] NOTIFICATION_CSS_CLASSNAMES = new String[]{SUCCESS_LABEL, WARNING_LABEL, ERROR_LABEL};

    private final String earlyStoppingUrl;
    private final String alEngineErrorArticleUrl;
    private final List<String> trainingErrorsHelpArticleList;

    public StoppedTrainingNotification(String earlyStoppingUrl, String alEngineErrorArticleUrl, List<String> trainingErrorsHelpArticleList) {
        super("");
        this.earlyStoppingUrl = earlyStoppingUrl;
        this.alEngineErrorArticleUrl = alEngineErrorArticleUrl;
        this.trainingErrorsHelpArticleList = trainingErrorsHelpArticleList;
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
                String trainingError = "";
                if (ExperimentUtils.isAnyLogicEngineError(trainingError)) {
                    trainingError =
                            "AnyLogic engine has returned ERROR. Please follow " +
                                    "<a target=\"_blank\" href=\"{0}\">these instructions</a> " +
                                    "to reproduce the error back in AnyLogic";
                    trainingError = MessageFormat.format(trainingError, alEngineErrorArticleUrl);
                } else {
                    trainingError = experiment.getTrainingError() + " Click <a target=\"_blank\" href=\"{0}\">here</a> for more information.";
                    trainingError = MessageFormat.format(trainingError, trainingErrorsHelpArticleList.get(Math.toIntExact(experiment.getTrainingErrorId()) - 1));
                }
                showTheReasonWhyTheTrainingStopped(trainingError, ERROR_LABEL, false);
            }
        } else if(experiment.isTrainingStoppedEarly()) {
            showTheReasonWhyTheTrainingStopped(experiment.getTrainingStoppedEarlyMessage(), SUCCESS_LABEL, true);
        }
    }
}