package io.skymind.pathmind.data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Project implements Data
{
	// TODO -> Implement. These are needed for validation purposes.
	public static final long MIN_NUMBER_OF_OBSERVATIONS = 1;
	public static final long MAX_NUMBER_OF_OBSERVATIONS = 1000;

	// TODO -> Implement. These are needed for validation purposes.
	public static final long MIN_NUMBER_OF_POSSIBLE_ACTIONS = 1;
	public static final long MAX_NUMBER_OF_POSSIBLE_ACTIONS = 1000;

	// TODO -> Implement
	public static final long DEFAULT_NUMBER_OF_OBSERVATIONS = 3;
	public static final long DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS = 5;
	public static final String DEFAULT_GET_OBSERVATION_FOR_REWARD_FUNCTION = "Copy and paste your getObservation for reward function here so you" +
			"can reference it while writing your reward function in the next step.";

	private long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDate dateCreated;

//	private List<Experiment> experiments;
	private PathmindUser pathmindUser;

	private LocalDate lastActivityDate;

	private long numberOfObservations = DEFAULT_NUMBER_OF_OBSERVATIONS;
	private long numberOfPossibleActions = DEFAULT_NUMBER_OF_POSSIBLE_ACTIONS;
	private String getObservationForRewardFunction = "";

	public Project() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

//	public List<Experiment> getExperiments() {
//		return experiments;
//	}
//
//	public void setExperiments(List<Experiment> experiments) {
//		this.experiments = experiments;
//	}
//
	@Override
	public String toString() {
		return name;
	}

	public PathmindUser getPathmindUser() {
		return pathmindUser;
	}

	public void setPathmindUser(PathmindUser pathmindUser) {
		this.pathmindUser = pathmindUser;
	}

	public LocalDate getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(LocalDate lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public long getNumberOfObservations() {
		return numberOfObservations;
	}

	public void setNumberOfObservations(long numberOfObservations) {
		this.numberOfObservations = numberOfObservations;
	}

	public String getGetObservationForRewardFunction() {
		return getObservationForRewardFunction;
	}

	public long getNumberOfPossibleActions() {
		return numberOfPossibleActions;
	}

	public void setNumberOfPossibleActions(long numberOfPossibleActions) {
		this.numberOfPossibleActions = numberOfPossibleActions;
	}

	public void setGetObservationForRewardFunction(String getObservationForRewardFunction) {
		this.getObservationForRewardFunction = getObservationForRewardFunction;
	}
}
