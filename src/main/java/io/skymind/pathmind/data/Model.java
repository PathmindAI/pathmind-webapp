package io.skymind.pathmind.data;

import java.time.LocalDateTime;

public class Model extends ArchivableData
{
	public static final int MIN_NUMBER_OF_OBSERVATIONS = 1;
	public static final int MAX_NUMBER_OF_OBSERVATIONS = 10000; // a 100*100 field doesn't seem that outlandish

	public static final int MIN_NUMBER_OF_POSSIBLE_ACTIONS = 1;
	public static final int MAX_NUMBER_OF_POSSIBLE_ACTIONS = 1000;

	public static final int DEFAULT_NUMBER_OF_OBSERVATIONS = 1;
	public static final int DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS = 1;
	public static final String DEFAULT_GET_OBSERVATION_FOR_REWARD_FUNCTION = "Copy and paste your getObservation for reward function here so you" +
			"can reference it while writing your reward function in the next step.";

	public static final String DEFAULT_INITIAL_MODEL_NAME = "Initial Model Revision";

	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private int numberOfObservations = DEFAULT_NUMBER_OF_OBSERVATIONS;
	private int numberOfPossibleActions = DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS;
	private String getObservationForRewardFunction = "";
	private byte[] file;
	private long projectId;
	private String userNotes;
	private int rewardVariablesCount;

	public Model() {
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public LocalDateTime getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(LocalDateTime lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getGetObservationForRewardFunction() {
		return getObservationForRewardFunction;
	}

	public void setGetObservationForRewardFunction(String getObservationForRewardFunction) {
		this.getObservationForRewardFunction = getObservationForRewardFunction != null ? getObservationForRewardFunction : "";
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public int getNumberOfObservations() {
		return numberOfObservations;
	}

	public void setNumberOfObservations(int numberOfObservations) {
		this.numberOfObservations = numberOfObservations;
	}

	public int getNumberOfPossibleActions() {
		return numberOfPossibleActions;
	}

	public void setNumberOfPossibleActions(int numberOfPossibleActions) {
		this.numberOfPossibleActions = numberOfPossibleActions;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

	public int getRewardVariablesCount() {
		return rewardVariablesCount;
	}

	public void setRewardVariablesCount(int rewardVariablesCount) {
		this.rewardVariablesCount = rewardVariablesCount;
	}
}
