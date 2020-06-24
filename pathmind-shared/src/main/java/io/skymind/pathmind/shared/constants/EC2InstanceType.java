package io.skymind.pathmind.shared.constants;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum  EC2InstanceType {
    IT_16CPU_32GB("16cpu_32gb"),
    IT_16CPU_64GB("16cpu_64gb"),
    IT_8CPU_16GB("8cpu_16gb"),
    IT_8CPU_32GB("8cpu_32gb"),
    IT_36CPU_72GB("36cpu_72gb"),
    DEFAULT("default");

    private String name;

    EC2InstanceType(String name) {
        this.name = name;
    }

    public static EC2InstanceType fromName(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name.equals(name))
                .findAny()
                .get();
    }

    @JsonValue
    public String toString() {
        return this.name;
    }
}
