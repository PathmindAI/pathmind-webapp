package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentButton extends Button {

    public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, SegmentIntegrator segmentIntegrator) {
        this(experimentDAO, modelId, ButtonVariant.LUMO_PRIMARY, segmentIntegrator);
    }

    public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, ButtonVariant buttonVariant, SegmentIntegrator segmentIntegrator) {
        super("New Experiment");
        setIcon(new Icon(VaadinIcon.PLUS));

        addClickListener(evt -> getUI().ifPresent(ui -> {
            segmentIntegrator.newExperiment();
            Experiment experiment = experimentDAO.createNewExperiment(modelId);
            EventBus.post(new ExperimentCreatedBusEvent(experiment));
            ui.navigate(NewExperimentView.class, ""+experiment.getId());
        }));

        addThemeVariants(buttonVariant);
        addClassName("new-experiment-button");
    }

}
