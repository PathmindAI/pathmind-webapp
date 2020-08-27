package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import io.skymind.pathmind.db.jooq.tables.Model;
import org.jooq.*;
import org.jooq.impl.DSL;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;

public class SearchRepository {

    protected static List<SearchResult> findSearchResults(DSLContext ctx, SearchDescription searchDescription, long userId,
                                                          int offset, int limit) {
        assert !searchDescription.isEmpty();
        Table<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> searchQueryTable = generateSearchQueryTable(ctx, searchDescription, userId);
        return ctx
                .selectFrom(searchQueryTable)
                .orderBy(searchQueryTable.field("lastActivity").desc())
                .offset(offset)
                .limit(limit)
                .fetch(record -> fetchIntoSearchResult(record));
    }
    
    protected static int countSearchResults(DSLContext ctx, SearchDescription searchDescription, long userId) {
        assert !searchDescription.isEmpty();
        return ctx.selectCount().from(generateSearchQueryTable(ctx, searchDescription, userId)).fetchOne(DSL.count());
    }
    
    private static Table<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> generateSearchQueryTable(DSLContext ctx, SearchDescription searchDescription, long userId){
        assert !searchDescription.isEmpty();

        List<SelectOptionStep<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>>> queries = new ArrayList<>();

        if (!searchDescription.getProjectClauses().isEmpty()) {
            Condition projectCondition = generateProjectCondition(searchDescription.getProjectClauses());
            SelectConditionStep<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> projectsQuery = ctx.select(DSL.inline(SearchResultItemType.PROJECT.getName()).as("itemType"), PROJECT.ID, PROJECT.ARCHIVED, PROJECT.DATE_CREATED, PROJECT.LAST_ACTIVITY_DATE.as("lastActivity"), PROJECT.USER_NOTES, PROJECT.NAME.as("projectName"), DSL.inline("").as("modelName"), DSL.inline("").as("experimentName"))
                    .from(PROJECT)
                    .where(PROJECT.PATHMIND_USER_ID.eq(userId).and(projectCondition));
            queries.add(projectsQuery);
        }
        if (!searchDescription.getModelClauses().isEmpty()) {
            Condition modelCondition = generateModelCondition(searchDescription.getModelClauses());
            SelectConditionStep<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> modelsQuery = ctx.select(DSL.inline(SearchResultItemType.MODEL.getName()).as("itemType"), MODEL.ID, MODEL.ARCHIVED, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE.as("lastActivity"), MODEL.USER_NOTES, PROJECT.NAME.as("projectName"), MODEL.NAME.as("modelName"), DSL.inline("").as("experimentName"))
                    .from(MODEL)
                    .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                    .where(PROJECT.PATHMIND_USER_ID.eq(userId).and(modelCondition));
            queries.add(modelsQuery);
        }
        if (!searchDescription.getExperimentClauses().isEmpty()) {
            Condition experimentCondition = generateExperimentCondition(searchDescription.getExperimentClauses());
            SelectConditionStep<Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String>> experimentsQuery = ctx.select(DSL.inline(SearchResultItemType.EXPERIMENT.getName()).as("itemType"), EXPERIMENT.ID, EXPERIMENT.ARCHIVED, EXPERIMENT.DATE_CREATED, EXPERIMENT.LAST_ACTIVITY_DATE.as("lastActivity"), EXPERIMENT.USER_NOTES, PROJECT.NAME.as("projectName"), MODEL.NAME.as("modelName"), EXPERIMENT.NAME.as("experimentName"))
                    .from(EXPERIMENT)
                    .innerJoin(MODEL).on(EXPERIMENT.MODEL_ID.eq(MODEL.ID))
                    .innerJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                    .where(PROJECT.PATHMIND_USER_ID.eq(userId).and(experimentCondition));
            queries.add(experimentsQuery);
        }

        return queries.stream().reduce(SelectUnionStep::union).orElseThrow(() -> new RuntimeException("I can't happen")).asTable();
    }

    private static Condition generateModelCondition(Set<SearchDescription.OrClause> modelClauses) {
        return modelClauses.stream()
                .map(orClause -> {
                    switch (orClause.getField()){
                        case NAME: return MODEL.NAME.equal(orClause.getKeyword());
                        case USERNOTES: return MODEL.USER_NOTES.likeIgnoreCase(toLike(orClause.getKeyword()));
                        default:
                            throw new RuntimeException("I can't happen");
                    }
                })
                .reduce(Condition::or).orElseThrow(() -> new RuntimeException("I can't happen"));
    }

    private static Condition generateExperimentCondition(Set<SearchDescription.OrClause> experimentClauses) {
        return experimentClauses.stream()
                .map(orClause -> {
                    switch (orClause.getField()){
                        case NAME: return EXPERIMENT.NAME.equal(orClause.getKeyword());
                        case USERNOTES: return EXPERIMENT.USER_NOTES.likeIgnoreCase(toLike(orClause.getKeyword()));
                        default:
                            throw new RuntimeException("I can't happen");
                    }
                })
                .reduce(Condition::or).orElseThrow(() -> new RuntimeException("I can't happen"));
    }

    private static Condition generateProjectCondition(Set<SearchDescription.OrClause> projectClauses) {
        return projectClauses.stream()
                .<Condition>map(orClause -> {
                    switch (orClause.getField()){
                        case NAME:
                            return PROJECT.NAME.likeIgnoreCase(toLike(orClause.getKeyword()));
                        case USERNOTES: return PROJECT.USER_NOTES.likeIgnoreCase(toLike(orClause.getKeyword()));
                        default:
                            throw new RuntimeException("I can't happen");
                    }
                })
                .reduce(Condition::or).orElseThrow(() -> new RuntimeException("I can't happen"));
    }

    private static String toLike(String keyword) {
        return "%" + keyword.replace("%", "\\%").replace("_", "\\_") + "%";
    }

    private static SearchResult fetchIntoSearchResult(Record9<String, Long, Boolean, LocalDateTime, LocalDateTime, String, String, String, String> record) {
        return new SearchResult(SearchResultItemType.getEnumFromName(record.value1()), record.value2(), record.value3(), record.value4(), record.value5(), record.value6(), record.value7(), record.value8(), record.value9());
    }
}
