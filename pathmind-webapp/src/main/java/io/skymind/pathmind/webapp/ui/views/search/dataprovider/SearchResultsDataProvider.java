package io.skymind.pathmind.webapp.ui.views.search.dataprovider;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.skymind.pathmind.db.dao.SearchDAO;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.security.SecurityUtils;

@UIScope
@SpringComponent
public class SearchResultsDataProvider extends AbstractBackEndDataProvider<SearchResult, String> {

    @Autowired
    private SearchDAO dao;
    
    @Override
    protected Stream<SearchResult> fetchFromBackEnd(Query<SearchResult, String> query) {
        if (query.getFilter().isPresent()) {
            return dao.search(SecurityUtils.getUserId(), query.getFilter().get()).stream();
        } else {
            return Stream.empty();
        }
    }

    @Override
    protected int sizeInBackEnd(Query<SearchResult, String> query) {
        if (query.getFilter().isPresent()) {
            return dao.count(SecurityUtils.getUserId(), query.getFilter().get());
        } else {
            return 0;
        }
    }


}
