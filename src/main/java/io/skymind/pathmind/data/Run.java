package io.skymind.pathmind.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Run
{
	@Id
	private long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDate date;

	@ManyToOne
	@JoinColumn(name = "experiment_id", nullable = false)
	private Experiment experiment;

	public Run() {
	}

	public Run(long id, @NotNull String name, @NotNull LocalDate date, Experiment experiment) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.experiment = experiment;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
}
