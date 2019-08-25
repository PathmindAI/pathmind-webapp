package io.skymind.pathmind.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Experiment
{
	@Id
	private long id;

	@NotNull
	private LocalDate date;

	@NotNull
	private int runType;

	@NotNull
	private int score;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	public Experiment() {
	}

	public Experiment(long id, @NotNull LocalDate date, @NotNull int runType, @NotNull int score) {
		this.id = id;
		this.date = date;
		this.runType = runType;
		this.score = score;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
