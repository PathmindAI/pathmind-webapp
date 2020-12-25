package io.skymind.pathmind.webapp.ui.components.alp;

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

public class DownloadModelAlpLink extends Anchor {

    private String projectName;
    private long modelId;
    private String modelName;
    private String modelPackageName;

    // TODO -> FIONNA -> (FROM STEPH) Quick temporary implementation for the experiment view only.
    private ModelService modelService;
    private SegmentIntegrator segmentIntegrator;
    private boolean buttonMode;
    private boolean isAlreadyRendered;

    public DownloadModelAlpLink(String projectName, Model model, ModelService modelService, SegmentIntegrator segmentIntegrator) {
        this(projectName, model, modelService, segmentIntegrator, false);
    }

    public DownloadModelAlpLink(String projectName, Model model, ModelService modelService, SegmentIntegrator segmentIntegrator, boolean buttonMode) {
        this.modelId = model.getId();
        this.modelName = model.getName();
        this.modelPackageName = model.getPackageName();
        this.projectName = projectName;

        Button downloadButton = new Button("Model ALP", new Icon(VaadinIcon.DOWNLOAD_ALT));
        if (!buttonMode) {
            downloadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        }

        getElement().setAttribute("download", true);
        getElement().addEventListener("click", event -> {
            segmentIntegrator.downloadedALP();
        });
        addClassName("download-alp-link");
        if (modelService.hasModelAlp(modelId)) {
            modelService.getModelAlp(modelId).ifPresent(resource -> {
                getElement().setAttribute("href", getResourceStream(resource));
            });
            add(downloadButton);
        } else {
            setVisible(false);
        }
    }

    // TODO -> FIONNA -> I just quickly implemented this but the goal is that we don't have the experiment at component load time (for example we don't have
    // a comparison experiment on the experimentView) so we can't know the project or model. This quickly resolves it but it's not pretty so either you or I
    // will have to refactor it. It was done quickly to get the titlebar on the experimentView working for the experiment comparison feature (2149).
    public DownloadModelAlpLink(ModelService modelService, SegmentIntegrator segmentIntegrator, boolean buttonMode) {
        this.modelService = modelService;
        this.segmentIntegrator = segmentIntegrator;
        this.buttonMode = buttonMode;
    }

    // TODO -> FIONNA -> (FROM STEPH) I just quickly implemented this but the goal is that we don't have the experiment at component load time (for example we don't have
    // a comparison experiment on the experimentView) so we can't know the project or model. This quickly resolves it but it's not pretty so either you or I
    // will have to refactor it. It was done quickly to get the titlebar on the experimentView working for the experiment comparison feature (2149).
    public void setExperiment(Experiment experiment) {
        this.modelId = experiment.getModel().getId();
        this.modelName = experiment.getModel().getName();
        this.modelPackageName = experiment.getModel().getPackageName();
        this.projectName = experiment.getProject().getName();

        if(isAlreadyRendered) {
            return;
        }

        // TODO -> FIONNA -> (FROM STEPH) The logic I implemented here is pretty gross but it works as a temporary solution until we have the time to clean up
        //  this component. I would do it but I don't fully know how it's used across the application.
        Button downloadButton = new Button("Model ALP", new Icon(VaadinIcon.DOWNLOAD_ALT));
        if (!buttonMode) {
            downloadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        }

        getElement().setAttribute("download", true);
        getElement().addEventListener("click", event -> {
            segmentIntegrator.downloadedALP();
        });
        addClassName("download-alp-link");
        if (modelService.hasModelAlp(modelId)) {
            modelService.getModelAlp(modelId).ifPresent(resource -> {
                getElement().setAttribute("href", getResourceStream(resource));
            });
            add(downloadButton);
        } else {
            setVisible(false);
        }

        isAlreadyRendered = true;
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

