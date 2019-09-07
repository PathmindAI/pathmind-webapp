package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.UIConstants;
import io.skymind.pathmind.ui.utils.WrapperUtils;

import java.util.Arrays;
import java.util.List;

public class NewProjectWizardStatusPanel extends VerticalLayout
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

	public NewProjectWizardStatusPanel()
	{
		HorizontalLayout statusBar = WrapperUtils.wrapCenteredFormHorizontal(steps.stream().toArray(Component[]::new));
		statusBar.setJustifyContentMode(JustifyContentMode.EVENLY);

		// TODO -> All this should be done in proper CSS styles.
		steps.stream().forEach(label -> label.getStyle().set("font-size", "11px"));

		add(WrapperUtils.wrapCenteredFormVertical(
					statusBar,
					GuiUtils.getHr(UIConstants.CENTERED_FORM_WIDTH)));

		setWidth(UIConstants.CENTERED_FORM_WIDTH);

		setCreateANewProject();
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
