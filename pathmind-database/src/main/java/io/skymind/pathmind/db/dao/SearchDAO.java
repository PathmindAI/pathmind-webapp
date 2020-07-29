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

    public List<SearchResult> findSearchResults(long userId, String keyword, int offset, int limit) {
        List<SearchResult> searchResults = new ArrayList<>();
        List<ProcessKeywordResult> processedKeywords = processKeyword(keyword);
        processedKeywords.stream().forEach(processedKeyword -> {
            final List<SearchResult> keywordSearchResults = SearchRepository.findSearchResults(ctx, processedKeyword.itemsTypes,
                    userId, processedKeyword.keyword, offset, limit);
            searchResults.addAll(keywordSearchResults);
        });
        Collections.sort(searchResults, Comparator.comparing(SearchResult::getUpdateDate));
        return searchResults;
    }
    
    public int countSearchResults(long userId, String keyword) {
        List<ProcessKeywordResult> processedKeywords = processKeyword(keyword);
        List<Integer> count = new ArrayList<>();
        processedKeywords.stream().forEach(processedKeyword -> {
            int partialCount = SearchRepository.countSearchResults(ctx, processedKeyword.itemsTypes, userId, processedKeyword.keyword);
            count.add(partialCount);
        });
        return count.stream().reduce(0, Integer::sum);
    }

    private List<ProcessKeywordResult> processKeyword(String originalKeyword) {
        String projectName = SearchResultItemType.PROJECT.getName().toLowerCase();
        String modelName = SearchResultItemType.MODEL.getName().toLowerCase();
        String experimentName = SearchResultItemType.EXPERIMENT.getName().toLowerCase();
        String[] splitTypeAndKeyword = originalKeyword.split(":", 2);
        String searchType = splitTypeAndKeyword[0].toLowerCase();
        originalKeyword = splitTypeAndKeyword[1];
        originalKeyword = originalKeyword.toLowerCase();

        if (searchType.equals(projectName)) {
            return generateKeywordResult(SearchResultItemType.PROJECT, originalKeyword, projectName);
        } else if (searchType.equals(modelName)) {
            return generateKeywordResult(SearchResultItemType.MODEL, originalKeyword, modelName);
        } else if (searchType.equals(experimentName)) {
            return generateKeywordResult(SearchResultItemType.EXPERIMENT, originalKeyword, experimentName);
        } else {
            List<ProcessKeywordResult> resultList = new ArrayList<>();
            ProcessKeywordResult result = new ProcessKeywordResult();
            HashSet originalKeywordTypes = new HashSet<>(Arrays.asList(SearchResultItemType.PROJECT));
            
            if (matchTypeInKeyword(modelName, originalKeyword)) {
                resultList.addAll(generateKeywordResult(SearchResultItemType.MODEL, originalKeyword, modelName));
            } else {
                originalKeywordTypes.add(SearchResultItemType.MODEL);
            }
            if (matchTypeInKeyword(experimentName, originalKeyword)) {
                resultList.addAll(generateKeywordResult(SearchResultItemType.EXPERIMENT, originalKeyword, experimentName));
            } else {
                originalKeywordTypes.add(SearchResultItemType.EXPERIMENT);
            }
            result.itemsTypes = originalKeywordTypes;
            result.keyword = originalKeyword;
            resultList.add(result);
            return resultList;
        }
    }

    private List<ProcessKeywordResult> generateKeywordResult(SearchResultItemType searchResultItemType, String originalKeyword, String name) {
        ProcessKeywordResult result = new ProcessKeywordResult();
        result.itemsTypes = Collections.singleton(searchResultItemType);
        result.keyword = replaceTypeWord(originalKeyword, name);
        return new ArrayList<>(Arrays.asList(result));
    }

    private String replaceTypeWord(String originalKeyword, String name) {
        return originalKeyword.replaceFirst(name + " #", "").replaceFirst(name + " ", "");
    }

    private Boolean matchTypeInKeyword(String typeName, String keyword) {
        return keyword.matches("(?i)"+typeName+"\\s#?\\d+");
    }

    private static class ProcessKeywordResult {
        private Collection<SearchResultItemType> itemsTypes;
        private String keyword;
    }
}
