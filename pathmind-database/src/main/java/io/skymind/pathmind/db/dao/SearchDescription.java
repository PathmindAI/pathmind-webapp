package io.skymind.pathmind.db.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
@ToString
public class SearchDescription {
    private final Set<OrClause> projectClauses = new HashSet<>();
    private final Set<OrClause> modelClauses = new HashSet<>();
    private final Set<OrClause> experimentClauses = new HashSet<>();


    public SearchDescription withProjectClauses(Collection<OrClause> orClauses) {
        projectClauses.clear();
        projectClauses.addAll(orClauses);
        return this;
    }

    public SearchDescription withProjectClauses(OrClause... orClauses) {
        return withProjectClauses(Arrays.asList(orClauses));
    }

    public SearchDescription withModelClauses(Collection<OrClause> orClauses) {
        modelClauses.clear();
        modelClauses.addAll(orClauses);
        return this;
    }

    public SearchDescription withModelClauses(OrClause... orClauses) {
        return withModelClauses(Arrays.asList(orClauses));
    }

    public SearchDescription withExperimentClauses(Collection<OrClause> orClauses) {
        experimentClauses.clear();
        experimentClauses.addAll(orClauses);
        return this;
    }

    public SearchDescription withExperimentClauses(OrClause... orClauses) {
        return withExperimentClauses(Arrays.asList(orClauses));
    }

    public boolean isEmpty() {
        return projectClauses.isEmpty() && modelClauses.isEmpty() && experimentClauses.isEmpty();
    }

    public enum Field {
        NAME, USERNOTES
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class OrClause {
        private final Field field;
        private final String keyword;

        public OrClause(Field field, String keyword) {
            this.field = field;
            this.keyword = keyword;
        }
    }
}
