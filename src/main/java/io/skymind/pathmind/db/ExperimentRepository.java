package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long>
{
	List<Experiment> findExperimentsByProjectId(long projectId);
}
