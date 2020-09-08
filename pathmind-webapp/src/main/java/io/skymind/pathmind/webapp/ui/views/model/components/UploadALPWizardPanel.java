package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadMode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

@Slf4j
public class UploadALPWizardPanel extends VerticalLayout {
    private final Model model;
    private final int maxFileSize;

    private VerticalLayout uploadModelPanel;
    private Upload upload;

    private UploadMode mode;
    private Consumer<Collection<String>> uploadFailedConsumer;
    private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));

    public UploadALPWizardPanel(Model model, UploadMode mode, int maxFileSize) {
        this.model = model;
        this.mode = mode;
        this.maxFileSize = maxFileSize;

        // TODO: move nextStepButton to its own component, the code below is being copied in all wizard panels
        nextStepButton.setIconAfterText(true);
        nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setupLayout();
        setWidthFull();
        setPadding(false);
        setSpacing(false);

    }

    private void setupLayout() {
        setupUploadPanel();

        add(LabelFactory.createLabel("Upload ALP file", NO_TOP_MARGIN_LABEL),
                GuiUtils.getFullWidthHr(),
                getInstructionsDiv(),
                uploadModelPanel,
                WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton)
        );
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

    public void addFileUploadFailedListener(Consumer<Collection<String>> consumer) {
        uploadFailedConsumer = consumer;
    }

    private Div getInstructionsDiv() {
        Div div = new Div();
        div.setWidthFull();
        setInstructionsForUploadDiv(div);
        return div;
    }


    private void setInstructionsForUploadDiv(Div div) {
        div.getElement().setProperty("innerHTML",
                "<ol>" +
                        "<li>Here we'll add</li>" +
                        "<li>the</li>" +
                        "<li>relevant info</li>" +
                        "</ol>");
    }
}
