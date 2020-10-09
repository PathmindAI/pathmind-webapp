package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Model extends ArchivableData implements DeepCloneableInterface<Model> {
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

    @Override
    public Model shallowClone() {
        return super.shallowClone(Model.builder()
                .dateCreated(dateCreated)
                .lastActivityDate(lastActivityDate)
                .numberOfObservations(numberOfObservations)
                .file(file == null ? null : Arrays.copyOf(file, file.length))
                .alpFile(alpFile == null ? null : Arrays.copyOf(alpFile, alpFile.length))
                .projectId(projectId)
                .userNotes(userNotes)
                .draft(draft)
                .hasGoals(hasGoals)
                .rewardVariablesCount(rewardVariablesCount)
                .packageName(packageName)
                .invalidModel(invalidModel)
                .build());
    }
	private int modelType;
}
