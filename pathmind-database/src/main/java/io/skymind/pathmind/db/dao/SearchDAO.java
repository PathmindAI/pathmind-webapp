package io.skymind.pathmind.db.dao;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;

@Repository
public class SearchDAO {
    
    private final DSLContext ctx;
    private SearchResultItemType targetType = null;

    SearchDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<SearchResult> findSearchResults(long userId, String keyword, int offset, int limit){
        // String processedKeyword = processKeyword(keyword.toLowerCase());
        List<SearchResult> originalSearchResults = SearchRepository.findSearchResults(ctx, userId, keyword, offset, limit);
        // List<SearchResult> originalSearchResults = SearchRepository.findSearchResults(ctx, userId, processedKeyword, offset, limit);
        // if (targetType != null) {
        //     return originalSearchResults
        //             .stream()
        //             .filter(searchResult -> searchResult.getItemType().equals(targetType))
        //             .collect(Collectors.toList());
        // }
        return originalSearchResults;
    }
    
    public int countSearchResults(long userId, String keyword){
        // String processedKeyword = processKeyword(keyword.toLowerCase());
        // if (targetType != null) {
        //     return 0; // TODO: count search results with targetType as itemType and keyword in the
        // }
        return SearchRepository.countSearchResults(ctx, userId, keyword); 
    }

    private String processKeyword(String originalKeyword) {
        String keyword = originalKeyword;
        String modelName = SearchResultItemType.MODEL.getName().toLowerCase();
        String experimentName = SearchResultItemType.EXPERIMENT.getName().toLowerCase();

        if (findTypeInKeyword(modelName, keyword) && !modelName.equals(keyword)) {
            targetType = SearchResultItemType.MODEL;
            keyword = keyword.replaceFirst(modelName+" #", "");
            keyword = keyword.replaceFirst(modelName+" ", "");
        } else if (findTypeInKeyword(SearchResultItemType.EXPERIMENT.getName(), keyword) && !experimentName.equals(keyword)) {
            targetType = SearchResultItemType.EXPERIMENT;
            keyword = keyword.replaceFirst(experimentName+" #", "");
            keyword = keyword.replaceFirst(experimentName+" ", "");
        }

        return keyword;
    }

    private Boolean findTypeInKeyword(String typeName, String keyword) {
        return Pattern.compile(Pattern.quote(typeName), Pattern.CASE_INSENSITIVE).matcher(keyword).find();
    }
}
