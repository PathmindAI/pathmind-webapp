package io.skymind.pathmind.db.dao;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.data.SearchResult;

@Repository
public class SearchDAO {
    
    private final DSLContext ctx;

    SearchDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<SearchResult> findSearchResults(long userId, String keyword, int offset, int limit){
        return SearchRepository.findSearchResults(ctx, userId, keyword, offset, limit);
    }
    
    public int countSearchResults(long userId, String keyword){
        return SearchRepository.countSearchResults(ctx, userId, keyword); 
    }
}
