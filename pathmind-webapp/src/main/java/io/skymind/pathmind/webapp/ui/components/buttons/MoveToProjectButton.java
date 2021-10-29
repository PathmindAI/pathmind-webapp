package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.project.ChooseProjectForModelViewContent;

public class MoveToProjectButton extends Button {

    long modelId;

    public MoveToProjectButton(long modelId, ProjectDAO projectDAO, ModelDAO modelDAO, SegmentIntegrator segmentIntegrator) {
        super("Move Model");
        setIcon(new Icon(VaadinIcon.FILE_TREE));
        this.modelId = modelId;

        addClickListener(evt -> getUI().ifPresent(ui -> {
            Dialog dialog = new Dialog();
            ChooseProjectForModelViewContent dialogContent = new ChooseProjectForModelViewContent(projectDAO, modelDAO, segmentIntegrator);
            Model model;
            try {
                model = modelDAO.getModelIfAllowed(this.modelId, SecurityUtils.getUserId()).get();
            } catch (Exception e) {
                throw new InvalidDataException("You don't have permission to access this model");
            }
            dialogContent.setModel(model);
            dialogContent.setIsDialog(true);
            dialogContent.setSubmitButtonCallback(() -> dialog.close());
            dialog.add(dialogContent);
            dialog.open();
        }));

        addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addClassName("move-model-button");
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

}
