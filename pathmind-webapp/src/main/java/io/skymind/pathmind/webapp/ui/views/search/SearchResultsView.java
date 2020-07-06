package io.skymind.pathmind.webapp.ui.views.search;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ViewSection;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.ui.views.search.dataprovider.SearchResultsDataProvider;

@Route(value= Routes.SEARCHRESULTS_URL, layout = MainLayout.class)
public class SearchResultsView extends PathMindDefaultView implements HasUrlParameter<String>{

    private ConfigurableFilterDataProvider<SearchResult, Void, String> dataProvider;
    private ExperimentDAO experimentDao;
    
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
    protected Component getMainContent() {
        addClassName("search-results-view");
        
        Span title = LabelFactory.createLabel("Search results", CssMindPathStyles.SECTION_TITLE_LABEL, CssMindPathStyles.TRUNCATED_LABEL);
        HorizontalLayout headerWrapper = WrapperUtils.wrapWidthFullHorizontal(title);
        Grid<SearchResult> grid = createSearchResultsGrid();
        grid.addSelectionListener(evt -> navigateToSelectedRecord(evt.getFirstSelectedItem()));
        
        FlexLayout gridWrapper = new ViewSection(headerWrapper, grid);
        gridWrapper.addClassName("page-content");
        return gridWrapper;
    }

    private void navigateToSelectedRecord(Optional<SearchResult> selectedItem) {
        selectedItem.ifPresent(item -> {
            getMainLayout().ifPresent(MainLayout::clearSearchBoxValue);
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
        grid.addColumn(SearchResult::getItemType).setHeader("Type").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(SearchResult::getName).setHeader("Name").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(new LocalDateTimeRenderer<>(SearchResult::getCreateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER)).setHeader("Created").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(new LocalDateTimeRenderer<>(SearchResult::getUpdateDate, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER)).setHeader("Last Activity").setAutoWidth(true).setFlexGrow(0).setResizable(true);
        grid.addColumn(SearchResult::getNotes).setHeader("Notes").setAutoWidth(true).setFlexGrow(1).setResizable(true);
        grid.setSizeFull();
        grid.setDataProvider(dataProvider);
        return grid;
    }

    @Override
    public void setParameter(BeforeEvent event, String keyword) {
        dataProvider.setFilter(URLDecoder.decode(keyword, StandardCharsets.UTF_8));
    }

}
