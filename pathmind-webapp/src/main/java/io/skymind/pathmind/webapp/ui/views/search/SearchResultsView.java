package io.skymind.pathmind.webapp.ui.views.search;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.ui.views.search.dataprovider.SearchResultsDataProvider;

@Route(value= Routes.SEARCHRESULTS_URL, layout = MainLayout.class)
public class SearchResultsView extends PathMindDefaultView implements AfterNavigationObserver, HasUrlParameter<String>{

    private ConfigurableFilterDataProvider<SearchResult, Void, String> dataProvider;
    private ExperimentDAO experimentDao;
    private String decodedKeyword;
    private String titleText = "Search Results";
    private String numberOfResultsText;
    private Span numberOfResults;
    
    @Autowired
    public SearchResultsView(SearchResultsDataProvider searchResultsDataProvider, ExperimentDAO experimentDao) {
        dataProvider = searchResultsDataProvider.withConfigurableFilter();
        this.experimentDao = experimentDao;
    }
    
    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        getMainLayout().ifPresent(MainLayout::clearSearchBoxValue);
        numberOfResultsText = "Showing " + dataProvider.size(new Query<>()) + " results";
        numberOfResults.setText(numberOfResultsText);
    }

    @Override
    protected Component getMainContent() {
        addClassName("search-results-view");

        Grid<SearchResult> grid = createSearchResultsGrid();
        Span title = LabelFactory.createLabel(titleText, CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.TRUNCATED_LABEL);
        numberOfResults = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);
        VerticalLayout headerWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(title, numberOfResults);
        grid.addSelectionListener(evt -> navigateToSelectedRecord(evt.getFirstSelectedItem()));
        
        FlexLayout gridWrapper = new ViewSection(headerWrapper, grid);
        gridWrapper.addClassName("page-content");
        return gridWrapper;
    }

    private void navigateToSelectedRecord(Optional<SearchResult> selectedItem) {
        selectedItem.ifPresent(item -> {
            switch (item.getItemType()) {
                case PROJECT :
                    getUI().ifPresent(ui -> ui.navigate(ProjectView.class, item.getItemId()));
                    break;
                case MODEL :
                    getUI().ifPresent(ui -> ui.navigate(ModelView.class, item.getItemId()));
                    break;
                case EXPERIMENT:
                    Experiment experiment = experimentDao.getExperimentWithRuns(item.getItemId()).get();
                    if (ExperimentUtils.isDraftRunType(experiment)) {
                        getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, item.getItemId()));
                    } else {
                        getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, item.getItemId()));
                    }
                    break;
            }
        });
    }

    private Optional<MainLayout> getMainLayout() {
        return getParent().map(p -> (MainLayout) p);
    }

    private Div highlightSearchResult(SearchResult searchResult, String columnText) {
        Div searchResultColumn = new Div();
        Boolean isArchived = searchResult.getIsArchived();
        String[] parts = columnText.split(decodedKeyword);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                searchResultColumn.add(
                    LabelFactory.createLabel(decodedKeyword, CssPathmindStyles.HIGHLIGHT_LABEL)
                );
            }
            searchResultColumn.add(parts[i]);
        }
        if (parts.length == 0) {
            searchResultColumn.add(
                LabelFactory.createLabel(decodedKeyword, CssPathmindStyles.HIGHLIGHT_LABEL)
            );
        }
        if (isArchived) {
            searchResultColumn.add(LabelFactory.createLabel("Archived", CssPathmindStyles.TAG_LABEL));
        }
        searchResultColumn.addClassName("highlighted-text-wrapper");
        return searchResultColumn;
    }

    private Grid<SearchResult> createSearchResultsGrid() {
        Grid<SearchResult> grid = new Grid<>();
        grid.addColumn(SearchResult::getItemType)
            .setHeader("Type")
            .setAutoWidth(true)
            .setFlexGrow(0)
            .setResizable(true);
        grid.addComponentColumn(
                searchResult -> highlightSearchResult(searchResult, searchResult.getName())
            )
            .setHeader("Name")
            .setComparator(Comparator.comparing(SearchResult::getName))
            .setAutoWidth(true)
            .setFlexGrow(0)
            .setResizable(true);
        Grid.Column<SearchResult> createdDateColumn = grid.addColumn(new LocalDateTimeRenderer<>(SearchResult::getCreateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_FOMATTER))
            .setComparator(Comparator.comparing(SearchResult::getCreateDate))
            .setHeader("Created")
            .setAutoWidth(true)
            .setFlexGrow(0)
            .setResizable(true);
        Grid.Column<SearchResult> lastActivityColumn = grid.addColumn(new LocalDateTimeRenderer<>(SearchResult::getUpdateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_FOMATTER))
            .setComparator(Comparator.comparing(SearchResult::getUpdateDate))
            .setHeader("Last Activity")
            .setAutoWidth(true)
            .setFlexGrow(0)
            .setResizable(true);
        grid.addComponentColumn(
                searchResult -> {
                    String notes = searchResult.getNotes();
                    if (notes.isEmpty()) {
                        return new Span("—");
                    } else {
                        Div notesColumn = highlightSearchResult(searchResult, notes);
                        notesColumn.addClassName("grid-notes-column");
                        return notesColumn;
                    }
                }
            )
            .setHeader("Notes")
            .setResizable(true);
        grid.setSizeFull();
        grid.setDataProvider(dataProvider);
        grid.sort(Arrays.asList(new GridSortOrder<>(lastActivityColumn, SortDirection.DESCENDING)));
        return grid;
    }

    @Override
    public void setParameter(BeforeEvent event, String keyword) {
        decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        dataProvider.setFilter(decodedKeyword);
        titleText = "Search Results for: " + decodedKeyword;
    }

}
