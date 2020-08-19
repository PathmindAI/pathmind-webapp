package io.skymind.pathmind.webapp.ui.views.model;

import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.TAG_LABEL;

@Service
public class NonTupleModelService {
    private final ProjectFileCheckService projectFileCheckService;

    public NonTupleModelService(ProjectFileCheckService projectFileCheckService) {
        this.projectFileCheckService = projectFileCheckService;
    }

    public Span createNonTupleErrorLabel(Model model) {
        Span result = LabelFactory.createLabel("", TAG_LABEL, ERROR_LABEL);
        result.getElement().setProperty("innerHTML", projectFileCheckService.getNonTupleErrorMessage());
        result.setVisible(!ModelUtils.isTupleModel(model));
        return result;
    }
}
