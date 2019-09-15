package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Model;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.Tables.MODEL;

@Repository
public class ModelRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Model> getModelsForProject(long projectId) {
        return dslContext
				.selectFrom(MODEL)
				.where(MODEL.PROJECT_ID.eq(projectId))
				.fetchInto(Model.class);
    }
}
