package io.skymind.pathmind.data;

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

	@NotNull
	private String rewardFunction;

	private Project project;

	private List<Run> runs;

	public Experiment() {
	}

	public Experiment(@NotNull String name, @NotNull LocalDate date, @NotNull int runType, @NotNull int score, @NotNull String rewardFunction, Project project) {
		this.name = name;
		this.date = date;
		this.runType = runType;
		this.score = score;
		this.rewardFunction = rewardFunction;
		this.project = project;
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
}
