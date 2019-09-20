package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;

import java.util.Arrays;
import java.util.List;

public class NewProjectStatusWizardPanel extends VerticalLayout
{
	private Tab createANewProjectLabel = new Tab("Create A New Project");
	private Tab pathmindHelperLabel = new Tab("Pathmind Helper");
	private Tab uploadModelLabel = new Tab("Upload Model");
	private Tab modelDetailsLabel = new Tab("Model Details");

	private List<Tab> steps = Arrays.asList(
			createANewProjectLabel,
			pathmindHelperLabel,
			uploadModelLabel,
			modelDetailsLabel);

	public NewProjectStatusWizardPanel()
	{
		add(getStatusBar());

		setWidthFull();
		setMargin(false);

		setCreateANewProject();
	}

	private Tabs getStatusBar() {
		Tabs statusBar = new Tabs(steps.toArray(new Tab[0]));
		statusBar.setWidthFull();
		statusBar.addThemeVariants(TabsVariant.LUMO_SMALL, TabsVariant.LUMO_EQUAL_WIDTH_TABS, TabsVariant.LUMO_CENTERED);
		steps.forEach(it -> {
			it.setEnabled(false);
			it.setVisible(true);
			it.getStyle().set("color", "inherit");
		});
		return statusBar;
	}

	public void setCreateANewProject() {
		setStep(createANewProjectLabel);
	}

	public void setPathmindHelper() {
		setStep(pathmindHelperLabel);
	}

	public void setUploadModel() {
		setStep(uploadModelLabel);
	}

	public void setModelDetails() {
		setStep(modelDetailsLabel);
	}

	private void setStep(Tab activeButton) {
		steps.forEach(it -> {
			it.setSelected(it.equals(activeButton));
		});
	}
}
