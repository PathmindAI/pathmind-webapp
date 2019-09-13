package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;

import java.util.Arrays;
import java.util.List;

public class NewProjectStatusWizardPanel extends VerticalLayout
{
	private Label createANewProjectLabel = new Label("Create A New Project");
	private Label pathmindHelperLabel = new Label("Pathmind Helper");
	private Label uploadModelLabel = new Label("Upload Model");
	private Label modelDetailsLabel = new Label("Model Details");

	private List<Label> steps = Arrays.asList(
			createANewProjectLabel,
			pathmindHelperLabel,
			uploadModelLabel,
			modelDetailsLabel);

	public NewProjectStatusWizardPanel()
	{
		add(getStatusBar(), GuiUtils.getFullWidthHr());

		// TODO -> All this should be done in proper CSS styles.
		steps.stream().forEach(label -> label.getStyle().set("font-size", "11px"));

		setWidthFull();
		setMargin(false);

		setCreateANewProject();
	}

	private HorizontalLayout getStatusBar() {
		HorizontalLayout statusBar = WrapperUtils.wrapFormCenterHorizontal(steps.stream().toArray(Component[]::new));
		statusBar.setWidthFull();
		statusBar.setJustifyContentMode(JustifyContentMode.EVENLY);
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

	private void setStep(Label activeButton) {
		steps.stream().forEach(label -> {
			label.getStyle().set("font-weight", label.equals(activeButton) ? "bold" : "normal");
		});
	}
}
