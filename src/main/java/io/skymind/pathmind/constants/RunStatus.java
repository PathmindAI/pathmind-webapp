package io.skymind.pathmind.constants;

public enum RunStatus
{
	NotStarted(1, "Not Started"),
	Running(2, "Running"),
	Completed(2, "Completed");

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
}