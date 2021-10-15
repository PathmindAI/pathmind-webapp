package io.skymind.pathmind.webapp.ui.views.experiment.components.codeViewer;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardTerm;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.stream.Collectors;

@Tag("code-viewer")
@JsModule("./src/experiment/code-viewer.ts")
public class CodeViewer extends LitTemplate implements HasStyle, ExperimentComponent {

    public CodeViewer() {
        super();
    }

    public CodeViewer(Experiment experiment, Boolean showCopyButton, Boolean showBorder) {
        super();
        setExperiment(experiment);
        getElement().setProperty("showCopyButton", showCopyButton);
        getElement().setProperty("showBorder", showBorder);
        getElement().setProperty("isWithRewardTerms", experiment.isWithRewardTerms());
    }

    public void setExperiment(Experiment experiment) {

        String snippet = experiment.getRewardFunction();

        if (experiment.isWithRewardTerms()) {
            snippet = experiment.getRewardFunctionFromTerms();
            String weights = ExperimentUtils.rewardTermsWeights(experiment, ", ");
            if (StringUtils.isNotEmpty(weights)) {
                snippet += "\n\n" + "reward_terms_weights=" + weights;
            }
        }

        setValue(snippet, experiment.isWithRewardTerms());
    }

    public void setComparisonModeTheOtherRewardFunction(String comparisonModeTheOtherRewardFunction, String comparisonModeTheOtherRewardFunctionFromTerms, Boolean isWithRewardTerms) {
        getElement().setProperty("comparisonCodeSnippet", isWithRewardTerms ? comparisonModeTheOtherRewardFunctionFromTerms : comparisonModeTheOtherRewardFunction);
        getElement().setProperty("comparisonIsWithRewardTerms", isWithRewardTerms);
    }

    public void setValue(String rewardFunction, Boolean isWithRewardTerms) {
        getElement().setProperty("codeSnippet", rewardFunction);
        getElement().setProperty("isWithRewardTerms", isWithRewardTerms);
    }
}