package io.skymind.pathmind.webapp.ui.views.project.components;

import io.skymind.pathmind.shared.data.Model;

public interface ModelComponent {

    void setModel(Model model);

    // Do nothing by default for view only components.
    default void updateModel() {
    }
}

