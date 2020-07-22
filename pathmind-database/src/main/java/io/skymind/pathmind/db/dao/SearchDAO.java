package io.skymind.pathmind.db.dao;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        ProcessKeywordResult processedKeyword = processKeyword(keyword.toLowerCase());
        List<SearchResult> originalSearchResults = SearchRepository.findSearchResults(ctx, processedKeyword.itemsTypes,
                userId, processedKeyword.keyword, offset, limit);
        return originalSearchResults;
    }
    
    public int countSearchResults(long userId, String keyword){
        ProcessKeywordResult processedKeyword = processKeyword(keyword.toLowerCase());
        return SearchRepository.countSearchResults(ctx, processedKeyword.itemsTypes, userId, processedKeyword.keyword);
    }

    private ProcessKeywordResult processKeyword(String originalKeyword) {

        String modelName = SearchResultItemType.MODEL.getName().toLowerCase();
        String experimentName = SearchResultItemType.EXPERIMENT.getName().toLowerCase();

        if (findTypeInKeyword(modelName, originalKeyword) && !modelName.equals(originalKeyword)) {
            ProcessKeywordResult result = new ProcessKeywordResult();
            result.itemsTypes = Collections.singleton(SearchResultItemType.MODEL);
            result.keyword = originalKeyword.replaceFirst(modelName + " #", "")
                    .replaceFirst(modelName + " ", "");
            return result;
        } else if (findTypeInKeyword(SearchResultItemType.EXPERIMENT.getName(), originalKeyword) && !experimentName.equals(originalKeyword)) {
            ProcessKeywordResult result = new ProcessKeywordResult();
            result.itemsTypes = Collections.singleton(SearchResultItemType.EXPERIMENT);
            result.keyword = originalKeyword.replaceFirst(experimentName + " #", "")
                    .replaceFirst(experimentName + " ", "");
            return result;
        }
        else {
            ProcessKeywordResult result = new ProcessKeywordResult();
            result.itemsTypes = new HashSet<>(Arrays.asList(SearchResultItemType.values()));
            result.keyword = originalKeyword;
            return result;
        }
    }

    private Boolean findTypeInKeyword(String typeName, String keyword) {
        return Pattern.compile(Pattern.quote(typeName), Pattern.CASE_INSENSITIVE).matcher(keyword).find();
    }

    private static class ProcessKeywordResult {
        private Collection<SearchResultItemType> itemsTypes;
        private String keyword;
    }
}
