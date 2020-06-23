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

    public List<SearchResult> search(long userId, String keyword){
        return SearchRepository.search(ctx, userId, keyword);
    }
    
    public int count(long userId, String keyword){
        return SearchRepository.count(ctx, userId, keyword);
    }
}
