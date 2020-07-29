package io.skymind.pathmind.db.dao;

import java.util.*;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;

@Repository
public class SearchDAO {
    
    private final DSLContext ctx;
    SearchDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<SearchResult> findSearchResults(long userId, String keyword, int offset, int limit) {
        ProcessKeywordResult searchDescription = processKeyword(keyword);
        return SearchRepository.findSearchResults(ctx, searchDescription.resultTypes,
                userId, searchDescription.keyword, offset, limit);
    }
    
    public int countSearchResults(long userId, String keyword) {
        ProcessKeywordResult searchDescription = processKeyword(keyword);
        return SearchRepository.countSearchResults(ctx, searchDescription.resultTypes, userId, searchDescription.keyword);
    }

    private ProcessKeywordResult processKeyword(String originalKeyword) {
        String[] splitTypeAndKeyword = originalKeyword.split(":", 2);
        String searchType = splitTypeAndKeyword[0].toLowerCase();
        String searchKeyword = splitTypeAndKeyword[1].toLowerCase();

        ProcessKeywordResult result = new ProcessKeywordResult();
        result.resultTypes = getResultTypes(searchType);
        result.keyword = searchKeyword;
        return result;
    }

    private Collection<SearchResultItemType> getResultTypes(String searchType) {
        String projectName = SearchResultItemType.PROJECT.getName().toLowerCase();
        String modelName = SearchResultItemType.MODEL.getName().toLowerCase();
        String experimentName = SearchResultItemType.EXPERIMENT.getName().toLowerCase();
        if (searchType.equals(projectName)) {
            return Collections.singleton(SearchResultItemType.PROJECT);
        } else if (searchType.equals(modelName)) {
            return Collections.singleton(SearchResultItemType.MODEL);
        } else if (searchType.equals(experimentName)) {
            return Collections.singleton(SearchResultItemType.EXPERIMENT);
        } else {
            return Arrays.asList(SearchResultItemType.PROJECT, SearchResultItemType.MODEL, SearchResultItemType.EXPERIMENT);
        }
    }

    private static class ProcessKeywordResult {
        private Collection<SearchResultItemType> resultTypes;
        private String keyword;
    }
}
