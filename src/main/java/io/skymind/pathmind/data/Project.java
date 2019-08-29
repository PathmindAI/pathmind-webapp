package io.skymind.pathmind.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Project
{
	@Id
	private int id;

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "DATE_CREATED")
	private LocalDate dateCreated;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Experiment> experiments;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Project() {
	}

	public Project(int id, @NotNull String name, @NotNull LocalDate dateCreated) {
		this.id = id;
		this.name = name;
		this.dateCreated = dateCreated;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
