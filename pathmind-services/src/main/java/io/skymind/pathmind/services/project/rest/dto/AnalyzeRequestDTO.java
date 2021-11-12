package io.skymind.pathmind.services.project.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalyzeRequestDTO {
    public enum ModelType {
        ANY_LOGIC,
        PYTHON
    }

    private String id;
    private ModelType type;
    private String mainAgent;
    private String experimentClass;
    private String experimentType;
    private String pathmindHelperClass;
    private String environment;
    private String obsSelection;
    private String rewFctName;

    public AnalyzeRequestDTO(String id, ModelType type, String mainAgent, String experimentClass, String experimentType, String pathmindHelperClass) {
        this.id = id;
        this.type = type;
        this.mainAgent = mainAgent;
        this.experimentClass = experimentClass;
        this.experimentType = experimentType;
        this.pathmindHelperClass = pathmindHelperClass;
    }

    public AnalyzeRequestDTO(String id, ModelType type, String environment, String obsSelection, String rewFctName) {
        this.id = id;
        this.type = type;
        this.environment = environment;
        this.obsSelection = obsSelection;
        this.rewFctName = rewFctName;
    }
}