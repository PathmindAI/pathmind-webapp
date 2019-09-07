package io.skymind.pathmind.data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class Project implements Data
{
	private long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDate dateCreated;

	private List<Experiment> experiments;
	private PathmindUser pathmindUser;

	private LocalDate lastActivityDate;

	public Project() {
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

	@Override
	public String toString() {
		return name;
	}

	public PathmindUser getPathmindUser() {
		return pathmindUser;
	}

	public void setPathmindUser(PathmindUser pathmindUser) {
		this.pathmindUser = pathmindUser;
	}

	public LocalDate getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(LocalDate lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}
}
