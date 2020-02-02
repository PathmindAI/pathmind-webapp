package io.skymind.pathmind.constants;

public enum GuideStep {

    Overview(0, "Overview"),
    InstallPathmindHelper(1, "Install Pathmind Helper"),
    BuildObservationSpace(2, "Build Observation Space"),
    BuildActionSpace(3, "Build Action Space"),
    TriggerAction(4, "Triggering Actions"),
    DefineDoneCondition(5, "Define \"Done\" Condition"),
    DefineRewardVariable(6, "Define Reward Variables"),
    Recap(7, "Conclusion / Re-cap"),
    Completed(8, "");

    private long id;
	private String name;

    private GuideStep(long id, String name) {
        this.id = id;
        this.name = name;
    }

	public String toString() {
		return name;
	}

	public long getId() {
		return id;
    }
    
    // TODO -> For now if you try to go back it just stays the same. We should however throw a RuntimeException much like an ArrayOutOfBoundException. But I didn't
    // want to go beyond this basic setup until we decide which options we want to go with.
    public GuideStep nextStep() {
        return ordinal() >= GuideStep.values().length - 1 ? Completed : GuideStep.values()[ordinal() + 1];
    }

    // TODO -> For now if you try to go back it just stays the same. We should however throw a RuntimeException much like an ArrayOutOfBoundException. But I didn't
    // want to go beyond this basic setup until we decide which options we want to go with.
    public GuideStep previousStep() {
        return ordinal() == 0 ? Overview : GuideStep.values()[ordinal() - 1];
    }

    // Only here in case the values change over time and no longer match their ordinal value.
    public int getValue() {
        return ordinal();
    }
}
