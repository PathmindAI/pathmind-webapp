package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.ui.views.project.components.wizard.PathminderHelperWizardPanel;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.Tables.MODEL;
import static io.skymind.pathmind.data.db.Tables.RUN;

@Repository
public class RunRepository
{
    @Autowired
    private DSLContext dslContext;

//    public List<Model> getRunsForUser(long userId) {
//        return dslContext
//                .select(RUN.asterisk())
//                .select(MODEL.NAME)
//			    .from(RUN)
//				.where(MODEL.PROJECT_ID.eq(projectId))
//				.fetchInto(Model.class);
//    }

//    public long getProjectIdForModel(long modelId) {
//    	return dslContext
//				.select(MODEL.PROJECT_ID)
//				.from(MODEL)
//				.where(MODEL.ID.eq(modelId))
//				.fetchOneInto(Long.class);
//	}

    public List<Run> getRunsForExperiment(long experimentId) {
        return dslContext
                .select(RUN.asterisk())
                .from(RUN)
                .where(RUN.EXPERIMENT_ID.eq(experimentId))
                .fetchInto(Run.class);
    }
}
