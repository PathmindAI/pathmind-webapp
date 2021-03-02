package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Tag("models-navbar-item")
@JsModule("./src/project/models-navbar-item.js")
public class ModelsNavbarItem extends PolymerTemplate<ModelsNavbarItem.PolymerModel> {
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

    @EventHandler
    private void handleRowClicked() {
        modelsNavbar.setCurrentModel(model);
        NavBarItemSelectModelAction.selectModel(model, projectView);
    }

    @EventHandler
    private void onArchiveButtonClicked() {
        archiveOrUnarchiveEventHandler(true);
    }

    @EventHandler
    private void onUnarchiveButtonClicked() {
        archiveOrUnarchiveEventHandler(false);
    }

    private void archiveOrUnarchiveEventHandler(Boolean isArchive) {
        modelDAO.archive(model.getId(), isArchive);
        segmentIntegrator.archived(Model.class, isArchive);
        getModel().setIsArchived(isArchive);
        model.setArchived(isArchive);
        modelsNavbar.setCurrentCategory();
    }

    public void setAsCurrent() {
        getModel().setIsCurrent(true);
    }

    public Boolean getIsCurrent() {
        return getModel().getIsCurrent();
    }

    public void removeAsCurrent() {
        getModel().setIsCurrent(false);
    }

    public Model getItemModel() {
        return model;
    }

    private void setModelDetails(Model model) {
        long projectId = model.getProjectId();
        long modelId = model.getId();
        getModel().setIsDraft(model.isDraft());
        getModel().setIsArchived(model.isArchived());
        getModel().setModelName(model.getName());
        getModel().setModelPackageName(model.getPackageName());
        String target = model.isDraft() ?
                "/uploadModel/" + PathmindUtils.getResumeUploadModelPath(projectId, modelId)
                : PathmindUtils.getProjectModelPath(projectId, modelId);
        getModel().setModelLink(target);
        getElement().appendChild(new DatetimeDisplay(model.getDateCreated()).getElement());
    }

    public interface PolymerModel extends TemplateModel {
        void setModelName(String modelName);

        void setModelPackageName(String modelPackageName);

        void setModelLink(String modelLink);

        Boolean getIsCurrent();
        void setIsCurrent(boolean isCurrent);

        void setIsDraft(boolean isDraft);

        void setIsArchived(boolean isArchived);
    }
}
