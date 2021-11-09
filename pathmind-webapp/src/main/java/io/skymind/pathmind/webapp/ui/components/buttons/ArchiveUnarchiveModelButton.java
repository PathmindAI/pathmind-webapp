package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ModelArchivedBusEvent;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;

public class ArchiveUnarchiveModelButton extends Button {

    ModelDAO modelDAO;
    Model model;
    long modelId;
    SegmentIntegrator segmentIntegrator;

    public ArchiveUnarchiveModelButton(Model model, ModelDAO modelDAO, SegmentIntegrator segmentIntegrator) {
        super();
        setModel(model);
        setButtonIcon();
        setButtonText();
        this.modelDAO = modelDAO;
        this.segmentIntegrator = segmentIntegrator;

        addClickListener(evt -> {
            String modelName = "Model #"+this.model.getName()+" ("+this.model.getPackageName()+")";
            if (model.isArchived()) {
                ConfirmationUtils.unarchive(modelName, () -> archiveAction());
            } else {
                ConfirmationUtils.archive(modelName, () -> archiveAction());
            }
        });

        addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addClassName("archive-model-button");
    }

    private void archiveAction() {
        Boolean newArchiveStatus = !model.isArchived();
        modelDAO.archive(this.modelId, newArchiveStatus);
        segmentIntegrator.archived(Model.class, newArchiveStatus);
        model.setArchived(newArchiveStatus);
        EventBus.post(new ModelArchivedBusEvent(model));
        setButtonIcon();
        setButtonText();
    }

    private void setButtonIcon() {
        if (model.isArchived()) {
            setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
        } else {
            setIcon(new Icon(VaadinIcon.ARCHIVE));
        }
    }

    private void setButtonText() {
        if (model.isArchived()) {
            setText("Unarchive");
        } else {
            setText("Archive");
        }
    }

    public void setModel(Model model) {
        this.model = model;
        this.modelId = model.getId();
        setButtonIcon();
        setButtonText();
    }

}
