package io.skymind.pathmind.webapp.ui.mocks;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.data.Experiment;

public class ExperimentMock extends Experiment {
    private LocalDateTime dateCreated = Constants.mockDate();
    private LocalDateTime lastActivityDate = Constants.mockDate();
    public ExperimentMock() {
        setId(2);
        setName("1");
        setDateCreated(dateCreated);
        setLastActivityDate(lastActivityDate);
    }
}