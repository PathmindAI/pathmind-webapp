package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.security.SecurityUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.Project.PROJECT;

@Repository
public class ProjectRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Project> getProjectsForUser(int userId) {
        return dslContext
            .selectFrom(PROJECT)
            .where(PROJECT.USER_ID.eq(userId))
            .fetchInto(Project.class);
    }

    // Convenience method so we don't always need to pass in the userId
    public List<Project> getProjectsForUser() {
        return dslContext
            .selectFrom(PROJECT)
            .where(PROJECT.USER_ID.eq(SecurityUtils.getUser().getId()))
			.fetchInto(Project.class);
    }

    public Project getProject(int projectId) {
    	return dslContext
			.selectFrom(PROJECT)
			.where(PROJECT.ID.eq(projectId))
			.fetchOneInto(Project.class);
	}
}
