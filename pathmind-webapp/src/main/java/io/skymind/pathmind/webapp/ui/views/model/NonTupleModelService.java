package io.skymind.pathmind.webapp.ui.views.model;

import com.vaadin.flow.component.html.Span;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import org.springframework.stereotype.Service;

@Service
public class NonTupleModelService {
    private final ProjectFileCheckService projectFileCheckService;

    public NonTupleModelService(ProjectFileCheckService projectFileCheckService) {
        this.projectFileCheckService = projectFileCheckService;
    }

    public Span createNonTupleErrorLabel(Model model) {
        Span result = LabelFactory.createLabel("", "tag", "error-label");
        result.getElement().setProperty("innerHTML", projectFileCheckService.getNonTupleErrorMessage());
        result.setVisible(!ModelUtils.isTupleModel(model));
        return result;
    }
}
