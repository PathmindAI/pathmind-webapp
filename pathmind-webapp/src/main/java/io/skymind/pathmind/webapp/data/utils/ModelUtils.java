package io.skymind.pathmind.webapp.data.utils;

import java.util.List;
import java.util.stream.IntStream;

import io.skymind.pathmind.shared.data.Model;

public class ModelUtils {

    private ModelUtils() {
    }

    public static boolean isSameModel(Model model, long modelId) {
        return model != null && model.getId() == modelId;
    }

    /**
     * Replace the existing model in the models list without replicating the list (for example using map and then collecting)
     * as well as keeping the exact same order in case the list is already sorted.
     */
    public static void updateModelInModelsList(List<Model> models, Model model) {
        int index = IntStream.range(0, models.size())
                .filter(x -> model.getId() == models.get(x).getId())
                .findFirst().orElse(-1);
        if (index > -1) {
            models.set(index, model);
        }
    }
}
