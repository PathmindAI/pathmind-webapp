package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainingError {
    private long id;
    private String keyword;
    private String description;
    private boolean restartable;
    private String supportArticle;
}
