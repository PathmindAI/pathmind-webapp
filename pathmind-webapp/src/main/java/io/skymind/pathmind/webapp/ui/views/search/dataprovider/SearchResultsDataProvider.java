package io.skymind.pathmind.webapp.ui.views.search.dataprovider;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.skymind.pathmind.db.dao.SearchDAO;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class SearchResultsDataProvider extends AbstractBackEndDataProvider<SearchResult, String> {

    @Autowired
    private SearchDAO dao;

    @Override
    protected Stream<SearchResult> fetchFromBackEnd(Query<SearchResult, String> query) {
        if (query.getFilter().isPresent()) {
            return dao.findSearchResults(SecurityUtils.getUserId(), query.getFilter().get(), query.getOffset(), query.getLimit()).stream();
        } else {
            return Stream.empty();
        }
    }

    @Override
    protected int sizeInBackEnd(Query<SearchResult, String> query) {
        if (query.getFilter().isPresent()) {
            return dao.countSearchResults(SecurityUtils.getUserId(), query.getFilter().get());
        } else {
            return 0;
        }
    }

}
