package io.skymind.pathmind.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Experiment
{
	@Id
	private long id;

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "DATE")
	private LocalDate date;

	// TODO -> Should use the RunType enum that I've created and map it to the database.
	@NotNull
	@Column(name = "RUN_TYPE")
	private int runType;

	@NotNull
	@Column(name = "SCORE")
	private int score;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Run> runs;

	public Experiment() {
	}

	public Experiment(@NotNull String name, @NotNull LocalDate date, @NotNull int runType, @NotNull int score, Project project) {
		this.name = name;
		this.date = date;
		this.runType = runType;
		this.score = score;
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
}
