package io.skymind.pathmind.shared.constants;

public enum ViewPermission {
    BASIC_READ("basic:read"),
    SETTINGS_READ("settings:read");

    private final String permission;

    ViewPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
