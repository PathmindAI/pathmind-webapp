package io.skymind.pathmind.webapp.ui.views.project.dataprovider;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.skymind.pathmind.services.project.ExperimentGridService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class ExperimentGridDataProvider extends AbstractBackEndDataProvider<Experiment, Void> {
    
    @Autowired
    private ExperimentGridService service;

    private Long modelId;

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    @Override
    protected Stream<Experiment> fetchFromBackEnd(Query<Experiment, Void> query) {
        return service.getExperimentsInModelForUser(SecurityUtils.getUserId(), modelId, query.getOffset(), query.getLimit()).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Experiment, Void> query) {
        return service.countTotalExperimentsInModel(modelId);
    }

}
