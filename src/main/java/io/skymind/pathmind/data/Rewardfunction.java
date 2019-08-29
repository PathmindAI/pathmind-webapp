package io.skymind.pathmind.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Rewardfunction
{
	@Id
	private int id;

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "function")
	private String function;

	@OneToOne
	@JoinColumn(name = "experiment_id", nullable = false)
	private Experiment experiment;

	public Rewardfunction() {
	}

	public Rewardfunction(int id, @NotNull String name, @NotNull String function, Experiment experiment) {
		this.id = id;
		this.name = name;
		this.function = function;
		this.experiment = experiment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
}
