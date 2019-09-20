package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class JobSummary {
    @Nullable
    private ClusterStatus clusterStatusDisplay;
    @NotNull
    private LocalDateTime dateInserted;
    @NotNull
    private String name;
    @NotNull
    private List analysisNames;
    private long storage;
    @NotNull
    private JobSummaryStatus jobStatus;
    @NotNull
    private List sharedWith;
    private boolean isVisible;
    @NotNull
    private String owner;
    @NotNull
    private String id;

    // for deserialization
    private JobSummary(){}

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

    @Nullable
    public ClusterStatus getClusterStatusDisplay() {
        return this.clusterStatusDisplay;
    }

    public void setClusterStatusDisplay(@Nullable ClusterStatus clusterStatusDisplay) {
        this.clusterStatusDisplay = clusterStatusDisplay;
    }

    @NotNull
    public LocalDateTime getDateInserted() {
        return this.dateInserted;
    }

    public void setDateInserted(LocalDateTime dateInserted) {
        this.dateInserted = dateInserted;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public List getAnalysisNames() {
        return this.analysisNames;
    }

    public void setAnalysisNames(List analysisNames) {
        this.analysisNames = analysisNames;
    }

    public long getStorage() {
        return this.storage;
    }

    public void setStorage(long storage) {
        this.storage = storage;
    }

    @NotNull
    public JobSummaryStatus getJobStatus() {
        return this.jobStatus;
    }

    public void setJobStatus(JobSummaryStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    @NotNull
    public List getSharedWith() {
        return this.sharedWith;
    }

    public void setSharedWith(List sharedWith) {
        this.sharedWith = sharedWith;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @NotNull
    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
