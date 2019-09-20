package io.skymind.pathmind.constants;

import java.util.Arrays;

public enum RunType
{
    TestRun(1, "Test Run"),
    DiscoverRun(2, "Discovery Run");

	private int id;
    private String name;

	private RunType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getValue() {
		return id;
	}

	public static RunType getEnumFromValue(int value) {
		return Arrays.stream(values())
				.filter(runType -> runType.getValue() == value)
				.findAny()
				.get();
	}
}