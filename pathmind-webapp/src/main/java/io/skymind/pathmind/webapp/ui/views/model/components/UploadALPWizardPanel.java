package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

@Slf4j
public class UploadALPWizardPanel extends VerticalLayout {
    private final Model model;
    private final int maxFileSize;

    private VerticalLayout uploadModelPanel;
    private Upload upload;

    private Button nextStepButton;

    public UploadALPWizardPanel(Model model, boolean isResumeUpload, boolean isValidModel, int maxFileSize) {
        this.model = model;
        this.maxFileSize = maxFileSize;

        nextStepButton = UploadModelView.createNextStepButton();
        nextStepButton.setEnabled(isValidModel);

        setupLayout(isResumeUpload);
        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupLayout(boolean isResumeUpload) {
        setupUploadPanel();
        List<Component> items = new ArrayList<>(Arrays.asList(
                WrapperUtils.wrapWidthFullBetweenHorizontal(
                        LabelFactory.createLabel("Upload alp file", NO_TOP_MARGIN_LABEL),
                        new TagLabel("Optional")
                ),
                GuiUtils.getFullWidthHr()));
        if (!isResumeUpload) {
            Icon checkmarkIcon = new Icon(VaadinIcon.CHECK_CIRCLE);
            checkmarkIcon.setColor("var(--pm-friendly-color)");

            HorizontalLayout successMessage = WrapperUtils.wrapWidthFullHorizontal(
                    checkmarkIcon,
                    WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                            LabelFactory.createLabel("Your model was successfully uploaded!", BOLD_LABEL))
            );
            items.add(successMessage);
        }
        items.addAll(Arrays.asList(
                new UploadAlpInstructions(),
                uploadModelPanel,
                WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton)));

        add(items.toArray(new Component[0]));
    }

    public void setIsValidModel(boolean isValidModel) {
        nextStepButton.setEnabled(isValidModel);
    }

    private void setupUploadPanel() {
        upload = new Upload();

        upload.setReceiver(new MemoryBuffer());

        Button uploadButton = new Button(VaadinIcon.UPLOAD.create());
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uploadButton.setText("Upload alp file");

        upload.setUploadButton(uploadButton);

        upload.setMaxFileSize(maxFileSize);

        addUploadsFinishedListener();
        addUploadRemoveFileListener();

        uploadModelPanel = WrapperUtils.wrapWidthFullCenterVertical(upload);
    }

    public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        nextStepButton.addClickListener(listener);
    }

    private void addUploadsFinishedListener() {
        upload.addSucceededListener((evt) -> {
            try {
                Receiver receiver = upload.getReceiver();
                MemoryBuffer buffer = (MemoryBuffer) receiver;
                model.setAlpFile(buffer.getInputStream().readAllBytes());
                log.info("ALP Upload completed");
            } catch (IOException e) {
                // TODO -> We need to do something if this fails.
                log.error("ALP Upload failed", e);
            }
        });
    }

    // Currently the only way this seems possible is by listening for the DOM event
    // as explained at: https://vaadin.com/forum/thread/17336034/remove-file-uploaded-vaadin-upload
    private void addUploadRemoveFileListener() {
        upload.getElement().addEventListener("file-remove", new DomEventListener() {
            @Override
            public void handleEvent(DomEvent event) {
                model.setAlpFile(new byte[0]);
            }
        });
    }
}
