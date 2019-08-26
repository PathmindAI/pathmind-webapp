package io.skymind.pathmind.data;

// TODO -> Implement
public class Reward
{
	private int id;
	private String function;

	public Reward(int id, String function) {
		this.id = id;
		this.function = function;
	}

	@Override
	public String toString() {
		return id + " rewards " + function;
	}
}
