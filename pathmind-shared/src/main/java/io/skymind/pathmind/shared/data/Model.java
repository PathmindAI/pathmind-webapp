package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Model extends ArchivableData
{
    private static final long serialVersionUID = 3143001029296125340L;

	public static final int DEFAULT_NUMBER_OF_OBSERVATIONS = 1;
	public static final int DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS = 1;

	public static final String DEFAULT_INITIAL_MODEL_NAME = "Initial Model Revision";

	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private int numberOfObservations = DEFAULT_NUMBER_OF_OBSERVATIONS;
	private int numberOfPossibleActions = DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS;
	private transient byte[] file;
	private long projectId;
	private String userNotes;
	private boolean draft;
	private int rewardVariablesCount;
	private String packageName;
	private int actionTupleSize;

}
