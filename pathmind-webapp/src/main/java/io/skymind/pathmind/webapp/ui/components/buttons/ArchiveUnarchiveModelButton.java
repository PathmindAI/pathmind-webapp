package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

public class ArchiveUnarchiveModelButton extends Button {

    Model model;
    long modelId;

    public ArchiveUnarchiveModelButton(Model model, ButtonVariant buttonVariant, ModelDAO modelDAO, SegmentIntegrator segmentIntegrator) {
        super("Archive Model");
        setIcon(new Icon(VaadinIcon.ARCHIVE));
        setModel(model);

        addClickListener(evt -> {
            Boolean newArchiveStatus = !model.isArchived();
            modelDAO.archive(this.modelId, newArchiveStatus);
            segmentIntegrator.archived(Model.class, newArchiveStatus);
            model.setArchived(newArchiveStatus);
        });

        addThemeVariants(buttonVariant);
        addClassName("archive-model-button");
    }

    public void setModel(Model model) {
        this.model = model;
        this.modelId = model.getId();
    }

}
