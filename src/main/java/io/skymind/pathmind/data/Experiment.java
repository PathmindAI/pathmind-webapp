package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Experiment implements Data
{
	private long id;
	private String name;
	// TODO -> Rename to dateCreated
	private LocalDate date;

	// TODO -> Not stored anywhere for now.
	private Instant startTime;
	private Instant endTime;

	@NotNull
	private int runType;
	@NotNull
	private int score;

	private int status = RunStatus.NotStarted.getValue();
	private int completed = RunStatus.NotStarted.getValue();

	@NotNull
	private long modelId;

	@NotNull
	private String rewardFunction;

	private Model model;

	private List<Run> runs;

	// TODO -> This needs to be properly implemented and stored in the database.
	private Algorithm algorithm = Algorithm.DQN;

	private long duration = 0;

	// TODO -> What is the data?
	private List<Number> scores = new ArrayList<Number>();

	// TODO -> To implement
	private String notes = "Todo";

	public Experiment() {
	}

	public Experiment(@NotNull String name, @NotNull LocalDate date, @NotNull RunType runType, Algorithm algorithm, long duration, @NotNull int score, @NotNull String rewardFunction, Model model, @NotNull long modelId, String notes) {
		this.name = name;
		this.date = date;
		this.runType = runType.getValue();
		this.algorithm = algorithm;
		this.duration = duration;
		this.score = score;
		this.rewardFunction = rewardFunction;
		this.model = model;
		this.modelId = modelId;
		this.notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getRunType() {
		return runType;
	}

	public void setRunType(int runType) {
		this.runType = runType;
	}

	// Issue #34 Properly convert EnumTypes with Converters in JOOQ
	public RunType getRunTypeEnum() {
		return RunType.getEnumFromValue(runType);
	}

	public void setRunTypeEnum(RunType runTypeEnum) {
		this.runType = runTypeEnum.getValue();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Run> getRuns() {
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	public String getRewardFunction() {
		return rewardFunction;
	}

	public void setRewardFunction(String rewardFunction) {
		this.rewardFunction = rewardFunction;
	}

	public long getModelId() {
		return modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// Issue #34 Properly convert EnumTypes with Converters in JOOQ
	public RunStatus getStatusEnum() {
		return RunStatus.getEnumFromValue(status);
	}

	public void setStatusEnum(RunStatus runStatusEnum) {
		this.status = runStatusEnum.getValue();
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	// Issue #34 Properly convert EnumTypes with Converters in JOOQ
	public RunStatus getCompletedEnum() {
		return RunStatus.getEnumFromValue(completed);
	}

	public void setCompleted(RunStatus runStatusEnum) {
		this.completed = runStatusEnum.getValue();
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public void startExperimentNow() {
		startTime = Instant.now();
	}

	public List<Number> getScores() {
		return scores;
	}

	public void setScores(List<Number> scores) {
		this.scores = scores;
	}

	public Number getLastScore() {
		return scores.get(scores.size() - 1);
	}

	@Override
	public String toString() {
		return "Experiment{" +
				"id=" + id +
				", name='" + name + '\'' +
				", date=" + date +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", runType=" + runType +
				", score=" + score +
				", status=" + status +
				", completed=" + completed +
				", modelId=" + modelId +
				", rewardFunction='" + rewardFunction + '\'' +
				", model=" + model +
				", runs=" + runs +
				", algorithm=" + algorithm +
				", duration=" + duration +
				", scores=" + scores +
				'}';
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
