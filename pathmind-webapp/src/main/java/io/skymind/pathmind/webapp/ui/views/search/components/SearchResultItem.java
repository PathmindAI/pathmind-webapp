package io.skymind.pathmind.webapp.ui.views.search.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;

public class SearchResultItem extends HorizontalLayout {
    private SearchResult searchResult;
    private SearchResultItemType searchResultType;
    private String searchResultName;
    private String createdDate;
    private Div searchResultNotesComponent;
    private String decodedKeyword;

    public SearchResultItem(SearchResult item, String decodedKeyword) {
        this.searchResult = item;
        this.decodedKeyword = decodedKeyword;
        searchResultType = searchResult.getItemType();
        searchResultName = searchResult.getName();
        // createdDate = new LocalDateTimeRenderer<>(SearchResult::getCreateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_FOMATTER);

        searchResultNotesComponent = createSearchResultsNotesComponent();
        add(
            new Span("Type: "),
            new Span("Name: "),
            new Span("Notes: ")
        );
        setClassName("search-result-item");
    }

    private Div createSearchResultsNotesComponent() {
        String notes = searchResult.getNotes();
        if (notes.isEmpty()) {
            return new Div(new Span("—"));
        } else {
            Div notesColumn = highlightSearchResult(searchResult, notes);
            notesColumn.addClassName("grid-notes-column");
            return notesColumn;
        }
    }

    private Div highlightSearchResult(SearchResult searchResult, String columnText) {
        Div searchResultColumn = new Div();
        Boolean isArchived = searchResult.getIsArchived();
        String[] parts = columnText.split(decodedKeyword);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                searchResultColumn.add(
                    LabelFactory.createLabel(decodedKeyword, CssMindPathStyles.HIGHLIGHT_LABEL)
                );
            }
            searchResultColumn.add(parts[i]);
        }
        if (parts.length == 0) {
            searchResultColumn.add(
                LabelFactory.createLabel(decodedKeyword, CssMindPathStyles.HIGHLIGHT_LABEL)
            );
        }
        if (isArchived) {
            searchResultColumn.add(LabelFactory.createLabel("Archived", CssMindPathStyles.TAG_LABEL));
        }
        searchResultColumn.addClassName("highlighted-text-wrapper");
        return searchResultColumn;
    }
}