package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Tag("models-navbar-item")
@JsModule("./src/project/models-navbar-item.ts")
public class ModelsNavbarItem extends LitTemplate {
    private ModelDAO modelDAO;
    private Model model;
    private ModelsNavbar modelsNavbar;
    private SegmentIntegrator segmentIntegrator;
    private ProjectView projectView;

    public ModelsNavbarItem(ModelsNavbar modelsNavbar, ProjectView projectView, ModelDAO modelDAO, Model model, SegmentIntegrator segmentIntegrator) {
        this.modelDAO = modelDAO;
        this.model = model;
        this.modelsNavbar = modelsNavbar;
        this.projectView = projectView;
        this.segmentIntegrator = segmentIntegrator;

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
        NavBarItemSelectModelAction.selectModel(model, projectView);
    }

    private void navigateToUploadModelView() {
        String target = PathmindUtils.getResumeUploadModelPath(projectView.getProjectId(), model.getId());
        projectView.getUI().ifPresent(ui -> ui.navigate(UploadModelView.class, target));
    }

    @ClientCallable
    private void archiveOrUnarchiveEventHandler(Boolean isArchive) {
        modelDAO.archive(model.getId(), isArchive);
        segmentIntegrator.archived(Model.class, isArchive);
        getElement().setProperty("isArchived", isArchive);
        model.setArchived(isArchive);
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
