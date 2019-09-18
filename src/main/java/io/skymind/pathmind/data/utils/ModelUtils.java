package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.security.SecurityUtils;

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
