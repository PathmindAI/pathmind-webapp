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

	public static final String DEFAULT_INITIAL_MODEL_NAME = "Initial Model Revision";

	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private int numberOfObservations = DEFAULT_NUMBER_OF_OBSERVATIONS;
	private int numberOfPossibleActions = DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS;
	private byte[] file;
	private long projectId;
	private String userNotes;

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
}
