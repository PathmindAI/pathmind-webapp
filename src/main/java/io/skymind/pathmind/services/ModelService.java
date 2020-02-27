package io.skymind.pathmind.services;

import io.skymind.pathmind.data.Model;

import java.util.Optional;

public interface ModelService {
    long addModelToProject(Model model, long id, String userNotes);
    Optional<Model> getModel(long modelId);
    byte[] getModelFile(long id);
    void saveModelFile(long modelId, byte[] file);
}
