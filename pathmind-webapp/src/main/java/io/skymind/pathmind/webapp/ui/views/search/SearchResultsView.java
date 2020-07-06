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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
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
    private String titleText = "Search Results";
    
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
    }

    @Override
    protected Component getMainContent() {
        addClassName("search-results-view");
        
        Span title = LabelFactory.createLabel(titleText, CssPathmindStyles.SECTION_TITLE_LABEL, CssPathmindStyles.TRUNCATED_LABEL);
        HorizontalLayout headerWrapper = WrapperUtils.wrapWidthFullHorizontal(title);
        Grid<SearchResult> grid = createSearchResultsGrid();
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

    private Grid<SearchResult> createSearchResultsGrid() {
        Grid<SearchResult> grid = new Grid<>();
        grid.addColumn(SearchResult::getItemType)
            .setHeader("Type")
            .setAutoWidth(true)
            .setFlexGrow(0)
            .setResizable(true);
        grid.addColumn(TemplateRenderer.<SearchResult> of("[[item.name]] <span class='tag'>[[item.archived]]</span>")
                .withProperty("name", SearchResult::getName)
                .withProperty("archived", resultItem -> resultItem.getIsArchived() ? "Archived" : ""))
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
        grid.addColumn(searchResult -> {
            String notes = searchResult.getNotes();
            return notes.isEmpty() ? "—" : notes;
        })
            .setClassNameGenerator(column -> "grid-notes-column")
            .setHeader("Notes")
            .setResizable(true);
        grid.setSizeFull();
        grid.setDataProvider(dataProvider);
        grid.sort(Arrays.asList(new GridSortOrder<>(lastActivityColumn, SortDirection.DESCENDING)));
        return grid;
    }

    @Override
    public void setParameter(BeforeEvent event, String keyword) {
        String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        dataProvider.setFilter(decodedKeyword);
        titleText = "Search Results for: " + decodedKeyword;
    }

}
