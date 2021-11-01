package io.skymind.pathmind.webapp.ui.views.project.components;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.buttons.ArchiveUnarchiveModelButton;
import io.skymind.pathmind.webapp.ui.components.buttons.EditGoalsButton;
import io.skymind.pathmind.webapp.ui.components.buttons.MoveToProjectButton;
import io.skymind.pathmind.webapp.ui.components.buttons.NewExperimentButton;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.project.ExportAllPoliciesButton;

public class ModelActionBar extends HorizontalLayout {

    Model model;
    Long modelId;

    MoveToProjectButton moveToProjectButton;
    ArchiveUnarchiveModelButton archiveUnarchiveModelButton;
    EditGoalsButton editGoalsButton;
    ExportAllPoliciesButton exportAllPoliciesButton;
    NewExperimentButton newExperimentButton;

    public ModelActionBar(ProjectDAO projectDAO,
                        ModelDAO modelDAO,
                        ExperimentDAO experimentDAO,
                        PolicyFileService policyFileService,
                        Model model,
                        SegmentIntegrator segmentIntegrator) {
        this.model = model;
        this.modelId = model.getId();

        moveToProjectButton = new MoveToProjectButton(modelId, projectDAO, modelDAO, segmentIntegrator);
        archiveUnarchiveModelButton = new ArchiveUnarchiveModelButton(model, modelDAO, segmentIntegrator);
        editGoalsButton = new EditGoalsButton(modelId, segmentIntegrator);
        exportAllPoliciesButton = new ExportAllPoliciesButton(policyFileService, experimentDAO);
        newExperimentButton = new NewExperimentButton(experimentDAO, modelId, ButtonVariant.LUMO_TERTIARY,
                segmentIntegrator);

        add(exportAllPoliciesButton, moveToProjectButton, archiveUnarchiveModelButton, editGoalsButton, newExperimentButton);
        setSpacing(false);
        addClassName("model-action-bar");
    }

    public void setModel(Model model) {
        this.model = model;
        this.modelId = model.getId();

        newExperimentButton.setModelId(modelId);
        moveToProjectButton.setModelId(modelId);
        editGoalsButton.setModelId(modelId);
        exportAllPoliciesButton.setModelId(modelId);
        archiveUnarchiveModelButton.setModel(model);
    }
    
}
