package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Tag("models-navbar-item")
@JsModule("./src/project/models-navbar-item.ts")
public class ModelsNavbarItem extends LitTemplate {
    private Model model;
    private ModelsNavbar modelsNavbar;
    private ProjectView projectView;

    public ModelsNavbarItem(ModelsNavbar modelsNavbar, Model model) {
        this(modelsNavbar, null, model);
    }

    public ModelsNavbarItem(ModelsNavbar modelsNavbar, ProjectView projectView, Model model) {
        this.model = model;
        this.modelsNavbar = modelsNavbar;
        this.projectView = projectView;

        setModelDetails(model);
    }

    private void setModelDetails(Model model) {
        long projectId = model.getProjectId();
        long modelId = model.getId();
        getElement().setProperty("isDraft", model.isDraft());
        getElement().setProperty("isArchived", model.isArchived());
        getElement().setProperty("isCurrent", false);
        getElement().setProperty("modelName", model.getName());
        getElement().setProperty("modelPackageName", model.getPackageName());
        String target = model.isDraft() ?
                "/uploadModel/" + PathmindUtils.getResumeUploadModelPath(projectId, modelId)
                : PathmindUtils.getProjectModelPath(projectId, modelId);
        getElement().setProperty("modelLink", target);
        getElement().appendChild(new DatetimeDisplay(model.getDateCreated()).getElement());
    }

    @ClientCallable
    private void handleRowClicked() {
        if (model.isDraft()) {
            navigateToUploadModelView();
        }
        modelsNavbar.setCurrentModel(model);
        
        if (projectView != null) {
            getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.PROJECT + "/" + model.getProjectId() + "/model/" + model.getId()));
            projectView.setModel(model);
        } else {
            getUI().ifPresent(ui -> ui.navigate(Routes.PROJECT + "/" + model.getProjectId() + "/model/" + model.getId()));
        }
    }

    private void navigateToUploadModelView() {
        String target = PathmindUtils.getResumeUploadModelPath(model.getProjectId(), model.getId());
        getUI().ifPresent(ui -> ui.navigate(UploadModelView.class, target));
    }

    public void setIsArchived(Boolean isArchive) {
        getElement().setProperty("isArchived", isArchive);
        modelsNavbar.setCurrentCategory();
    }

    public void setAsCurrent() {
        getElement().setProperty("isCurrent", true);
    }

    public Boolean getIsCurrent() {
        return Boolean.parseBoolean(getElement().getProperty("isCurrent"));
    }

    public void removeAsCurrent() {
        getElement().setProperty("isCurrent", false);
    }

    public Model getItemModel() {
        return model;
    }
}
