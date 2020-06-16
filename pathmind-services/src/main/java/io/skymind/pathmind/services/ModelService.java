package io.skymind.pathmind.services;

import java.util.Optional;

import io.skymind.pathmind.shared.data.Model;

public interface ModelService {
    Optional<Model> getModel(long modelId);

    byte[] getModelFile(long modelId);
    void addDraftModelToProject(Model model, long id, String modelNotes);
    void updateDraftModel(Model model, String modelNotes);
    long resumeModelCreation(Model model, String modelNotes);
    String buildModelPath(long modelId);
}
