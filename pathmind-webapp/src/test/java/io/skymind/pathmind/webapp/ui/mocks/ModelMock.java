package io.skymind.pathmind.webapp.ui.mocks;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.data.Model;

public class ModelMock extends Model {
    private LocalDateTime dateCreated = Constants.mockDate();
    private LocalDateTime lastActivityDate = Constants.mockDate();
    public ModelMock() {
        setId(123);
        setName("1");
        setDateCreated(dateCreated);
        setLastActivityDate(lastActivityDate);
        setPackageName("modelMock_v1");
        setDraft(false);
    }
}