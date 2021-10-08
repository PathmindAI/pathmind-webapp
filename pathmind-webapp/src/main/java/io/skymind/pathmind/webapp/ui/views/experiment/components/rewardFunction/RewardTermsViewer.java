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
    private List<RewardVariable> rewardVariables;

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
            setRewardVariables(experiment.getRewardVariables());
            setValue(experiment.getRewardTerms(), experiment.isWithRewardTerms());
        } else {
            getElement().setVisible(false);
        }
    }

    public void setValue(List<RewardTerm> rewardTerms, Boolean isWithRewardTerms) {
        JsonArray rewardTermsArray = Json.createArray();
        rewardTerms.sort(Comparator.comparing(RewardTerm::getIndex));
        for (RewardTerm rewardTerm : rewardTerms) {
            int index = rewardTermsArray.length();
            JsonObject rewardTermItem = Json.createObject();
            rewardTermItem.put("index", rewardTerm.getIndex().toString());
            rewardTermItem.put("weight", rewardTerm.getWeight().toString());
            rewardTermItem.put("rewardVariable", rewardTerm.getRewardVariableIndex() != null ? rewardVariables.get(rewardTerm.getRewardVariableIndex()).getName() : "");
            rewardTermItem.put("goalCondition", rewardTerm.getGoalCondition() != null ? rewardTerm.getGoalCondition().getCode() : "undefined");
            rewardTermItem.put("rewardSnippet", rewardTerm.getRewardSnippet() != null ? rewardTerm.getRewardSnippet() : "");

            rewardTermsArray.set(index, rewardTermItem);
        }
        getElement().callJsFunction("setRewardTerms", rewardTermsArray);
        getElement().setProperty("isWithRewardTerms", isWithRewardTerms);
    }

    private void setRewardVariables(List<RewardVariable> rewardVariables) {
        this.rewardVariables = ListUtils.emptyIfNull(rewardVariables);
        this.rewardVariables.sort(Comparator.comparing(RewardVariable::getArrayIndex));
    }
}
