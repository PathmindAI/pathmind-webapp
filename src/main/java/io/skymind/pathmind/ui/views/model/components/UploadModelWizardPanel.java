package io.skymind.pathmind.ui.views.model.components;

import java.io.IOException;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadModelWizardPanel extends VerticalLayout
{
	private final Model model;

	private Label projectNameLabel;

	private Button checkYourModelButton = new Button("Check Your model");

	private VerticalLayout uploadModelPanel;
	private Upload upload;

	private ProgressBar fileCheckProgressBar = new ProgressBar();
	private VerticalLayout fileCheckPanel;

	private Text errorText;

	public UploadModelWizardPanel(Model model)
	{
		this.model = model;

		projectNameLabel = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);
		projectNameLabel.getStyle().set("margin-top", "0px");
		
		setupUploadPanel();
		setupFileCheckPanel();

		add(LabelFactory.createLabel("Project", CssMindPathStyles.SECTION_TITLE_LABEL),
				projectNameLabel,
				GuiUtils.getFullWidthHr(),
				getInstructionsDiv(),
				uploadModelPanel,
				fileCheckPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(checkYourModelButton));

		checkYourModelButton.setVisible(false);
		fileCheckPanel.setVisible(false);

		setClassName("view-section"); // adds the white 'panel' style with rounded corners

		setWidthFull();
	}

	private void setupFileCheckPanel() {
		errorText = new Text("");
		fileCheckPanel = WrapperUtils.wrapWidthFullCenterVertical(
				fileCheckProgressBar,
				WrapperUtils.wrapWidthFullCenterHorizontal(new Label("Checking your model...")),
				errorText);
	}

	private void setupUploadPanel()
	{
		MemoryBuffer buffer = new MemoryBuffer();

		upload = new Upload(buffer);

		// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/123
//		upload.setMaxFileSize(PathmindConstants.MAX_UPLOAD_FILE_SIZE);
//		upload.setAcceptedFileTypes("application/zip");
//		upload.addFailedListener(event -> log.error("ERROR " + event.getReason().getMessage(), e.getReason().getMessage()));

		addUploadSucceedListener(buffer);
		addUploadRemoveFileListener();

		uploadModelPanel = WrapperUtils.wrapWidthFullCenterVertical(upload);
	}

	private void addUploadSucceedListener(MemoryBuffer buffer) {
		upload.addSucceededListener(event -> {
			try {
				final byte[] bytes = buffer.getInputStream().readAllBytes();
				model.setFile(bytes);

				log.info("Upload completed");
				checkYourModelButton.setVisible(true);
				checkYourModelButton.click(); // Rigging this to automatically run the file check
			} catch (IOException e) {
				// TODO -> We need to do something if this fails.
				log.error("Upload failed", e);
			}
		});
	}

	// Currently the only way this seems possible is by listening for the DOM event
	// as explained at: https://vaadin.com/forum/thread/17336034/remove-file-uploaded-vaadin-upload
	private void addUploadRemoveFileListener() {
		upload.getElement().addEventListener("file-remove", new DomEventListener() {
			@Override
			public void handleEvent(DomEvent event) {
				clearError();
			}
		});
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		checkYourModelButton.addClickListener(listener);
	}

	// TODO -> CSS -> Move CSS to styles.css
	private Div getInstructionsDiv() {
		Div div = new Div();
		div.setWidthFull();
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li>Make sure you have <a href=\"https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper/\" target=\"_blank\">Pathmind Helper</a> installed in your model.</li>" +
					"<li><a href=\"https://help.anylogic.com/topic/com.anylogic.help/html/standalone/Export_Java_Application.html\" target=\"_blank\">Export your model as a standalone Java application.</a></li>" +
					"<li>Open the exported folder.</li>" +
					"<li>Create a zip file that contains:</li>" +
						"<ul>" +
							"<li>model.jar</li>" +
							"<li>the \"database\" folder if needed</li>" +
							"<li>custom libraries from the \"lib\" folder if needed. (This is uncommon)</li>" +
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
		setFileCheckStatusProgressBarValue(0);
	}

	public void setFileCheckStatusProgressBarValue(double value) {
		if(value == 0.0){
			fileCheckProgressBar.removeThemeVariants(ProgressBarVariant.LUMO_ERROR);
			this.errorText.setText("");
		}
		fileCheckProgressBar.setValue(value);
	}

	public void setError(String error) {
		fileCheckProgressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
		this.errorText.setText(error);
		upload.setVisible(true);
	}

	public void clearError() {
		fileCheckProgressBar.setValue(0);
		errorText.setText("");
		fileCheckPanel.setVisible(false);
		upload.setVisible(true);
	}
}
