package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Tag("models-navbar-item")
@JsModule("./src/project/models-navbar-item.js")
public class ModelsNavbarItem extends PolymerTemplate<ModelsNavbarItem.PolymerModel> {
    private ModelsNavbar modelsNavbar;
    private Supplier<Optional<UI>> getUISupplier;
    private ModelDAO modelDAO;
    private Model model;
    private Consumer<Model> selectModelConsumer;

    public ModelsNavbarItem(ModelsNavbar modelsNavbar, Supplier<Optional<UI>> getUISupplier, ModelDAO modelDAO, Model model, Consumer<Model> selectModelConsumer) {
        this.modelsNavbar = modelsNavbar;
        this.getUISupplier = getUISupplier;
	    this.modelDAO = modelDAO;
	    this.model = model;
        this.selectModelConsumer = selectModelConsumer;

        UI.getCurrent().getUI().ifPresent(ui -> setModelDetails(ui, model));
    }

    @EventHandler
    private void handleItemClicked() {
        setAsCurrent();
        selectModelConsumer.accept(model);
    }

    @EventHandler
    private void onArchiveButtonClicked() {
        modelDAO.archive(model.getId(), true);
        getModel().setIsArchived(true);
    }

    @EventHandler
    private void onUnarchiveButtonClicked() {
        modelDAO.archive(model.getId(), false);
        getModel().setIsArchived(false);
    }

    public void setAsCurrent() {
        getModel().setIsCurrent(true);
    }

    public void removeAsCurrent() {
        getModel().setIsCurrent(false);
    }

    public Model getItemModel() {
        return model;
    }

    public void setModelDetails(UI ui, Model model) {
        getModel().setIsDraft(model.isDraft());
        getModel().setIsArchived(model.isArchived());
        getModel().setModelName(model.getName());
        getModel().setModelPackageName(model.getPackageName());
        VaadinDateAndTimeUtils.withUserTimeZoneId(ui, timeZoneId -> {
            getModel().setCreatedDate(DateAndTimeUtils.formatDateAndTimeShortFormatter(model.getDateCreated(), timeZoneId));
        });
    }

	public interface PolymerModel extends TemplateModel {
        void setModelName(String modelName);
        void setModelPackageName(String modelPackageName);
        void setCreatedDate(String createdDate);
        void setIsCurrent(boolean isCurrent);
        void setIsDraft(boolean isDraft);
        void setIsArchived(boolean isArchived);
    }
}
