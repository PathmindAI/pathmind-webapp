package io.skymind.pathmind.data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class Run implements Data
{
	private long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDate date;

	private Experiment experiment;

	public Run() {
	}

	public Run(long id, @NotNull String name, @NotNull LocalDate date, Experiment experiment) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.experiment = experiment;
	}

	@Override
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
