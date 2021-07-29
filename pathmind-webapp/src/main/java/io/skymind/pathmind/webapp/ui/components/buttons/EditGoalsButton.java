package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.model.EditGoalsView;

public class EditGoalsButton extends Button {

    long modelId;

    public EditGoalsButton(long modelId, SegmentIntegrator segmentIntegrator) {
        this(modelId, ButtonVariant.LUMO_PRIMARY, segmentIntegrator);
    }

    public EditGoalsButton(long modelId, ButtonVariant buttonVariant, SegmentIntegrator segmentIntegrator) {
        super("Edit Goals");
        setIcon(new Icon(VaadinIcon.EDIT));
        this.modelId = modelId;

        addClickListener(evt -> getUI().ifPresent(ui -> {
            segmentIntegrator.navigatedToEditGoalsFromProjectView();
            ui.navigate(EditGoalsView.class, this.modelId);
        }));

        addThemeVariants(buttonVariant);
        addClassName("edit-goals-button");
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

}
