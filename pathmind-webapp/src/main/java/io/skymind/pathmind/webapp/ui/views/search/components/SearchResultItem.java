package io.skymind.pathmind.webapp.ui.views.search.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.shared.utils.PathmindStringUtils;
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
        lastActivityDateComponent = new Span("Last Activity");
        notesComponent = createSearchResultsNotesComponent();
        add(
            createInfoRow(),
            createNameRow(),
            WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(new Span("Notes: "), notesComponent)
        );
        setSpacing(false);
        setPadding(false);
        setClassName("search-result-item");
    }

    private HorizontalLayout createInfoRow() {
        HorizontalLayout infoRow = WrapperUtils.wrapWidthFullBetweenHorizontal();
        HorizontalLayout tags = new HorizontalLayout(LabelFactory.createLabel(searchResultType.getName(), CssMindPathStyles.TAG_LABEL, CssMindPathStyles.TAG_OUTLINE_LABEL));
        HorizontalLayout dates = new HorizontalLayout(createdDateComponent, lastActivityDateComponent);
        
        if (isArchived) {
            tags.add(LabelFactory.createLabel("Archived", CssMindPathStyles.TAG_LABEL));
        }
        infoRow.add(tags, dates);
        infoRow.addClassName("info-row");
        return infoRow;
    }

    private VerticalLayout createNameRow() {
        Boolean resultTypeModel = searchResultType.equals(SearchResultItemType.MODEL);
        Boolean resultTypeExperiment = searchResultType.equals(SearchResultItemType.EXPERIMENT);
        Div projectName = highlightSearchResult(searchResult.getProjectName());
        VerticalLayout nameRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(projectName);
        if (resultTypeModel || resultTypeExperiment) {
            nameRow.add(highlightSearchResult("Model #"+searchResult.getModelName()));
        }
        if (resultTypeExperiment) {
            nameRow.add(highlightSearchResult("Experiment #"+searchResult.getExperimentName()));
        }
        nameRow.addClassName("name-row");
        return nameRow;
    }

    private Div createSearchResultsNotesComponent() {
        String notes = searchResult.getNotes();
        if (notes.isEmpty()) {
            return new Div(new Span("—"));
        } else {
            Div notesColumn = highlightSearchResult(notes);
            notesColumn.addClassName("grid-notes-column");
            return notesColumn;
        }
    }

    private Div highlightSearchResult(String columnText) {
        Div searchResultColumn = new Div();
        String escapedKeyword = PathmindStringUtils.escapeNonAlphanumericalCharacters(decodedKeyword);
        String[] parts = columnText.split("(?i)((?<="+escapedKeyword+")|(?=(?i)"+escapedKeyword+"))");

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].toLowerCase().equals(decodedKeyword.toLowerCase())) {
                searchResultColumn.add(
                    LabelFactory.createLabel(parts[i], CssMindPathStyles.HIGHLIGHT_LABEL)
                );
            } else {
                searchResultColumn.add(parts[i]);
            }
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