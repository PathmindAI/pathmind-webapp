package io.skymind.pathmind.constants;

import java.util.Arrays;

// TODO -> Rename once I understand what this represents: https://docs.google.com/document/d/1FxRjOE6fW2nseu7myk6K6iGNEErmK38zoEMcNdD155M/edit?ts=5d8017fe#
public enum TestRun
{
    Draft(1, "Draft");

	private int id;
    private String name;

	private TestRun(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getValue() {
		return id;
	}

	public static io.skymind.pathmind.constants.TestRun getEnumFromValue(int value) {
		return Arrays.stream(values())
				.filter(testRun -> testRun.getValue() == value)
				.findAny()
				.get();
	}
}