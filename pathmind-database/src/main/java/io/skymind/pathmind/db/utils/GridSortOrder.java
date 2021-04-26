package io.skymind.pathmind.db.utils;

public class GridSortOrder {
    private String propertyName;
    private boolean descending;

    public GridSortOrder(String propertyName, boolean descending) {
        setPropertyName(propertyName);
        setDescending(descending);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }
}
