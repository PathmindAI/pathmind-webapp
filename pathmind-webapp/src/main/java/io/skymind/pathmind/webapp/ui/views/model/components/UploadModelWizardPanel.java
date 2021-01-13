package io.skymind.pathmind.webapp.ui.views.model.components;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.dom.DomEventListener;
import io.skymind.pathmind.services.model.analyze.ModelBytes;
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

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

@Slf4j
public class UploadModelWizardPanel extends VerticalLayout {
    private final Model model;
    private final int maxFileSize;

    private VerticalLayout uploadModelPanel;
    private PathmindModelUploader upload;

    private ProgressBar fileCheckProgressBar = new ProgressBar();
    private VerticalLayout fileCheckPanel;

    private Consumer<ModelBytes> checkModelConsumer;

    private HorizontalLayout checkingModelComponent;
    private Span errorMessage;
    private UploadModeSwitcherButton uploadModeSwitcher;

    private UploadMode mode;
    private Consumer<Collection<String>> uploadFailedConsumer;

    public UploadModelWizardPanel(Model model, UploadMode mode, int maxFileSize) {
        this.model = model;
        this.mode = mode;
        this.maxFileSize = maxFileSize;

        setupLayout();
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
        errorMessage = LabelFactory.createLabel("", ERROR_LABEL);
        checkingModelComponent = WrapperUtils
                .wrapWidthFullCenterHorizontal(LabelFactory.createLabel("Checking your model..."));
        fileCheckPanel = WrapperUtils.wrapWidthFullCenterVertical(
                fileCheckProgressBar,
                checkingModelComponent,
                errorMessage);
        fileCheckPanel.setPadding(false);
        fileCheckPanel.addClassName("file-check-panel");
    }

    private void setupUploadPanel() {
        upload = new PathmindModelUploader(mode);

        upload.setMaxFileSize(maxFileSize);

        addUploadsFinishedListener();
        addUploadRemoveFileListener();

        uploadModelPanel = WrapperUtils.wrapWidthFullCenterVertical(upload);
        uploadModelPanel.setPadding(false);
    }

    private void addUploadsFinishedListener() {
        upload.addAllFilesUploadedListener((errors) -> {
            log.info("Upload a model for project {}", model.getProjectId());

            if (errors.size() > 0) {
                uploadFailedConsumer.accept(errors);
                return;
            }
            try {
                Receiver receiver = upload.getReceiver();
                byte[] data;
                // In folder upload mode, receiver is MultiFileMemoryBuffer, so a zip file should be created
                if (receiver instanceof MultiFileMemoryBuffer) {
                    MultiFileMemoryBuffer buffer = (MultiFileMemoryBuffer) receiver;
                    data = UploadUtils.createZipFileFromBuffer(buffer);
                } else {
                    MemoryBuffer buffer = (MemoryBuffer) receiver;
                    data = buffer.getInputStream().readAllBytes();
                }
                ModelBytes modelBytes = ModelBytes.of(data);
                checkModelConsumer.accept(modelBytes);
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
        upload.getElement().addEventListener("file-remove", (DomEventListener) event -> clearError());
    }

    public void addFileUploadCompletedListener(Consumer<ModelBytes> checkModelConsumer) {
        this.checkModelConsumer = checkModelConsumer;
    }

    public void addFileUploadFailedListener(Consumer<Collection<String>> consumer) {
        uploadFailedConsumer = consumer;
    }

    private UploadModelInstructions getInstructionsDiv() {
        UploadModelInstructions uploadModelInstructions = new UploadModelInstructions();
        upload.isFolderUploadSupported(isFolderUploadSupported -> {
            uploadModelInstructions.setIsZip(!(mode == UploadMode.FOLDER && isFolderUploadSupported));
        });
        return uploadModelInstructions;
    }

    public void showFileCheckPanel() {
        upload.setVisible(false);
        fileCheckPanel.setVisible(true);
        setFileCheckStatusProgressBarValue(0);
    }

    public void setFileCheckStatusProgressBarValue(double value) {
        if (value == 0.0) {
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
        errorMessage.getElement().executeJs("this.innerHTML = $0", error);
        upload.setVisible(true);
    }

    public void clearError() {
        fileCheckProgressBar.setValue(0);
        errorMessage.setText("");
        fileCheckPanel.setVisible(false);
        upload.setVisible(true);
    }
}
