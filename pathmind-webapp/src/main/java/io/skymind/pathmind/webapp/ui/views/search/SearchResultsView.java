package io.skymind.pathmind.webapp.ui.views.search;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.components.buttons.NewProjectButton;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.search.dataprovider.SearchResultsDataProvider;

@Route(value= Routes.SEARCHRESULTS_URL, layout = MainLayout.class)
public class SearchResultsView extends PathMindDefaultView implements HasUrlParameter<String>{

    protected ConfigurableFilterDataProvider<SearchResult, Void, String> dataProvider;
    
    @Autowired
    public SearchResultsView(SearchResultsDataProvider searchResultsDataProvider) {
        dataProvider = searchResultsDataProvider.withConfigurableFilter();
    }
    
    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Search results");
    }

    @Override
    protected Component getMainContent() {
        addClassName("search-results-view");
        
        Span title = LabelFactory.createLabel("Search results", CssMindPathStyles.SECTION_TITLE_LABEL, CssMindPathStyles.TRUNCATED_LABEL);
        HorizontalLayout headerWrapper = WrapperUtils.wrapWidthFullHorizontal(title);
        Grid<SearchResult> grid = createSearchResultsGrid();
        
        FlexLayout gridWrapper = new ViewSection(headerWrapper, grid);
        gridWrapper.addClassName("page-content");
        return gridWrapper;
    }

    private Grid<SearchResult> createSearchResultsGrid() {
        Grid<SearchResult> grid = new Grid<>();
        grid.addColumn(SearchResult::getItemType).setHeader("Type").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(SearchResult::getName).setHeader("Name").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(new LocalDateTimeRenderer<>(SearchResult::getCreateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER)).setHeader("Creates").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(new LocalDateTimeRenderer<>(SearchResult::getUpdateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER)).setHeader("Last Activity").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(SearchResult::getNotes).setHeader("Notes").setAutoWidth(true).setFlexGrow(1).setResizable(true);
        grid.setSizeFull();
        grid.setDataProvider(dataProvider);
        return grid;
    }

    @Override
    public void setParameter(BeforeEvent event, String keyword) {
        dataProvider.setFilter(keyword);
    }

}
