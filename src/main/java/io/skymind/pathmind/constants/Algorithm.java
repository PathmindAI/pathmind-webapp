package io.skymind.pathmind.constants;

import java.util.Arrays;

// TODO -> Clean up the Algorithm so that it's coming a data model object rather than being called here directly.
// https://github.com/SkymindIO/pathmind-webapp/issues/101
public enum Algorithm
{
    DQN(1, "DQN");

	private int id;
    private String name;

	private Algorithm(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getValue() {
		return id;
	}

	public static Algorithm getEnumFromValue(int value) {
		return Arrays.stream(values())
				.filter(runType -> runType.getValue() == value)
				.findAny()
				.get();
	}
}