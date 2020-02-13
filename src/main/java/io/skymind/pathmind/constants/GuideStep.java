package io.skymind.pathmind.constants;

public enum GuideStep {

    Overview("Overview", "overview"),
    InstallPathmindHelper("Install Pathmind Helper", "install"),
    BuildObservationSpace("Build Observation Space", "observation"),
    BuildActionSpace("Build Action Space", "action-space"),
    TriggerAction("Triggering Actions", "trigger-actions"),
    DefineDoneCondition("Define \"Done\" Condition", "done-condition"),
    DefineRewardVariable("Define Reward Variables", "reward"),
    Recap("Conclusion / Re-cap", "recap"),
    Completed("", "recap");

	private String name;
    private String path;

    private GuideStep(String name, String path) {
        this.name = name;
        this.path = path;
    }

	public String toString() {
		return name;
    }
    
    public String getPath() {
        return "guide/"+path;
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
