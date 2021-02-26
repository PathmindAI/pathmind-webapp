package io.skymind.pathmind.webapp.ui.components;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.StreamResource;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import org.apache.commons.lang3.ObjectUtils;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;
import static io.skymind.pathmind.shared.utils.PathmindStringUtils.toCamelCase;

public class DownloadModelLink extends Anchor {

    private String projectName;
    private long modelId;
    private String modelName;
    private String modelPackageName;

    private ModelService modelService;
    private SegmentIntegrator segmentIntegrator;
    private boolean buttonMode;
    private boolean isPythonModel = false;
    private boolean isAlreadyRendered;

    public DownloadModelLink(String projectName, Model model, ModelService modelService, SegmentIntegrator segmentIntegrator, boolean buttonMode, boolean isPythonModel) {
        this.projectName = projectName;
        this.modelId = model.getId();
        this.modelName = model.getName();
        this.modelPackageName = model.getPackageName();
        this.modelService = modelService;
        this.segmentIntegrator = segmentIntegrator;
        this.buttonMode = buttonMode;
        this.isPythonModel = isPythonModel;

        setupButton();
    }

    public DownloadModelLink(ModelService modelService, SegmentIntegrator segmentIntegrator, boolean buttonMode, boolean isPythonModel) {
        this.modelService = modelService;
        this.segmentIntegrator = segmentIntegrator;
        this.buttonMode = buttonMode;
        this.isPythonModel = isPythonModel;
    }

    public void setExperiment(Experiment experiment) {
        this.modelId = experiment.getModel().getId();
        this.modelName = experiment.getModel().getName();
        this.modelPackageName = experiment.getModel().getPackageName();
        this.projectName = experiment.getProject().getName();

        if (isAlreadyRendered) {
            return;
        }

        setupButton();

        isAlreadyRendered = true;
    }

    private void setupButton() {
        Button downloadButton = new Button("Model ALP", new Icon(VaadinIcon.DOWNLOAD_ALT));
        if (!buttonMode) {
            downloadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        }

        getElement().setAttribute("download", true);
        getElement().addEventListener("click", event -> {
            segmentIntegrator.downloadedALP();
        });
        addClassName("download-alp-link");
        if (!isPythonModel && modelService.hasModelAlp(modelId)) {
            modelService.getModelAlp(modelId).ifPresent(resource -> {
                getElement().setAttribute("href", getResourceStream(resource));
            });
            add(downloadButton);
        } else if (isPythonModel) {
            getElement().setAttribute("href", "pythonURL"); // TODO: to be changed when we actually have it
            downloadButton.setText("Model");
            add(downloadButton);
        } else {
            setVisible(false);
        }
    }

    private String generateFileName() {
        if (!ObjectUtils.allNotNull(projectName, modelName, modelPackageName)) {
            return "-";
        }
        return removeInvalidChars(String.format("%s-M%s-%s.alp", toCamelCase(projectName), modelName, modelPackageName));
    }

    private StreamResource getResourceStream(byte[] resource) {
        return new StreamResource(generateFileName(), () -> new ByteArrayInputStream(resource));
    }
}

