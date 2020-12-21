package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Model extends ArchivableData  {

    private static final long serialVersionUID = 3143001029296125340L;

    public static final int DEFAULT_NUMBER_OF_OBSERVATIONS = 1;

    public static final String DEFAULT_INITIAL_MODEL_NAME = "Initial Model Revision";

    private LocalDateTime dateCreated;
    private LocalDateTime lastActivityDate;
    private int numberOfObservations = DEFAULT_NUMBER_OF_OBSERVATIONS;
    private transient byte[] file;
    private transient byte[] alpFile;
    private long projectId;
    private String userNotes;
    private boolean draft;
    private boolean hasGoals;
    private int rewardVariablesCount;
    private String packageName;
    private int invalidModel;
    private int modelType;
    private int numberOfAgents;
    private String pathmindHelper;
    private String mainAgent;
    private String experimentClass;
    private String experimentType;
}
