package io.skymind.pathmind.webapp.ui.views.policyServer;

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
import io.skymind.pathmind.services.project.PolicyServersGridService;
import io.skymind.pathmind.services.project.ProjectGridService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class PolicyServersGridDataProvider extends AbstractBackEndDataProvider<Experiment, Boolean> {
    
    @Autowired
    private PolicyServersGridService service;

    private List<GridSortOrder> sortOrders = new ArrayList<>();

    private void setSortOrders(Query<Experiment, Boolean> query) {
        sortOrders = query.getSortOrders()
                        .stream()
                        .map(sortOrder -> new GridSortOrder(
                                sortOrder.getSorted(),
                                sortOrder.getDirection().equals(SortDirection.DESCENDING)))
                        .collect(Collectors.toList());
    }

    @Override
    protected Stream<Experiment> fetchFromBackEnd(Query<Experiment, Boolean> query) {
        setSortOrders(query);
        if (query.getFilter().isPresent()) {
            return service.getFilteredExperimentsForUser(SecurityUtils.getUserId(),
                    query.getFilter().get(),
                    query.getOffset(),
                    query.getLimit(),
                    sortOrders).stream();
        }
        return Stream.empty();
    }

    @Override
    protected int sizeInBackEnd(Query<Experiment, Boolean> query) {
        if (query.getFilter().isPresent()) {
            return service.countFilteredExperiments(SecurityUtils.getUserId(), query.getFilter().get());
        }
        return 0;
    }

}
