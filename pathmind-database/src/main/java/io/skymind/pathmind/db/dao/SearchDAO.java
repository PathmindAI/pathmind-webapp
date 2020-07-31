package io.skymind.pathmind.db.dao;

import java.util.*;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.data.SearchResult;

@Repository
public class SearchDAO {
    
    private final DSLContext ctx;
    SearchDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<SearchResult> findSearchResults(long userId, String keyword, int offset, int limit) {
        SearchDescription searchDescription = processKeyword(keyword);
        return SearchRepository.findSearchResults(ctx, searchDescription,
                userId, offset, limit);
    }
    
    public int countSearchResults(long userId, String keyword) {
        SearchDescription searchDescription = processKeyword(keyword);
        return SearchRepository.countSearchResults(ctx, searchDescription, userId);
    }

    private SearchDescription processKeyword(String originalKeyword) {
        SearchParser parser = new SearchParser();
        return parser.parseKeyword(originalKeyword);
    }
}
