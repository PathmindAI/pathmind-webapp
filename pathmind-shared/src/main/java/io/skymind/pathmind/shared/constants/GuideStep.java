package io.skymind.pathmind.shared.constants;

public enum GuideStep {

    Overview("Overview", "overview"),
    InstallPathmindHelper("Install Pathmind Helper", "install"),
    BuildActionSpace("Actions", "action-space"),
    TriggerAction("Event Trigger", "trigger-actions"),
    BuildObservationSpace("Observations", "observation"),
    DefineRewardVariable("Reward Variables", "reward"),
    DefineDoneCondition("isDone", "done-condition"),
    RunTest("Run a Test", "test-simulation"),
    Recap("Export as a Standalone Java App", "recap"),
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
