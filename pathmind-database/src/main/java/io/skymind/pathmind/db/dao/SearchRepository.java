package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record4;

import io.skymind.pathmind.shared.data.SearchResult;

public class SearchRepository {

    protected static List<SearchResult> search(DSLContext ctx, long userId, String keyword) {
        return ctx.select(PROJECT.ID, PROJECT.NAME, PROJECT.LAST_ACTIVITY_DATE, PROJECT.USER_NOTES)
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
        .union(ctx.select(MODEL.ID, MODEL.NAME, MODEL.LAST_ACTIVITY_DATE, MODEL.USER_NOTES)
                .from(MODEL)
                .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(userId)))
        .union(ctx.select(EXPERIMENT.ID, EXPERIMENT.NAME, EXPERIMENT.LAST_ACTIVITY_DATE, EXPERIMENT.USER_NOTES)
                .from(EXPERIMENT)
                .innerJoin(MODEL).on(EXPERIMENT.MODEL_ID.eq(PROJECT.ID))
                .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(userId)))
        .fetch(record -> fetchIntoSearchResult(record));
    }
    
    protected static int count(DSLContext ctx, long userId, String keyword) {
        return ctx.selectCount()
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId))
                .union(ctx.selectCount()
                        .from(MODEL)
                        .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                        .where(PROJECT.PATHMIND_USER_ID.eq(userId)))
                .union(ctx.selectCount()
                        .from(EXPERIMENT)
                        .innerJoin(MODEL).on(EXPERIMENT.MODEL_ID.eq(PROJECT.ID))
                        .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                        .where(PROJECT.PATHMIND_USER_ID.eq(userId)))
                .fetchStream().collect(Collectors.summingInt(Record1::value1));
    }

    private static SearchResult fetchIntoSearchResult(Record4<Long, String, LocalDateTime, String> record) {
        return new SearchResult(record.value1(), "Custom", record.value2(), record.value3(), record.value4());
    }
}
