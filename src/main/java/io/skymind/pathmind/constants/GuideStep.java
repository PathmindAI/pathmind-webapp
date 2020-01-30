package io.skymind.pathmind.constants;

public enum GuideStep {

    Overview("Overview"),
    InstallPathmindHelper("Install Pathmind Helper"),
    BuildObservationSpace("Build Observation Space"),
    BuildActionSpace("Build Action Space"),
    TriggerAction("Triggering Actions"),
    DefineDoneCondition("Define \"Done\" Condition"),
    DefineRewardVariable("Define Reward Variables"),
    Recap("Conclusion / Re-cap"),
    Completed("");

	private String name;

    private GuideStep(String name) {
        this.name = name;
    }

	public String toString() {
		return name;
	}

    // STEPH -> TODO -> For now if you try to go back it just stays the same. We should however throw a RuntimeException much like an ArrayOutOfBoundException. But I didn't
    // want to go beyond this basic setup until we decide which options we want to go with.
    public GuideStep nextStep() {
        return ordinal() >= GuideStep.values().length - 1 ? Completed : GuideStep.values()[ordinal() + 1];
    }

    // STEPH -> TODO -> For now if you try to go back it just stays the same. We should however throw a RuntimeException much like an ArrayOutOfBoundException. But I didn't
    // want to go beyond this basic setup until we decide which options we want to go with.
    public GuideStep previousStep() {
        return ordinal() == 0 ? Overview : GuideStep.values()[ordinal() - 1];
    }

    // STEPH -> TODO -> Only here to explain how I plan to save to the database in the Repository layer of the code.
    public int getValue() {
        return ordinal();
    }
}
