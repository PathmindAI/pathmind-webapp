package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.components.buttons.UploadModelButton;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.ui.views.project.components.navbar.subscribers.NavBarModelArchivedSubscriber;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class ModelsNavbar extends VerticalLayout {
    private List<Model> models;
    private Model selectedModel;
    private String activeLabel = "Active";
    private String archivedLabel = "Archived";

    private List<ModelsNavbarItem> modelsNavbarItems = new ArrayList<>();
    private Select<String> categorySelect;
    private VerticalLayout rowsWrapper;
    private UploadModelButton newModelButton;
    private SegmentIntegrator segmentIntegrator;
    private ProjectView projectView;

    private ModelDAO modelDAO;

    public ModelsNavbar(ProjectView projectView, ModelDAO modelDAO, Model selectedModel, List<Model> models, SegmentIntegrator segmentIntegrator) {
        this.modelDAO = modelDAO;
        this.models = models;
        this.selectedModel = selectedModel;
        this.projectView = projectView;
        this.segmentIntegrator = segmentIntegrator;

        rowsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        rowsWrapper.addClassName("models-navbar-items");

        newModelButton = new UploadModelButton(models.get(0).getProjectId());

        createCategorySelect();

        setPadding(false);
        setSpacing(false);
        setWidth("auto");
        add(newModelButton, categorySelect, rowsWrapper);
        addClassName("models-navbar");
        addModelsToNavbar();
    }

    private void createCategorySelect() {
        categorySelect = new Select<>();
        categorySelect.setItems(activeLabel, archivedLabel);
        categorySelect.getElement().setAttribute("theme", "models-nav-bar-select small");
        categorySelect.addValueChangeListener(event -> {
            if (event.getValue().equals(archivedLabel)) {
                categorySelect.getElement().setAttribute("show-archived", true);
            } else {
                categorySelect.getElement().removeAttribute("show-archived");
            }
        });
    }

    public void setCurrentCategory() {
        if (selectedModel.isArchived()) {
            categorySelect.setValue(archivedLabel);
        } else {
            categorySelect.setValue(activeLabel);
        }
    }

    private void addModelsToNavbar() {
        models.sort(
                comparing(Model::getProjectChangedAt, reverseOrder())
                        .thenComparing(Model::getDateCreated, reverseOrder())
        );
        models.forEach(model -> {
                ModelsNavbarItem navBarItem = createModelsNavbarItem(model);
                modelsNavbarItems.add(navBarItem);
                if (model.equals(selectedModel)) {
                    navBarItem.setAsCurrent();
                }
                rowsWrapper.add(navBarItem);
        });
        setCurrentCategory();
    }

    private ModelsNavbarItem createModelsNavbarItem(Model model) {
        return new ModelsNavbarItem(this, projectView, modelDAO, model, segmentIntegrator);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this, () -> getUI(), 
                new NavBarModelArchivedSubscriber(this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public void setCurrentModel(Model newCurrentModel) {
        selectedModel = newCurrentModel;

        modelsNavbarItems.stream()
            .filter(item -> item.getIsCurrent())
            .findFirst()
            .ifPresent(item -> item.removeAsCurrent());

        modelsNavbarItems.stream()
            .filter(item -> item.getItemModel().equals(selectedModel))
            .findFirst()
            .ifPresent(item -> item.setAsCurrent());
    }

    public void setModelIsArchived(Model model, Boolean isArchived) {
        modelsNavbarItems.stream()
            .filter(item -> item.getItemModel().getId() == model.getId())
            .findFirst()
            .ifPresent(item -> item.setIsArchived(isArchived));
    }

    public long getProjectId() {
        return projectView.getProjectId();
    }

}
