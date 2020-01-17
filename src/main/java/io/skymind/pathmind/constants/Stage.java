package io.skymind.pathmind.constants;

public enum Stage {

	SetUpSimulation(0, "Set up simulation"), 
	WriteRewardFunction(1, "Write reward function"), 
	DiscoveryRunTraining(2, "Discovery run training"), 
	FullRunTraining(3, "Full run training"), 
	Export(4, "Export");
	
	private int id;
	private String name;

	private Stage(int id, String name) {
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
