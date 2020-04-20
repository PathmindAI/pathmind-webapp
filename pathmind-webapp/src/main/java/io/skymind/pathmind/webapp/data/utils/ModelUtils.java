package io.skymind.pathmind.webapp.data.utils;

import io.skymind.pathmind.shared.data.Model;

import java.time.LocalDateTime;

public class ModelUtils
{
	private ModelUtils() {
	}

	public static Model generateNewDefaultModel() {
		Model model = new Model();
		model.setName(Model.DEFAULT_INITIAL_MODEL_NAME);
		model.setDateCreated(LocalDateTime.now());
		return model;
	}
}
