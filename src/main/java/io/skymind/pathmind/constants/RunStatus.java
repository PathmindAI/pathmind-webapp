package io.skymind.pathmind.constants;

import java.util.Arrays;

public enum RunStatus
{
	NotStarted(0, "Not Started"),
	Starting(1, "Starting"),
	Running(2, "Running"),
	Completed(3, "Completed"),
	Error(4, "Error");

	private int id;
	private String name;

	private RunStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getValue() {
		return id;
	}

	public static RunStatus getEnumFromValue(int value) {
		return Arrays.stream(values())
				.filter(runStatus -> runStatus.getValue() == value)
				.findAny()
				.get();
	}
}