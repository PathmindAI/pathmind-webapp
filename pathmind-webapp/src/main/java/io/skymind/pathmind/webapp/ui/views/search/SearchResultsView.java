package io.skymind.pathmind.webapp.ui.views.search;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.PathmindStringUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.SearchBox;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.search.components.SearchResultItem;
import io.skymind.pathmind.webapp.ui.views.search.dataprovider.SearchResultsDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.SEARCHRESULTS, layout = MainLayout.class)
public class SearchResultsView extends PathMindDefaultView implements AfterNavigationObserver, BeforeLeaveObserver, HasUrlParameter<String> {

    private ConfigurableFilterDataProvider<SearchResult, Void, String> dataProvider;
    private ExperimentDAO experimentDAO;
    private String decodedKeyword;
    private String titleText = "Search Results";
    private String numberOfResultsText;
    private Span numberOfResults;

    @Autowired
    private ModelService modelService;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    public SearchResultsView(SearchResultsDataProvider searchResultsDataProvider, ExperimentDAO experimentDAO) {
        dataProvider = searchResultsDataProvider.withConfigurableFilter();
        this.experimentDAO = experimentDAO;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        int resultsCount = dataProvider.size(new Query<>());
        numberOfResultsText = "Showing " + resultsCount;
        numberOfResultsText += resultsCount == 1 ? " result" : " results";
        numberOfResults.setText(numberOfResultsText);
        segmentIntegrator.performedSearch();
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        Class navigationTarget = event.getNavigationTarget();
        if (navigationTarget != SearchResultsView.class) {
            getMainLayout().ifPresent(MainLayout::clearSearchBoxValue);
        }
    }

    @Override
    protected Component getMainContent() {
        addClassName("search-results-view");

        Grid<SearchResult> grid = createSearchResultsGrid();
        Span title = LabelFactory.createLabel(titleText, CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.TRUNCATED_LABEL);
        numberOfResults = LabelFactory.createLabel("", CssPathmindStyles.SECTION_SUBTITLE_LABEL);
        VerticalLayout headerWrapper = new VerticalLayout(title, numberOfResults);
        headerWrapper.setSpacing(false);

        FlexLayout gridWrapper = new ViewSection(headerWrapper, grid);
        gridWrapper.addClassName("page-content");
        return gridWrapper;
    }

    private Optional<MainLayout> getMainLayout() {
        return getParent().map(p -> (MainLayout) p);
    }

    private Grid<SearchResult> createSearchResultsGrid() {
        Grid<SearchResult> grid = new Grid<>();
        grid.addClassName("search-results");
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(
                searchResult -> new SearchResultItem(experimentDAO, modelService, searchResult, decodedKeyword)
        );
        grid.setSizeFull();
        grid.setDataProvider(dataProvider);
        return grid;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String keyword) {
        if (keyword == null) {
            decodedKeyword = "";
            titleText = "You did not search for anything.";
        } else {
            int maxAllowedInputSize = SearchBox.MAX_SEARCH_TYPE_LENGTH + SearchBox.OPERATOR_LENGTH + SearchBox.MAX_KEYWORD_LENGTH;
            String decodedValue = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
            decodedKeyword = decodedValue.substring(0, Math.min(decodedValue.length(), maxAllowedInputSize));
            String escapedBackslashDecodedKeyword = PathmindStringUtils.escapeBackslash(decodedKeyword);
            decodedKeyword = getActuaKeyword(decodedKeyword);
            dataProvider.setFilter(escapedBackslashDecodedKeyword);
            titleText = "Search Results for: " + decodedKeyword;
        }
    }

    private String getActuaKeyword(String fullKeyword) {
        String[] split = fullKeyword.split(":", 2);
        if (split.length == 2) {
            return split[1];
        } else {
            return split[0];
        }
    }

}
