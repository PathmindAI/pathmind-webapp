package io.skymind.pathmind.constants;

import java.util.Arrays;

public enum RunType
{
	DRAFT(-1, "Draft"),
    TestRun(1, "Test Run"),
    DiscoveryRun(2, "Discovery Run"),
    FullRun(3, "Full Run");

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