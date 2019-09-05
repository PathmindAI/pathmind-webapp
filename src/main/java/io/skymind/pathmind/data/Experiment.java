package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.RunType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Experiment implements Data
{
	@Id
	@Column(name = "ID")
	private long id;

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "DATE")
	private LocalDate date;

	@NotNull
	@Column(name = "RUN_TYPE")
	private int runType;

	@NotNull
	@Column(name = "SCORE")
	private int score;

	@NotNull
	@Column(name = "REWARD_FUNCTION")
	private String rewardFunction;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID", nullable = false)
	private Project project;

	@OneToMany(mappedBy = "EXPERIMENT", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
