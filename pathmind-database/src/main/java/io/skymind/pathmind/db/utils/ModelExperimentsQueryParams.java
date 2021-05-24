package io.skymind.pathmind.db.utils;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ModelExperimentsQueryParams {
    private final long userId;
    private final long modelId;
    private final int limit;
    private final int offset;
    private final boolean isArchived;
    private final String sortBy;
    private final boolean descending;
}