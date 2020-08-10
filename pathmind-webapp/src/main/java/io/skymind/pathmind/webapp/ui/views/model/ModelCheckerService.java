package io.skymind.pathmind.webapp.ui.views.model;

import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import org.springframework.stereotype.Service;

@Service
public class ModelCheckerService {
    private final ProjectFileCheckService projectFileCheckService;

    public ModelCheckerService(ProjectFileCheckService projectFileCheckService) {
        this.projectFileCheckService = projectFileCheckService;
    }

    public Span createInvalidErrorLabel(Model model) {
        Span result = LabelFactory.createLabel("", "tag", "error-label");
        result.setVisible(false);
        ModelUtils.checkIfModelIsInvalid(model).ifPresent(invalidModelType -> {
            result.getElement().setProperty("innerHTML", projectFileCheckService.getErrorMessage(invalidModelType));
            result.setVisible(true);
        });
        return result;
    }
}
