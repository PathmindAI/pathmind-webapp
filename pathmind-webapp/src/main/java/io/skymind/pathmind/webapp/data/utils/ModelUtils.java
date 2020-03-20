package io.skymind.pathmind.webapp.data.utils;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.mock.MockDefaultValues;

import java.time.LocalDateTime;

public class ModelUtils
{
	private ModelUtils() {
	}

	public static Model generateNewDefaultModel() {
		Model model = new Model();
		if(MockDefaultValues.isDebugAccelerate())
			ModelUtils.addNewProjectModelSettings(model);
		model.setName(Model.DEFAULT_INITIAL_MODEL_NAME);
		model.setDateCreated(LocalDateTime.now());
		return model;
	}

	private static void addNewProjectModelSettings(Model model) {
		model.setNumberOfObservations(MockDefaultValues.NEW_PROJECT_NUMBER_OF_OBSERVATIONS);
		model.setNumberOfPossibleActions(MockDefaultValues.NEW_PROJECT_NUMBER_OF_POSSIBLE_ACTIONS);
	}
}
