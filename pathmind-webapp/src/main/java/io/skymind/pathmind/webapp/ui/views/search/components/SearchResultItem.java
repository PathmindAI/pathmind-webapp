package io.skymind.pathmind.webapp.ui.views.search.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

public class SearchResultItem extends VerticalLayout {
    private SearchResult searchResult;
    private SearchResultItemType searchResultType;
    private Span createdDateComponent;
    private Span lastActivityDateComponent;
    private Div notesComponent;
    private String decodedKeyword;
    private Boolean isArchived;

    public SearchResultItem(SearchResult item, String decodedKeyword) {
        this.searchResult = item;
        this.decodedKeyword = decodedKeyword;
        this.isArchived = searchResult.getIsArchived();
        searchResultType = searchResult.getItemType();
        createdDateComponent = new Span("Created");
        lastActivityDateComponent = new Span(" Last Activity");
        notesComponent = createSearchResultsNotesComponent();
        add(
            WrapperUtils.wrapWidthFullBetweenHorizontal(createdDateComponent, lastActivityDateComponent),
            createNameRow(),
            WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(new Span("Notes: "), notesComponent)
        );
        setSpacing(false);
        setPadding(false);
        setClassName("search-result-item");
    }

    private HorizontalLayout createNameRow() {
        VerticalLayout itemName = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
            highlightSearchResult(searchResult, searchResult.getProjectName())
        );
        if (!searchResultType.equals(SearchResultItemType.PROJECT)) {
            itemName.add(highlightSearchResult(searchResult, "Model #"+searchResult.getModelName()));
        }
        if (searchResultType.equals(SearchResultItemType.EXPERIMENT)) {
            itemName.add(highlightSearchResult(searchResult, "Experiment #"+searchResult.getExperimentName()));
        }
        HorizontalLayout nameRow = new HorizontalLayout();
        nameRow.add(itemName);
        if (isArchived) {
            nameRow.add(LabelFactory.createLabel("Archived", CssMindPathStyles.TAG_LABEL));
        }
        nameRow.addClassName("name-row");
        return nameRow;
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
        searchResultColumn.addClassName("highlighted-text-wrapper");
        return searchResultColumn;
    }
	
	@Override
	protected void onAttach(AttachEvent evt) {
		VaadinDateAndTimeUtils.withUserTimeZoneId(evt.getUI(), timeZoneId -> {
			createdDateComponent.setText("Created: "+DateAndTimeUtils.formatDateAndTimeShortFormatter(searchResult.getCreateDate(), timeZoneId));
			lastActivityDateComponent.setText("Last Activity: "+DateAndTimeUtils.formatDateAndTimeShortFormatter(searchResult.getUpdateDate(), timeZoneId));
		});
	}
}