package io.skymind.pathmind.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Project
{
	@Id
	private long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDate dateCreated;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Experiment> experiments;

	public Project() {
	}

	public Project(long id, @NotNull String name, @NotNull LocalDate dateCreated) {
		this.id = id;
		this.name = name;
		this.dateCreated = dateCreated;
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

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(List<Experiment> experiments) {
		this.experiments = experiments;
	}
}
