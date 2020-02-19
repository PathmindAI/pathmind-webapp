package io.skymind.pathmind.ui.views.model.components;

import static io.skymind.pathmind.ui.constants.CssMindPathStyles.NO_TOP_MARGIN_LABEL;
import static io.skymind.pathmind.ui.constants.CssMindPathStyles.SECTION_SUBTITLE_LABEL;

import java.io.IOException;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
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

	private Span projectNameLabel;

	private VerticalLayout uploadModelPanel;
	private PathmindModelUploader upload;

	private ProgressBar fileCheckProgressBar = new ProgressBar();
	private VerticalLayout fileCheckPanel;
	
	private Command fileCheckerCommand;

	private Text errorText;
	private UploadModeSwitcherButton uploadModeSwitcher;
	
	private boolean isFolderUploadMode = true;

	public UploadModelWizardPanel(Model model)
	{
		this.model = model;

		projectNameLabel = LabelFactory.createLabel("", SECTION_SUBTITLE_LABEL, NO_TOP_MARGIN_LABEL);
		
		setupLayout();

		setClassName("view-section"); // adds the white 'panel' style with rounded corners

		setWidthFull();
	}
	
	private void setupLayout() {
		setupUploadPanel(isFolderUploadMode);
		setupFileCheckPanel();
		setupUploadModeSwitchButton();
		add(LabelFactory.createLabel("Project", CssMindPathStyles.SECTION_TITLE_LABEL),
				projectNameLabel,
				GuiUtils.getFullWidthHr(),
				getInstructionsDiv(isFolderUploadMode),
				uploadModelPanel,
				fileCheckPanel,
				WrapperUtils.wrapWidthFullCenterHorizontal(uploadModeSwitcher));

		fileCheckPanel.setVisible(false);
	}

	private void setupUploadModeSwitchButton() {
		uploadModeSwitcher = new UploadModeSwitcherButton(isFolderUploadMode, () -> switchUploadMode());
		upload.isFolderUploadSupported(isFolderUploadSupported -> {
			uploadModeSwitcher.setVisible(isFolderUploadSupported);
		});
	}
	

	private void switchUploadMode() {
		isFolderUploadMode = !isFolderUploadMode;
		
		removeAll();
		setupLayout();
	}

	private void setupFileCheckPanel() {
		errorText = new Text("");
		fileCheckPanel = WrapperUtils.wrapWidthFullCenterVertical(
				fileCheckProgressBar,
				WrapperUtils.wrapWidthFullCenterHorizontal(LabelFactory.createLabel("Checking your model...")),
				errorText);
	}

	private void setupUploadPanel(boolean isFolderUploadMode)
	{
		upload = new PathmindModelUploader(isFolderUploadMode);

		// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/123
//		upload.setMaxFileSize(PathmindConstants.MAX_UPLOAD_FILE_SIZE);
//		upload.setAcceptedFileTypes("application/zip");
//		upload.addFailedListener(event -> log.error("ERROR " + event.getReason().getMessage(), e.getReason().getMessage()));

		addUploadSucceedListener();
		addUploadRemoveFileListener();

		uploadModelPanel = WrapperUtils.wrapWidthFullCenterVertical(upload);
	}

	private void addUploadSucceedListener() {
		upload.addAllFilesUploadedListener(() -> {
			try {
				Receiver receiver = upload.getReceiver();
				// In folder upload mode, receiver is MultiFileMemoryBuffer, so a zip file should be created
				if (MultiFileMemoryBuffer.class.isInstance(receiver)) {
					MultiFileMemoryBuffer buffer = MultiFileMemoryBuffer.class.cast(receiver);
					model.setFile(UploadUtils.createZipFileFromBuffer(buffer));
				} else {
					MemoryBuffer buffer = MemoryBuffer.class.cast(receiver);
					model.setFile(UploadUtils.ensureZipFileStructure(buffer.getInputStream().readAllBytes()));
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
	
	private Div getInstructionsDiv(boolean isFolderUploadMode) {
		Div div = new Div();
		div.setWidthFull();
		upload.isFolderUploadSupported(isFolderUploadSupported -> {
			if (isFolderUploadMode && isFolderUploadSupported) {
				setInstructionsForFolderUploadDiv(div);
			} else {
				setInstructionsForZipUploadDiv(div);
			}
		});
		return div;
	}

	// TODO -> CSS -> Move CSS to styles.css
	private void setInstructionsForFolderUploadDiv(Div div) {
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li>Make sure you have <a href=\"https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper/\" target=\"_blank\">Pathmind Helper</a> installed in your model.</li>" +
					"<li><a href=\"https://help.anylogic.com/topic/com.anylogic.help/html/standalone/Export_Java_Application.html\" target=\"_blank\">Export your model as a standalone Java application.</a></li>" +
					"<li>Click Upload files button.</li>" +
					"<li>Select the exported folder.</li>" +
				"</ol>");
	}
	
	private void setInstructionsForZipUploadDiv(Div div) {
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
