package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

public class NewExperimentButton extends Button
{
    private long modelId;

	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, SegmentIntegrator segmentIntegrator) {
        this(experimentDAO, modelId, ButtonVariant.LUMO_PRIMARY, segmentIntegrator);
    }

	public NewExperimentButton(ExperimentDAO experimentDAO, long modelId, ButtonVariant buttonVariant, SegmentIntegrator segmentIntegrator) {
        super("New Experiment");
        setModelId(modelId);
		setIcon(new Icon(VaadinIcon.PLUS));

        addClickListener(evt -> getUI().ifPresent(ui -> {
            segmentIntegrator.newExperiment();
            ExperimentUtils.createAndNavigateToNewExperiment(ui, experimentDAO, this.modelId);
        }));

        addThemeVariants(buttonVariant);

        addClassName("new-experiment-button");
			
	}

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }
}
