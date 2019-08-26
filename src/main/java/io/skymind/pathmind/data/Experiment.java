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
	private String name;

	@NotNull
	private LocalDate date;

	// TODO -> Should use the RunType enum that I've created and map it to the database.
	@NotNull
	private int runType;

	@NotNull
	private int score;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	public Experiment() {
	}

	public Experiment(long id, @NotNull String name, @NotNull LocalDate date, @NotNull int runType, @NotNull int score) {
		this.id = id;
		this.name = name;
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
}
