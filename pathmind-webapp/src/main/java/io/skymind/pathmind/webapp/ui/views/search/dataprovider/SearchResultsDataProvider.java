package io.skymind.pathmind.webapp.ui.views.search.dataprovider;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
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

    private Comparator<SearchResult> sortOrder = null;

    @Autowired
    private SearchDAO dao;
    
    @Override
    protected Stream<SearchResult> fetchFromBackEnd(Query<SearchResult, String> query) {
        if (query.getFilter().isPresent()) {
            Stream<SearchResult> stream = dao.findSearchResults(SecurityUtils.getUserId(), query.getFilter().get(), query.getOffset(), query.getLimit())
                        .stream();

            Optional<Comparator<SearchResult>> comparing = Stream
                    .of(query.getInMemorySorting(), sortOrder)
                    .filter(Objects::nonNull)
                    .reduce((c1, c2) -> c1.thenComparing(c2));
    
            if (comparing.isPresent()) {
                stream = stream.sorted(comparing.get());
            }
            return stream;
        } else {
            return Stream.empty();
        }
    }

    public Comparator<SearchResult> getSortComparator() {
        return sortOrder;
    }

    public void setSortComparator(Comparator<SearchResult> comparator) {
        this.sortOrder = comparator;
        refreshAll();
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
