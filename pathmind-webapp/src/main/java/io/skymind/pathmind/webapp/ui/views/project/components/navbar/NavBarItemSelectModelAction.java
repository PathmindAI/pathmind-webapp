package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;

public class NavBarItemSelectModelAction {
    public static void selectModel(Model model, ProjectView projectView) {
        projectView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.PROJECT + "/" + model.getId()));
        synchronized (projectView.getModelLock()) {
            projectView.setModel(model);
        }
    }
}
