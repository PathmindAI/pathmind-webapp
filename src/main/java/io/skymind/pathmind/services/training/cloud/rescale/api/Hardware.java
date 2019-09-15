package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class Hardware {
    @NotNull
    private final String coreType;
    private final int coresPerSlot;
    private final int walltime;

    @NotNull
    public final String getCoreType() {
        return this.coreType;
    }

    public final int getCoresPerSlot() {
        return this.coresPerSlot;
    }

    public final int getWalltime() {
        return this.walltime;
    }

    public Hardware(@NotNull String coreType, int coresPerSlot, int walltime) {
        this.coreType = coreType;
        this.coresPerSlot = coresPerSlot;
        this.walltime = walltime;
    }

    public static Hardware mercury(){
        return new Hardware("mercury", 7, 24);
    }
}