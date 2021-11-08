package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.db.jooq.tables.records.ProjectRecord;
import io.skymind.pathmind.shared.data.Project;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.SortOrder;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateWhereStep;

import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.Tables.PROJECT;

class ProjectRepository {

    protected static List<Project> getFilteredProjectsForUser(DSLContext ctx, long userId, boolean isArchived, int offset, int limit, String sortBy, boolean isDesc) {
        Field<Integer> modelCountField = ctx.selectCount()
                .from(MODEL)
                .where(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .asField("modelCount");

        OrderField<?> orderField = PROJECT.LAST_ACTIVITY_DATE.sort(SortOrder.DESC);
        if (!sortBy.isEmpty()) {
            SortOrder fieldSortOrder = isDesc ? SortOrder.DESC : SortOrder.ASC;
            switch (sortBy.toUpperCase()) {
                case "NAME":
                    orderField = PROJECT.NAME.sort(fieldSortOrder);
                    break;
                case "MODELS":
                    orderField = modelCountField.sort(fieldSortOrder);
                    break;
                case "DATE_CREATED":
                    orderField = PROJECT.DATE_CREATED.sort(fieldSortOrder);
                    break;
                case "LAST_ACTIVITY_DATE":
                    orderField = PROJECT.LAST_ACTIVITY_DATE.sort(fieldSortOrder);
                    break;
                default:
                    orderField = PROJECT.LAST_ACTIVITY_DATE.sort(SortOrder.DESC);
            }
        }

        return ctx.select(PROJECT.asterisk(), modelCountField)
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
                .and(PROJECT.ARCHIVED.eq(isArchived))
                .orderBy(orderField)
                .offset(offset)
                .limit(limit)
                .fetchInto(Project.class);
    }

    protected static List<Project> getProjectsForUser(DSLContext ctx, long userId) {
        return ctx
                .selectFrom(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
                .fetchInto(Project.class);
    }

    protected static Optional<Project> getProjectIfAllowed(DSLContext ctx, long projectId, long userId) {
        return Optional.ofNullable(ctx
                .selectFrom(PROJECT)
                .where(PROJECT.ID.eq(projectId))
                .and(PROJECT.PATHMIND_USER_ID.eq(userId))
                .fetchOneInto(Project.class)
        );
    }

    protected static Integer getFilteredProjectCount(DSLContext ctx, long userId, boolean isArchived) {
        return ctx.selectCount()
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
                .and(PROJECT.ARCHIVED.eq(isArchived))
                .fetchOne(0, Integer.class);
    }

    protected static Integer getProjectCount(DSLContext ctx, long userId) {
        return ctx.selectCount()
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
                .fetchOne(0, Integer.class);
    }


    protected static long insertProject(DSLContext ctx, Project project, LocalDateTime dateCreated) {
        final ProjectRecord savedProject = PROJECT.newRecord();
        savedProject.attach(ctx.configuration());
        savedProject.setName(project.getName());
        savedProject.setDateCreated(dateCreated);
        savedProject.setLastActivityDate(savedProject.getDateCreated());
        savedProject.setPathmindUserId(project.getPathmindUserId());
        savedProject.store();
        return savedProject.key().get(PROJECT.ID);
    }


    protected static void update(DSLContext ctx, ProjectUpdateRequest updateRequest) {
        UpdateSetFirstStep update =  ctx.update(PROJECT);
        LocalDateTime activityDate = Optional.ofNullable((LocalDateTime)updateRequest.updates.get(PROJECT.LAST_ACTIVITY_DATE)).orElse(LocalDateTime.now());
        updateRequest.updates.put(PROJECT.LAST_ACTIVITY_DATE, activityDate);
        updateRequest.updates.forEach((f,v) -> update.set(f, v));
        UpdateConditionStep<?> command = ((UpdateWhereStep<?>)update).where(PROJECT.ID.eq(updateRequest.projectId));
        command.execute();
    }

}
