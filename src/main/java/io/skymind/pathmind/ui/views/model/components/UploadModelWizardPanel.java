package io.skymind.pathmind.ui.views.model.components;

import java.io.IOException;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.components.PathmindModelUploader;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadModelWizardPanel extends VerticalLayout
{
	private final Model model;

	private Label projectNameLabel;

	private VerticalLayout uploadModelPanel;
	private PathmindModelUploader upload;

	private ProgressBar fileCheckProgressBar = new ProgressBar();
	private VerticalLayout fileCheckPanel;
	
	private Command fileCheckerCommand;

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
				fileCheckPanel);

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
		// TODO: Check from client side whether folder upload is supported or not
		boolean isFolderUploadSupported = true;
		upload = new PathmindModelUploader(isFolderUploadSupported);

		// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/123
//		upload.setMaxFileSize(PathmindConstants.MAX_UPLOAD_FILE_SIZE);
//		upload.setAcceptedFileTypes("application/zip");
//		upload.addFailedListener(event -> log.error("ERROR " + event.getReason().getMessage(), e.getReason().getMessage()));

		addUploadSucceedListener(upload.getReceiver(), isFolderUploadSupported);
		addUploadRemoveFileListener();

		uploadModelPanel = WrapperUtils.wrapWidthFullCenterVertical(upload);
	}

	private void addUploadSucceedListener(Receiver receiver, boolean isFolderUploadSupported) {
		upload.addAllFilesUploadedListener(() -> {
			try {
				if (isFolderUploadSupported) {
					MultiFileMemoryBuffer buffer = MultiFileMemoryBuffer.class.cast(receiver);
					model.setFile(UploadUtils.createZipFileFromBuffer(buffer));
				} else {
					MemoryBuffer buffer = MemoryBuffer.class.cast(receiver);
					model.setFile(buffer.getInputStream().readAllBytes());
				}
				fileCheckerCommand.execute();
				log.info("Upload completed");
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

	public void addFileUploadCompletedListener(Command command) {
		fileCheckerCommand = command;
	}

	// TODO -> CSS -> Move CSS to styles.css
	private Div getInstructionsDiv() {
		Div div = new Div();
		div.setWidthFull();
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li>Make sure you have <a href=\"https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper/\" target=\"_blank\">Pathmind Helper</a> installed in your model.</li>" +
					"<li><a href=\"https://help.anylogic.com/topic/com.anylogic.help/html/standalone/Export_Java_Application.html\" target=\"_blank\">Export your model as a standalone Java application.</a></li>" +
					"<li>Click Upload files button.</li>" +
					"<li>Select the exported folder.</li>" +
				"</ol>");
		return div;
	}

	public void setProjectName(String name) {
		projectNameLabel.setText(name);
	}

	public void showFileCheckPanel() {
		upload.setVisible(false);
		fileCheckPanel.setVisible(true);
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
