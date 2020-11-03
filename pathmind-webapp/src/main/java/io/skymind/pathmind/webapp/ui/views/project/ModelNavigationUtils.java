package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Optional;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

public class ModelNavigationUtils {
    private ModelNavigationUtils() {
    }

    public static void navigateToModel(Optional<UI> optionalUI, Model model) {
        optionalUI.ifPresent(ui -> ui.navigate(ProjectView.class, PathmindUtils.getProjectModelPath(model.getProjectId(), model.getId())));
    }
}
