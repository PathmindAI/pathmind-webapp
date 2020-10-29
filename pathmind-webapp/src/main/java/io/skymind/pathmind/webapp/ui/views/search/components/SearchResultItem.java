package io.skymind.pathmind.webapp.ui.views.search.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.shared.utils.PathmindStringUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

public class SearchResultItem extends VerticalLayout {
    private SearchResult searchResult;
    private SearchResultItemType searchResultType;
    private Span createdDateComponent;
    private Span lastActivityDateComponent;
    private Div notesComponent;
    private String decodedKeyword;
    private Boolean isArchived;
    private ExperimentDAO experimentDAO;

    public SearchResultItem(ExperimentDAO experimentDAO, SearchResult item, String decodedKeyword) {
        this.searchResult = item;
        this.decodedKeyword = decodedKeyword;
        this.isArchived = searchResult.getIsArchived();
        this.experimentDAO = experimentDAO;
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
        HorizontalLayout tags = new HorizontalLayout();
        HorizontalLayout dates = new HorizontalLayout(createdDateComponent, lastActivityDateComponent);

        if (searchResultType.equals(SearchResultItemType.EXPERIMENT)) {
            Experiment experiment = experimentDAO.getExperiment(searchResult.getItemId()).orElse(null);
            if (experiment != null) {
                boolean isFavorite = ExperimentUtils.isFavorite(experiment);
                tags.add(new FavoriteStar(isFavorite, newIsFavorite -> 
                        ExperimentUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite)));
            }
        }
        tags.add(new TagLabel(searchResultType.getName(), true, "small"));
        if (isArchived) {
            tags.add(new TagLabel("Archived", false, "small"));
        }
        infoRow.add(tags, dates);
        infoRow.addClassName("info-row");
        return infoRow;
    }

    private VerticalLayout createNameRow() {
        Boolean resultTypeProject = searchResultType.equals(SearchResultItemType.PROJECT);
        Boolean resultTypeModel = searchResultType.equals(SearchResultItemType.MODEL);
        Boolean resultTypeExperiment = searchResultType.equals(SearchResultItemType.EXPERIMENT);
        VerticalLayout nameRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        String modelName = searchResult.getModelName();
        String experimentName = searchResult.getExperimentName();
        String modelNameText = "Model #"+searchResult.getModelName();
        String experimentNameText = "Experiment #"+searchResult.getExperimentName();
        RouterLink projectNameLink = new RouterLink();
        nameRow.add(highlightSearchResult(projectNameLink, searchResult.getProjectName(), null, resultTypeProject));
        if (resultTypeProject) {
            setRouterTarget(projectNameLink);
        }
        if (resultTypeModel || resultTypeExperiment) {
            RouterLink modelNameLink = new RouterLink();
            nameRow.add(highlightSearchResult(modelNameLink, modelNameText, "(?i)Model\\s#?"+modelName,
                    resultTypeModel && matchedDecodedKeyword(decodedKeyword, "Model", modelName)));
            if (resultTypeModel) {
                setRouterTarget(modelNameLink);
            }
        }
        if (resultTypeExperiment) {
            RouterLink experimentNameLink = new RouterLink();
            nameRow.add(highlightSearchResult(experimentNameLink, experimentNameText, "(?i)Experiment\\s#?"+experimentName,
                    resultTypeExperiment && matchedDecodedKeyword(decodedKeyword, "Experiment", experimentName)));
            setRouterTarget(experimentNameLink);
        }
        nameRow.addClassName("name-row");
        return nameRow;
    }

    private void setRouterTarget(RouterLink link) {
        switch (searchResult.getItemType().getName()) {
            case "Project" :
                link.setRoute(ProjectView.class, searchResult.getItemId());
                break;
            case "Model" :
                link.setRoute(ModelView.class, searchResult.getItemId());
                break;
            case "Experiment" :
                Experiment experiment = experimentDAO.getExperimentWithRuns(searchResult.getItemId()).get();
                if (ExperimentUtils.isDraftRunType(experiment)) {
                    link.setRoute(NewExperimentView.class, searchResult.getItemId());
                } else {
                    link.setRoute(ExperimentView.class, searchResult.getItemId());
                }
                break;
        }
    }

    private boolean matchedDecodedKeyword(String keyword, String itemTypePrefix, String name) {
        return keyword.equals(itemTypePrefix+" "+name) || keyword.equals(itemTypePrefix.toLowerCase()+" "+name) || 
                keyword.equals(itemTypePrefix+"# "+name) || keyword.equals(itemTypePrefix.toLowerCase()+"# "+name) ||
                keyword.equals(name) || keyword.equals(name.toLowerCase());
    }

    private Div createSearchResultsNotesComponent() {
        String notes = searchResult.getNotes();
        if (notes.isEmpty()) {
            return new Div(new Span("—"));
        } else {
            Div notesColumn = (Div) highlightSearchResult(new Div(), notes, null, true);
            notesColumn.addClassName("grid-notes-column");
            return notesColumn;
        }
    }

    private <ROW extends Component & HasComponents & HasStyle> ROW highlightSearchResult(ROW searchResultColumn, String columnText, String toMatch, boolean isHighlightable) {
        String escapedKeyword = PathmindStringUtils.escapeNonAlphanumericalCharacters(decodedKeyword);
        String[] parts = columnText.split("(?i)((?<="+escapedKeyword+")|(?=(?i)"+escapedKeyword+"))");

        if (!isHighlightable) {
            searchResultColumn.add(new Span(columnText));
            return searchResultColumn;
        }

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].toLowerCase().equals(decodedKeyword.toLowerCase())) {
                searchResultColumn.add(
                    LabelFactory.createLabel(parts[i], CssPathmindStyles.HIGHLIGHT_LABEL)
                );
            } else if (toMatch != null && escapedKeyword.matches(toMatch)) {
                searchResultColumn.add(
                    LabelFactory.createLabel(columnText, CssPathmindStyles.HIGHLIGHT_LABEL)
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