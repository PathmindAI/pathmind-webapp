package io.skymind.pathmind.webapp.ui.views.dashboard.utils;

public enum Stage {

	SetUpSimulation(0, "Set up simulation"), 
	WriteRewardFunction(1, "Write reward function"), 
	TrainPolicy(2, "Train policy"), 
	Export(3, "Export"),
	Completed(4, "Completed");
	
	private int id;
	private String name;

	private Stage(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public String getNameAfterDone() {
		if (TrainPolicy.getValue() == id) {
			return "Training";
		} else {
			return getName();
		}
	}

	public int getValue() {
		return id;
	}

}
