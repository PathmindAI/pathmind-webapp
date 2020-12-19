package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.atoms.DatetimeDisplay;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.project.ModelNavigationUtils;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Tag("models-navbar-item")
@JsModule("./src/project/models-navbar-item.js")
public class ModelsNavbarItem extends PolymerTemplate<ModelsNavbarItem.PolymerModel> {
    private ModelDAO modelDAO;
    private Model model;
    private ModelsNavbar modelsNavbar;
    private SegmentIntegrator segmentIntegrator;

    // TODO -> STEPH -> Create x time ago seems to be way off.
    public ModelsNavbarItem(ModelsNavbar modelsNavbar, Supplier<Optional<UI>> getUISupplier, ModelDAO modelDAO, Model model, SegmentIntegrator segmentIntegrator) {
        this.modelDAO = modelDAO;
        this.model = model;
        this.modelsNavbar = modelsNavbar;
        this.segmentIntegrator = segmentIntegrator;

        UI.getCurrent().getUI().ifPresent(ui -> setModelDetails(ui, model));
    }

    @EventHandler
    private void onArchiveButtonClicked() {
        modelDAO.archive(model.getId(), true);
        segmentIntegrator.archived(Model.class, true);
        getModel().setIsArchived(true);
        reloadCurrentModelView();
    }

    @EventHandler
    private void onUnarchiveButtonClicked() {
        modelDAO.archive(model.getId(), false);
        segmentIntegrator.archived(Model.class, false);
        getModel().setIsArchived(false);
        reloadCurrentModelView();
    }

    private void reloadCurrentModelView() {
        ModelNavigationUtils.navigateToModel(getUI(), modelsNavbar.getSelectedModel());
    }

    public void setAsCurrent() {
        getModel().setIsCurrent(true);
    }

    public Model getItemModel() {
        return model;
    }

    private void setModelDetails(UI ui, Model model) {
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

        void setIsCurrent(boolean isCurrent);

        void setIsDraft(boolean isDraft);

        void setIsArchived(boolean isArchived);
    }
}
