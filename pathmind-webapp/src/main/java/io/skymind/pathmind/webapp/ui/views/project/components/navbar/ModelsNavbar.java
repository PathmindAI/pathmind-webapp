package io.skymind.pathmind.webapp.ui.views.project.components.navbar;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.buttons.UploadModelButton;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModelsNavbar extends VerticalLayout
{
    private List<Model> models;
    private Model selectedModel;

    private List<ModelsNavbarItem> modelsNavbarItems = new ArrayList<>();
    private Select<String> categorySelect;
	private VerticalLayout rowsWrapper;
	private Consumer<Model> selectModelConsumer;
    private ModelsNavbarItem currentModelNavItem;
    private UploadModelButton newModelButton;
    private SegmentIntegrator segmentIntegrator;

    private ModelDAO modelDAO;
    private Supplier<Optional<UI>> getUISupplier;

    public ModelsNavbar(Supplier<Optional<UI>> getUISupplier, ModelDAO modelDAO, Model selectedModel, List<Model> models, Consumer<Model> selectModelConsumer, SegmentIntegrator segmentIntegrator)
	{
 	    this.getUISupplier = getUISupplier;
	    this.modelDAO = modelDAO;
	    this.models = models;
	    this.selectedModel = selectedModel;
        this.selectModelConsumer = selectModelConsumer;
        this.segmentIntegrator = segmentIntegrator;

        rowsWrapper = new VerticalLayout();
		rowsWrapper.addClassName("models-navbar-items");
		rowsWrapper.setPadding(false);
		rowsWrapper.setSpacing(false);
		
        newModelButton = new UploadModelButton(models.get(0).getProjectId());
        
        createCategorySelect();

		setPadding(false);
        setSpacing(false);
        setWidth("auto");
        add(newModelButton);
        add(categorySelect);
		add(rowsWrapper);
		addClassName("models-navbar");
        addModelsToNavbar();
	}

    public List<Model> getModels() {
        return models;
    }

    private void createCategorySelect() {
        String activeLabel = "Active";
        String archivedLabel = "Archived";
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
        if (selectedModel.isArchived()) {
            categorySelect.setValue(archivedLabel);
        } else {
            categorySelect.setValue(activeLabel);
        }
    }

    private void addModelsToNavbar() {
		rowsWrapper.removeAll();
		modelsNavbarItems.clear();
        models.sort(Comparator.comparing(Model::getDateCreated, Comparator.reverseOrder()));
		
		models.stream()
			.forEach(model -> {
                ModelsNavbarItem navBarItem = createModelsNavbarItem(model);
                modelsNavbarItems.add(navBarItem);
				if (model.equals(selectedModel)) {
					navBarItem.setAsCurrent();
					currentModelNavItem = navBarItem;
				}
				rowsWrapper.add(navBarItem);
		});
	}

    private ModelsNavbarItem createModelsNavbarItem(Model model) {
        return new ModelsNavbarItem(this, getUISupplier, modelDAO, model, selectModelConsumer, segmentIntegrator);
    }

    public void setCurrentModel(Model newCurrentModel) {
	    if (currentModelNavItem != null) {
	        currentModelNavItem.removeAsCurrent();
	    }
		modelsNavbarItems.stream().forEach(modelsNavbarItem -> {
			if (modelsNavbarItem.getItemModel().equals(newCurrentModel)) {
				currentModelNavItem = modelsNavbarItem;
			}
        });
        selectedModel = newCurrentModel;
    }
}
