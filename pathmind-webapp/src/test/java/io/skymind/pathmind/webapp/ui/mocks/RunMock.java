package io.skymind.pathmind.webapp.ui.mocks;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.data.Run;

public class RunMock extends Run {
    private LocalDateTime startedAt = Constants.mockDate();
    
    public RunMock() {
        setId(3);
        setExperimentId(2);
        setStartedAt(startedAt);
    }
}
