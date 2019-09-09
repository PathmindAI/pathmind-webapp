package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadModelWizardPanel extends VerticalLayout
{
	private static Logger log = LogManager.getLogger(UploadModelWizardPanel.class);

	private Label projectNameLabel = new Label();

	private Button checkYourModelButton = new Button("Check Your model");

	private VerticalLayout uploadModelPanel;
	private Upload upload;

	private ProgressBar fileCheckProgressBar = new ProgressBar();
	private VerticalLayout fileCheckPanel;

	public UploadModelWizardPanel()
	{
		projectNameLabel.getStyle().set("margin-top", "0px");

		setupUploadPanel();
		setupFileCheckPanel();

		add(getProjectH3(),
				projectNameLabel,
				GuiUtils.getFullWidthHr(),
				getInstructionsDiv(),
				uploadModelPanel,
				fileCheckPanel,
				WrapperUtils.wrapCenterFullWidthHorizontal(checkYourModelButton));

		checkYourModelButton.setVisible(false);
		fileCheckPanel.setVisible(false);

		getStyle().set("border", "1px solid #ccc");
		setWidthFull();
	}

	private void setupFileCheckPanel() {
		fileCheckPanel = WrapperUtils.wrapCenterAlignmentFullVertical(
				fileCheckProgressBar,
				WrapperUtils.wrapCenterFullWidthHorizontal(new Label("File check...")));
	}

	private void setupUploadPanel()
	{
		MemoryBuffer buffer = new MemoryBuffer();
		upload = new Upload(buffer);

		upload.addSucceededListener(event -> {
			// TODO -> Implement
			log.info("Upload completed");
			checkYourModelButton.setVisible(true);
		});

		uploadModelPanel = WrapperUtils.wrapCenterAlignmentFullVertical(upload);
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		checkYourModelButton.addClickListener(listener);
	}

	private H3 getProjectH3() {
		H3 h3 = new H3("Project");
		h3.getStyle().set("margin-bottom", "0px");
		return h3;
	}

	// TODO -> Move CSS to styles.css
	private Div getInstructionsDiv() {
		Div div = new Div();
		div.setWidthFull();
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li>Make sure you have <strong>PathmindHelper</strong> installed in your model.</li>" +
					"<li>Export your model as a standalone Java application.</li>" +
					"<li>Open the .jar file as a zip</li>" +
					"<li>Create a new zip file that contains:</li>" +
						"<ul>" +
							"<li>model.zip</li>" +
							"<li>the \"db\" folder if needed</li>" +
							"<li>custom libraries from the \"lib\" folder if needed.</li>" +
						"</ul>" +
					"<li>Upload the new zip file below." +
				"</ol>");
		return div;
	}

	public void setProjectName(String name) {
		projectNameLabel.setText(name);
	}

	public void showFileCheckPanel() {
		upload.setVisible(false);
		fileCheckPanel.setVisible(true);
		checkYourModelButton.setVisible(false);
	}

	public void setFileCheckStatusProgressBarValue(double value) {
		fileCheckProgressBar.setValue(value);
	}
}
