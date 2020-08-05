package io.skymind.pathmind.webapp.ui.views.model.components;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindModelUploader;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadMode;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.model.utils.UploadModelViewNavigationUtils;
import io.skymind.pathmind.webapp.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadModelWizardPanel extends VerticalLayout
{
	private final Model model;
    private final int maxFileSize;

    private VerticalLayout uploadModelPanel;
	private PathmindModelUploader upload;

	private ProgressBar fileCheckProgressBar = new ProgressBar();
	private VerticalLayout fileCheckPanel;
	
	private Command fileCheckerCommand;

	private HorizontalLayout checkingModelComponent;
	private Span errorMessage;
	private UploadModeSwitcherButton uploadModeSwitcher;
	
	private UploadMode mode;
    private Consumer<Collection<String>> uploadFailedConsumer;

    public UploadModelWizardPanel(Model model, UploadMode mode, int maxFileSize)
	{
		this.model = model;
		this.mode = mode;
		this.maxFileSize = maxFileSize;

		setupLayout();
		setWidthFull();
		setPadding(false);
		setSpacing(false);
	}
	
	private void setupLayout() {
		setupUploadPanel();
		setupFileCheckPanel();

		add(LabelFactory.createLabel("Upload Model", NO_TOP_MARGIN_LABEL),
				GuiUtils.getFullWidthHr(),
				getInstructionsDiv(),
				uploadModelPanel,
				fileCheckPanel,
				getUploadModeSwitchButton());

		fileCheckPanel.setVisible(false);
	}

	private HorizontalLayout getUploadModeSwitchButton() {
		HorizontalLayout buttonWrapper = WrapperUtils.wrapWidthFullCenterHorizontal();
		buttonWrapper.getStyle().set("margin", "var(--lumo-space-xxl) 0 calc(-1 * var(--lumo-space-l))");
		uploadModeSwitcher = new UploadModeSwitcherButton(mode, () -> switchUploadMode());
		upload.isFolderUploadSupported(isFolderUploadSupported -> {
			uploadModeSwitcher.setVisible(isFolderUploadSupported);
		});
		buttonWrapper.add(uploadModeSwitcher);
		return buttonWrapper;
	}
	

	private void switchUploadMode() {
		UploadMode switchedMode = mode == UploadMode.FOLDER ? UploadMode.ZIP : UploadMode.FOLDER;
 		getUI().ifPresent(ui -> 
 			ui.navigate(UploadModelView.class, UploadModelViewNavigationUtils.getUploadModelParameters(model.getProjectId(), switchedMode)));
	}

	private void setupFileCheckPanel() {
	    errorMessage = LabelFactory.createLabel("");
		checkingModelComponent = WrapperUtils
				.wrapWidthFullCenterHorizontal(LabelFactory.createLabel("Checking your model..."));
		fileCheckPanel = WrapperUtils.wrapWidthFullCenterVertical(
				fileCheckProgressBar,
				checkingModelComponent,
				errorMessage);
	}

	private void setupUploadPanel()
	{
		upload = new PathmindModelUploader(mode);

		upload.setMaxFileSize(maxFileSize);

		addUploadsFinishedListener();
		addUploadRemoveFileListener();

		uploadModelPanel = WrapperUtils.wrapWidthFullCenterVertical(upload);
	}

	private void addUploadsFinishedListener() {
		upload.addAllFilesUploadedListener((errors) -> {
		    if (errors.size() > 0) {
		        uploadFailedConsumer.accept(errors);
		        return;
            }
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

    public void addFileUploadFailedListener(Consumer<Collection<String>> consumer) {
        uploadFailedConsumer = consumer;
    }

    private Div getInstructionsDiv() {
		Div div = new Div();
		div.setWidthFull();
		upload.isFolderUploadSupported(isFolderUploadSupported -> {
			if (mode == UploadMode.FOLDER && isFolderUploadSupported) {
				setInstructionsForFolderUploadDiv(div);
			} else {
				setInstructionsForZipUploadDiv(div);
			}
		});
		return div;
	}

	private void setInstructionsForFolderUploadDiv(Div div) {
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li><a href=\"https://help.anylogic.com/topic/com.anylogic.help/html/standalone/Export_Java_Application.html\" target=\"_blank\">Export your model as a standalone Java application.</a><br/>(AnyLogic Professional is required)</li>" +
					"<li>Upload the exported folder.</li>" +
				"</ol>");
	}
	
	private void setInstructionsForZipUploadDiv(Div div) {
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li><a href=\"https://help.anylogic.com/topic/com.anylogic.help/html/standalone/Export_Java_Application.html\" target=\"_blank\">Export your model as a standalone Java application.</a><br/>(AnyLogic Professional is required)</li>" +
					"<li>Open the exported folder.</li>" +
					"<li>Create a zip file that contains:</li>" +
						"<ul>" +
							"<li>model.jar</li>" +
							"<li>the \"database\" folder if needed</li>" +
						"</ul>" +
					"<li>Upload the new zip file below." +
				"</ol>");
	}

	public void showFileCheckPanel() {
		upload.setVisible(false);
		fileCheckPanel.setVisible(true);
		setFileCheckStatusProgressBarValue(0);
	}

	public void setFileCheckStatusProgressBarValue(double value) {
		if(value == 0.0){
			fileCheckProgressBar.removeThemeVariants(ProgressBarVariant.LUMO_ERROR);
			this.errorMessage.setText("");
		}
		fileCheckProgressBar.setValue(value);
	}

	public void setError(String error) {
		fileCheckProgressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
		if (checkingModelComponent != null) {
			checkingModelComponent.setVisible(false);
		}
		errorMessage.getElement().setProperty("innerHTML", error);
		upload.setVisible(true);
	}

	public void clearError() {
		fileCheckProgressBar.setValue(0);
		errorMessage.setText("");
		fileCheckPanel.setVisible(false);
		upload.setVisible(true);
	}
}
