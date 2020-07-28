package io.skymind.pathmind.db.dao;

import java.util.*;
import java.util.regex.Pattern;

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

    public List<SearchResult> findSearchResults(long userId, String keyword, int offset, int limit){
        ProcessKeywordResult processedKeyword = processKeyword(keyword);
        List<SearchResult> originalSearchResults = SearchRepository.findSearchResults(ctx, processedKeyword.itemsTypes,
                userId, processedKeyword.keyword, offset, limit);
        return originalSearchResults;
    }
    
    public int countSearchResults(long userId, String keyword){
        ProcessKeywordResult processedKeyword = processKeyword(keyword);
        return SearchRepository.countSearchResults(ctx, processedKeyword.itemsTypes, userId, processedKeyword.keyword);
    }

    private ProcessKeywordResult processKeyword(String originalKeyword) {

        originalKeyword = originalKeyword.toLowerCase();
        String modelName = SearchResultItemType.MODEL.getName().toLowerCase();
        String experimentName = SearchResultItemType.EXPERIMENT.getName().toLowerCase();

        if (findTypeInKeyword(modelName, originalKeyword) && !modelName.equals(originalKeyword)) {
            return generateKeywordResult(SearchResultItemType.MODEL, originalKeyword, modelName);
        } else if (findTypeInKeyword(SearchResultItemType.EXPERIMENT.getName(), originalKeyword) && !experimentName.equals(originalKeyword)) {
            return generateKeywordResult(SearchResultItemType.EXPERIMENT, originalKeyword, experimentName);
        } else {
            ProcessKeywordResult result = new ProcessKeywordResult();
            result.itemsTypes = new HashSet<>(Arrays.asList(SearchResultItemType.values()));
            result.keyword = originalKeyword;
            return result;
        }
    }

    private ProcessKeywordResult generateKeywordResult(SearchResultItemType searchResultItemType, String originalKeyword, String name) {
        ProcessKeywordResult result = new ProcessKeywordResult();
        result.itemsTypes = Collections.singleton(searchResultItemType);
        result.keyword = originalKeyword.replaceFirst(name + " #", "")
                .replaceFirst(name + " ", "");
        return result;
    }

    private Boolean findTypeInKeyword(String typeName, String keyword) {
        return Pattern.compile(Pattern.quote(typeName), Pattern.CASE_INSENSITIVE).matcher(keyword).find();
    }

    private static class ProcessKeywordResult {
        private Collection<SearchResultItemType> itemsTypes;
        private String keyword;
    }
}
