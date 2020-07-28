package io.skymind.pathmind.webapp.ui.mocks;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.data.Project;

public class ProjectMock extends Project {
    private LocalDateTime dateCreated = Constants.mockDate();
    private LocalDateTime lastActivityDate = Constants.mockDate();
    public ProjectMock() {
        setId(18854);
        setName("Project Mock Name");
        setDateCreated(dateCreated);
        setLastActivityDate(lastActivityDate);
    }
}