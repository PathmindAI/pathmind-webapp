package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

import org.apache.commons.collections4.ListUtils;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardTerm;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

@Tag("reward-terms-viewer")
@JsModule("./src/experiment/reward-terms-viewer.ts")
public class RewardTermsViewer extends LitTemplate implements ExperimentComponent {
    public RewardTermsViewer() {
        super();
    }
    
    public RewardTermsViewer(Experiment experiment) {
        super();
        setExperiment(experiment);
    }

    public void setExperiment(Experiment experiment) {
        if (experiment.isWithRewardTerms()) {
            getElement().setVisible(true);
            setValue(experiment.getRewardTerms(), experiment.isWithRewardTerms(), experiment.getRewardVariables());
        } else {
            getElement().setVisible(false);
        }
    }

    public void setValue(List<RewardTerm> rewardTerms, Boolean isWithRewardTerms, List<RewardVariable> rewardVariables) {
        rewardTerms.sort(Comparator.comparing(RewardTerm::getIndex));
        getElement().callJsFunction("setRewardTerms", getArrayFromRewardTermsList(rewardTerms, rewardVariables));
        getElement().setProperty("isWithRewardTerms", isWithRewardTerms);
    }

    public void setComparisonModeTheOtherRewardTerms(List<RewardTerm> rewardTerms, Boolean comparisonIsWithRewardTerms, List<RewardVariable> rewardVariables) {
        rewardTerms.sort(Comparator.comparing(RewardTerm::getIndex));
        getElement().callJsFunction("setComparisonRewardTerms", getArrayFromRewardTermsList(rewardTerms, rewardVariables));
        getElement().setProperty("comparisonIsWithRewardTerms", comparisonIsWithRewardTerms);
    }

    private JsonArray getArrayFromRewardTermsList(List<RewardTerm> rewardTerms, List<RewardVariable> rewardVariables) {
        List<RewardVariable> rewardVariablesList = ListUtils.emptyIfNull(rewardVariables);
        rewardVariablesList.sort(Comparator.comparing(RewardVariable::getArrayIndex));
        JsonArray rewardTermsArray = Json.createArray();
        for (RewardTerm rewardTerm : rewardTerms) {
            int index = rewardTermsArray.length();
            JsonObject rewardTermItem = Json.createObject();
            rewardTermItem.put("index", rewardTerm.getIndex().toString());
            rewardTermItem.put("weight", rewardTerm.getWeight().toString());
            rewardTermItem.put("rewardVariable", rewardTerm.getRewardVariableIndex() != null && rewardVariables != null ? rewardVariables.get(rewardTerm.getRewardVariableIndex()).getName() : "");
            rewardTermItem.put("goalCondition", rewardTerm.getGoalCondition() != null ? rewardTerm.getGoalCondition().getCode() : "undefined");
            rewardTermItem.put("rewardSnippet", rewardTerm.getRewardSnippet() != null ? rewardTerm.getRewardSnippet() : "");

            rewardTermsArray.set(index, rewardTermItem);
        }
        return rewardTermsArray;
    }
}
