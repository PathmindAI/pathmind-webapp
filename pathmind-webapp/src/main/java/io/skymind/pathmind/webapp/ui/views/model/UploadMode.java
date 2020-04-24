package io.skymind.pathmind.webapp.ui.views.model;

import java.util.Arrays;
import java.util.Optional;

public enum UploadMode {
	FOLDER, ZIP, RESUME;
	
	public static Optional<UploadMode> getEnumFromValue(String name) {
		return Arrays.stream(values())
				.filter(mode -> mode.name().equalsIgnoreCase(name))
				.findAny();
	}
}
