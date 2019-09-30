package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import io.skymind.pathmind.data.db.tables.records.ProjectRecord;
import io.skymind.pathmind.db.repositories.ProjectRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.skymind.pathmind.data.db.Tables.*;

@Repository
public class ProjectDAO extends ProjectRepository
{
	private final DSLContext ctx;

	ProjectDAO(DSLContext ctx){
		this.ctx = ctx;
	}

	@Transactional
	public long setupNewProject(Project project, Model model) {
		final ProjectRecord proj = PROJECT.newRecord();
		proj.attach(ctx.configuration());

		proj.setName(project.getName());
		proj.setDateCreated(LocalDateTime.now());
		proj.setLastActivityDate(proj.getDateCreated());
		proj.setPathmindUserId(project.getPathmindUserId());
		proj.store();


		final ModelRecord mod = MODEL.newRecord();
		mod.attach(ctx.configuration());

		mod.setName("Initial Model");
		mod.setDateCreated(proj.getDateCreated());
		mod.setLastActivityDate(mod.getDateCreated());
		mod.setProjectId(proj.getId());
		mod.setNumberOfPossibleActions(model.getNumberOfPossibleActions());
		mod.setNumberOfObservations(model.getNumberOfObservations());
		mod.setGetObservationForRewardFunction(model.getGetObservationForRewardFunction());
		mod.setFile(model.getFile());
		mod.store();


		final ExperimentRecord ex = EXPERIMENT.newRecord();
		ex.attach(ctx.configuration());
		ex.setDateCreated(mod.getDateCreated());
		ex.setModelId(mod.getId());
		ex.setName("Initial Experiment");
		ex.setRewardFunction("");
		ex.store();

		return ex.getId();
	}
}
