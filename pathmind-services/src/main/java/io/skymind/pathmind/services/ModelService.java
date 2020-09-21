package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.RewardVariable;

import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.shared.data.Model;

public interface ModelService {
    Optional<Model> getModel(long modelId);

    byte[] getModelFile(long modelId);
    void addDraftModelToProject(Model model, long id, String modelNotes);
    void updateDraftModel(Model model, String modelNotes);
    Experiment resumeModelCreation(Model model, String modelNotes);
    String buildModelPath(long modelId);
    String buildModelAlpPath(long modelId);

    void saveModelAlp(Model model);

    void removeModelAlp(Model model);

    boolean hasModelAlp(long modelId);
    Optional<byte[]> getModelAlp(long modelId);
}
