package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Experiment;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;


@Repository
public class ExperimentRepository
{
	@Autowired
	private DSLContext dslContext;

	public List<Experiment> getExperimentsForProject(long projectId) {
		return dslContext
				.selectFrom(EXPERIMENT)
				.where(EXPERIMENT.PROJECT_ID.eq(projectId))
				.fetchInto(Experiment.class);
	}
}