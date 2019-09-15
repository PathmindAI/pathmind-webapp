package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

class JobSummary {
    @Nullable
    private final ClusterStatus clusterStatusDisplay;
    @NotNull
    private final LocalDateTime dateInserted;
    @NotNull
    private final String name;
    @NotNull
    private final List analysisNames;
    private final long storage;
    @NotNull
    private final JobSummaryStatus jobStatus;
    @NotNull
    private final List sharedWith;
    private final boolean isVisible;
    @NotNull
    private final String owner;
    @NotNull
    private final String id;

    @Nullable
    public final ClusterStatus getClusterStatusDisplay() {
        return this.clusterStatusDisplay;
    }

    @NotNull
    public final LocalDateTime getDateInserted() {
        return this.dateInserted;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final List getAnalysisNames() {
        return this.analysisNames;
    }

    public final long getStorage() {
        return this.storage;
    }

    @NotNull
    public final JobSummaryStatus getJobStatus() {
        return this.jobStatus;
    }

    @NotNull
    public final List getSharedWith() {
        return this.sharedWith;
    }

    public final boolean isVisible() {
        return this.isVisible;
    }

    @NotNull
    public final String getOwner() {
        return this.owner;
    }

    @NotNull
    public final String getId() {
        return this.id;
    }

    public JobSummary(@Nullable ClusterStatus clusterStatusDisplay, @NotNull LocalDateTime dateInserted, @NotNull String name, @NotNull List analysisNames, long storage, @NotNull JobSummaryStatus jobStatus, @NotNull List sharedWith, boolean isVisible, @NotNull String owner, @NotNull String id) {
        this.clusterStatusDisplay = clusterStatusDisplay;
        this.dateInserted = dateInserted;
        this.name = name;
        this.analysisNames = analysisNames;
        this.storage = storage;
        this.jobStatus = jobStatus;
        this.sharedWith = sharedWith;
        this.isVisible = isVisible;
        this.owner = owner;
        this.id = id;
    }

}
