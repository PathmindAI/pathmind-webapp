package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.Action;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.RewardVariable;

import java.util.List;
import java.util.Optional;

public interface ModelService {
    Optional<Model> getModel(long modelId);

    List<Action> getModelActions(long modelId);

    byte[] getModelFile(long modelId);
    void addDraftModelToProject(Model model, long id, String modelNotes);
    void updateDraftModel(Model model, String modelNotes);
    long resumeModelCreation(Model model, String modelNotes);
    void updateModelRewardVariables(Model model, List<RewardVariable> rewardVariables);
    void updateModelActions(Model model, List<Action> actions);
    List<RewardVariable> getModelRewardVariables(long modelId);
    String buildModelPath(long modelId);
}
