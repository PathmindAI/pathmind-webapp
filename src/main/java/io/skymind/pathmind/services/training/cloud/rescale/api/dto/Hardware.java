package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import javax.validation.constraints.NotNull;

public class Hardware {
    @NotNull
    private String coreType;
    private int coresPerSlot;
    private int walltime;

    // for deserialization
    private Hardware(){}

    public Hardware(@NotNull String coreType, int coresPerSlot, int walltime) {
        this.coreType = coreType;
        this.coresPerSlot = coresPerSlot;
        this.walltime = walltime;
    }

    public static Hardware mercury(){
        return new Hardware("mercury", 7, 24);
    }

    @NotNull
    public String getCoreType() {
        return this.coreType;
    }

    public void setCoreType(String coreType) {
        this.coreType = coreType;
    }

    public int getCoresPerSlot() {
        return this.coresPerSlot;
    }

    public void setCoresPerSlot(int coresPerSlot) {
        this.coresPerSlot = coresPerSlot;
    }

    public int getWalltime() {
        return this.walltime;
    }

    public void setWalltime(int walltime) {
        this.walltime = walltime;
    }
}