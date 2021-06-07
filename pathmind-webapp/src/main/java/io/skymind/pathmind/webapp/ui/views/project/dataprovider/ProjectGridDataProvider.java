package io.skymind.pathmind.webapp.ui.views.project.dataprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.skymind.pathmind.db.utils.GridSortOrder;
import io.skymind.pathmind.services.project.ProjectGridService;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class ProjectGridDataProvider extends AbstractBackEndDataProvider<Project, Boolean> {
    
    @Autowired
    private ProjectGridService service;

    private List<GridSortOrder> sortOrders = new ArrayList<>();

    private void setSortOrders(Query<Project, Boolean> query) {
        sortOrders = query.getSortOrders()
                        .stream()
                        .map(sortOrder -> new GridSortOrder(
                                sortOrder.getSorted(),
                                sortOrder.getDirection().equals(SortDirection.DESCENDING)))
                        .collect(Collectors.toList());
    }

    @Override
    protected Stream<Project> fetchFromBackEnd(Query<Project, Boolean> query) {
        setSortOrders(query);
        if (query.getFilter().isPresent()) {
            return service.getFilteredProjectsForUser(SecurityUtils.getUserId(),
                    query.getFilter().get(),
                    query.getOffset(),
                    query.getLimit(),
                    sortOrders).stream();
        }
        return Stream.empty();
    }

    @Override
    protected int sizeInBackEnd(Query<Project, Boolean> query) {
        if (query.getFilter().isPresent()) {
            return service.countFilteredProjects(SecurityUtils.getUserId(), query.getFilter().get());
        }
        return 0;
    }

}
