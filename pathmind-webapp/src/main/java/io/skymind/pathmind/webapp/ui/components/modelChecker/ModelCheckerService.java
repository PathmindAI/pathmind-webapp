package io.skymind.pathmind.webapp.ui.components.modelChecker;

import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.TAG_LABEL;

@Service
public class ModelCheckerService {
    private final ProjectFileCheckService projectFileCheckService;

    public ModelCheckerService(ProjectFileCheckService projectFileCheckService) {
        this.projectFileCheckService = projectFileCheckService;
    }

    public Span createInvalidErrorLabel(Model model) {
        Span result = LabelFactory.createLabel("", TAG_LABEL, ERROR_LABEL);
        result.setVisible(false);
        ModelUtils.checkIfModelIsInvalid(model).ifPresent(invalidModelType -> {
            result.getElement().setProperty("innerHTML", projectFileCheckService.getErrorMessage(invalidModelType));
            result.setVisible(true);
        });
        return result;
    }
}
