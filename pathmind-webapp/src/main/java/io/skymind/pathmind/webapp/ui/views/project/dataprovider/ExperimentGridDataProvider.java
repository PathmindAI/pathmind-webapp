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
import io.skymind.pathmind.services.project.ExperimentGridService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class ExperimentGridDataProvider extends AbstractBackEndDataProvider<Experiment, Boolean> {
    
    @Autowired
    private ExperimentGridService service;

    private List<GridSortOrder> sortOrders = new ArrayList<>();
    private Long modelId;

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

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
            return service.getExperimentsInModelForUser(SecurityUtils.getUserId(), 
                    modelId, 
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
            return service.countFilteredExperimentsInModel(modelId, query.getFilter().get());
        }
        return 0;
    }

}
