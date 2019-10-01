package io.skymind.pathmind.constants;

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
}