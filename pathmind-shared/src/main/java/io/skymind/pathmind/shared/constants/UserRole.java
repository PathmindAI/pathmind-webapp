package io.skymind.pathmind.shared.constants;

import java.util.Arrays;

public enum UserRole {
    Trial(0, "Trial"),
    Paid(1, "Paid"),
    Premium(2, "Premium"),
    Admin(3, "Admin"),
    Master(4, "Master");

    private int id;
    private String name;

    UserRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static UserRole getEnumFromId(int id) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.getId() == id)
                .findAny()
                .get();
    }
}
