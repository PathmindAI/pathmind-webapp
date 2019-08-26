package io.skymind.pathmind.constants;

public enum ModelTimeUnit
{
    Seconds("Seconds"),
    Minutes("Minutes"),
    Hours("Hours");

    String timeUnitName;

    private ModelTimeUnit(String timeUnitName) {
        this.timeUnitName = timeUnitName;
    }

    public String toString() {
        return timeUnitName;
    }
}