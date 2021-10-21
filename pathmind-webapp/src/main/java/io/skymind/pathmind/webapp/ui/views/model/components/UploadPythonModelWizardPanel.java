package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindModelUploader;
import io.skymind.pathmind.webapp.ui.components.atoms.ToggleButton;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadMode;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import lombok.extern.slf4j.Slf4j;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.ERROR_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

@Slf4j
public class UploadPythonModelWizardPanel extends VerticalLayout {
    private final Model model;

    private PathmindModelUploader upload;

    private ProgressBar fileCheckProgressBar = new ProgressBar();
    private VerticalLayout fileCheckPanel;

    private HorizontalLayout checkingModelComponent;
    private Span errorMessage;

    private UploadMode mode;
    private Consumer<Collection<String>> uploadFailedConsumer;
    private String apiUrl;
    private String apiToken;

    private Supplier<Optional<UI>> getUISupplier;
    public UploadPythonModelWizardPanel(Model model, UploadMode mode, Supplier<Optional<UI>> getUISupplier, String apiUrl, String apiToken) {
        this.model = model;
        this.mode = mode;
        this.getUISupplier = getUISupplier;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;

        setupLayout();
        setPadding(false);
        setSpacing(false);
    }

    private void setupLayout() {
        setupFileCheckPanel();

        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
                    LabelFactory.createLabel("Upload Model", NO_TOP_MARGIN_LABEL),
                    getToggleModelTypeButton()
            ),
                GuiUtils.getFullWidthHr(),
                getInstructionsDiv(),
                fileCheckPanel);

        fileCheckPanel.setVisible(false);
    }

    private ToggleButton getToggleModelTypeButton() {
        ToggleButton toggleModelTypeButton = new ToggleButton("AnyLogic", "Python");
        toggleModelTypeButton.setToggleButtonState(false);
        toggleModelTypeButton.setToggleCallback(() -> {
            boolean newButtonState = !toggleModelTypeButton.getToggleButtonState();
            toggleModelTypeButton.setToggleButtonState(newButtonState);
            if (newButtonState) {
                // AnyLogic model
                getUISupplier.get().ifPresent(ui ->
                    ui.navigate(UploadModelView.class, ""+model.getProjectId()));
            } else {
                // Python model
            }
        });
        return toggleModelTypeButton;
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

    public void addFileUploadFailedListener(Consumer<Collection<String>> consumer) {
        uploadFailedConsumer = consumer;
    }

    private UploadPythonModelInstructions getInstructionsDiv() {
        UploadPythonModelInstructions uploadModelInstructions = new UploadPythonModelInstructions(apiUrl, apiToken);
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
