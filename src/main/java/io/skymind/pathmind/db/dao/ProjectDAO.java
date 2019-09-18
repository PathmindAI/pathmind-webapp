package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.repositories.ProjectRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDAO extends ProjectRepository
{
	// TODO -> Needs to be transactional and save the project, model, and experiment (draft) all in one step or rollback.
	public Project saveNewProject(Project project) {
		return null;
	}
}
