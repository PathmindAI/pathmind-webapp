package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class Experiment implements Data
{
	private long id;
	private String name;
	private LocalDate date;

	@NotNull
	private int runType;
	@NotNull
	private int score;

	// TODO -> This needs to be properly implemented and stored in the database.
	@NotNull
	private int modelId;

	@NotNull
	private String rewardFunction;

	private Project project;

	private List<Run> runs;

	// TODO -> This needs to be properly implemented and stored in the database.
	private Algorithm algorithm;
	private long duration = 0;

	public Experiment() {
	}

	public Experiment(@NotNull String name, @NotNull LocalDate date, @NotNull RunType runType, Algorithm algorithm, long duration, @NotNull int score, @NotNull String rewardFunction, Project project, @NotNull int modelId) {
		this.name = name;
		this.date = date;
		this.runType = runType.getValue();
		this.algorithm = algorithm;
		this.duration = duration;
		this.score = score;
		this.rewardFunction = rewardFunction;
		this.project = project;
		this.modelId = modelId;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
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
}
