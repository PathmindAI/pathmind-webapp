package io.skymind.pathmind.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Project implements Data
{
	@Id
	private long id;

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "DATE_CREATED")
	private LocalDate dateCreated;

	@OneToMany(mappedBy = "PROJECT", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Experiment> experiments;

	@ManyToOne
	@JoinColumn(name = "PATHMIND_USER_ID", nullable = false)
	private PathmindUser pathmindUser;

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
}
