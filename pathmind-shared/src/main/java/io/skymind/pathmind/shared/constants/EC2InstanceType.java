package io.skymind.pathmind.shared.constants;

public enum  EC2InstanceType {
    M5A_2XL("m5a.2xlarge"),
    T2_2XL("t2.2xlarge"),
    H1_2XL("h1.2xlarge"),
    T3_2XL("t3.2xlarge"),
    M4_2XL("m4.2xlarge"),
    T3A_2XL("t3a.2xlarge"),
    M5_2XL("m5.2xlarge"),
    M5D_2XL("m5d.2xlarge");

    private String name;

    EC2InstanceType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
