package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record9;
import org.jooq.Table;
import org.jooq.impl.DSL;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;

public class SearchRepository {

    protected static List<SearchResult> findSearchResults(DSLContext ctx, long userId, String keyword, int offset, int limit) {
        Table<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> searchQueryTable = generateSearchQueryTable(ctx, userId, keyword);
        return ctx
                .selectFrom(searchQueryTable)
                .orderBy(searchQueryTable.field("lastActivity").desc())
                .offset(offset)
                .limit(limit)
                .fetch(record -> fetchIntoSearchResult(record));
    }
    
    protected static int countSearchResults(DSLContext ctx, long userId, String keyword) {
        return ctx.selectCount().from(generateSearchQueryTable(ctx, userId, keyword)).fetchOne(DSL.count());
    }
    
    private static Table<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> generateSearchQueryTable(DSLContext ctx, long userId, String keyword){
        String searchValue = "%" + keyword.replace("%", "\\%").replace("_", "\\_") + "%";
        return ctx.select(DSL.inline(SearchResultItemType.PROJECT.getName()).as("itemType"), PROJECT.ID, PROJECT.ARCHIVED, PROJECT.DATE_CREATED, PROJECT.LAST_ACTIVITY_DATE.as("lastActivity"), PROJECT.USER_NOTES, PROJECT.NAME.as("projectName"), DSL.inline("").as("modelName"), DSL.inline("").as("experimentName"))
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(userId).and(PROJECT.NAME.likeIgnoreCase(searchValue).or(PROJECT.USER_NOTES.likeIgnoreCase(searchValue))))
        .union(ctx.select(DSL.inline(SearchResultItemType.MODEL.getName()).as("itemType"), MODEL.ID, MODEL.ARCHIVED, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE.as("lastActivity"), MODEL.USER_NOTES, PROJECT.NAME.as("projectName"), MODEL.NAME.as("modelName"), DSL.inline("").as("experimentName"))
                .from(MODEL)
                .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(userId)).and(MODEL.NAME.equal(keyword).or(MODEL.USER_NOTES.likeIgnoreCase(searchValue))))
        .union(ctx.select(DSL.inline(SearchResultItemType.EXPERIMENT.getName()).as("itemType"), EXPERIMENT.ID, EXPERIMENT.ARCHIVED, EXPERIMENT.DATE_CREATED, EXPERIMENT.LAST_ACTIVITY_DATE.as("lastActivity"), EXPERIMENT.USER_NOTES, PROJECT.NAME.as("projectName"), MODEL.NAME.as("modelName"), EXPERIMENT.NAME.as("experimentName"))
                .from(EXPERIMENT)
                .innerJoin(MODEL).on(EXPERIMENT.MODEL_ID.eq(MODEL.ID))
                .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(userId).and(EXPERIMENT.NAME.equal(keyword).or(EXPERIMENT.USER_NOTES.likeIgnoreCase(searchValue)))))
                .asTable();
    }

    private static SearchResult fetchIntoSearchResult(Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String> record) {
        return new SearchResult(SearchResultItemType.getEnumFromName(record.value1()), record.value2(), record.value3(), record.value4(), record.value5(), record.value6(), record.value7(), record.value8(), record.value9());
    }
}
