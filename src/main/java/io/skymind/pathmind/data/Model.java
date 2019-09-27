package io.skymind.pathmind.data;

import java.time.LocalDateTime;
import java.util.List;

public class Model extends ArchivableData
{
	// TODO -> Implement. These are needed for validation purposes.
	public static final int MIN_NUMBER_OF_OBSERVATIONS = 1;
	public static final int MAX_NUMBER_OF_OBSERVATIONS = 10000; // a 100*100 field doesn't seem that outlandish

	// TODO -> Implement. These are needed for validation purposes.
	public static final int MIN_NUMBER_OF_POSSIBLE_ACTIONS = 1;
	public static final int MAX_NUMBER_OF_POSSIBLE_ACTIONS = 1000;

	// TODO -> Implement
	// It doesn't really make sense to have defaults for this. -- pdubs 2019-09-26
	public static final int DEFAULT_NUMBER_OF_OBSERVATIONS = 3;
	public static final int DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS = 5;
	public static final String DEFAULT_GET_OBSERVATION_FOR_REWARD_FUNCTION = "Copy and paste your getObservation for reward function here so you" +
			"can reference it while writing your reward function in the next step.";

	public static final String DEFAULT_INITIAL_MODEL_NAME = "Initial Model Revision";

	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private int numberOfObservations = DEFAULT_NUMBER_OF_OBSERVATIONS;
	private int numberOfPossibleActions = DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS;
	private String getObservationForRewardFunction = "";
	private byte[] file;
	private boolean isArchived = false;

	private long projectId;

	private List<Experiment> experiments;

	public Model() {
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(List<Experiment> experiments) {
		this.experiments = experiments;
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
		this.getObservationForRewardFunction = getObservationForRewardFunction;
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
}
