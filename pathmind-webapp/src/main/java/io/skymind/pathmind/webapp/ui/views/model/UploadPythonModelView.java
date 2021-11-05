package io.skymind.pathmind.webapp.ui.views.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import io.skymind.pathmind.db.dao.*;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.ModelUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.modelChecker.ModelCheckerService;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.components.UploadPythonModelWizardPanel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.PROJECT_TITLE;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_SUBTITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT;

@Slf4j
@Route(value = Routes.UPLOAD_PYTHON_MODEL, layout = MainLayout.class)
public class UploadPythonModelView extends PathMindDefaultView implements HasUrlParameter<String> {

    private static final int PROJECT_ID_SEGMENT = 0;
    private static final int UPLOAD_MODE_SEGMENT = 1;
    private static final int MODEL_ID_SEGMENT = 2;

    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private ModelService modelService;
    @Autowired
    private UserService userService;
    @Autowired
    private SegmentIntegrator segmentIntegrator;
    @Autowired
    private ModelCheckerService modelCheckerService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSizeAsStr;

    @Value("${pathmind.pathmind-api.url}")
    private String apiUrl;

    private Model model;

    private Binder<Model> modelBinder;

    private UploadPythonModelWizardPanel uploadModelWizardPanel;

    private List<Component> wizardPanels;

    private long projectId;
    private long modelId = -1;
    private long experimentId;
    private String modelNotes;
    private Project project;

    private UploadMode uploadMode;

    public UploadPythonModelView() {
        super();
    }

    protected Component getMainContent() {
        modelBinder = new Binder<>(Model.class);

        uploadModelWizardPanel = new UploadPythonModelWizardPanel(model,
                                        uploadMode,
                                        getUISupplier(),
                                        apiUrl,
                                        userService.getCurrentUser().getApiKey());

        modelBinder.readBean(model);

        wizardPanels = Arrays.asList(
                uploadModelWizardPanel);

        setVisibleWizardPanel(uploadModelWizardPanel);

        Div sectionTitleWrapper = new Div();

        sectionTitleWrapper.add(
                LabelFactory.createLabel("Project: ", SECTION_TITLE_LABEL),
                LabelFactory.createLabel(project.getName(), SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT, SECTION_SUBTITLE_LABEL)
        );
        sectionTitleWrapper.addClassName(PROJECT_TITLE);

        Span invalidModelErrorLabel = modelCheckerService.createInvalidErrorLabel(model);
        invalidModelErrorLabel.getStyle().set("margin-top", "10px");
        invalidModelErrorLabel.getStyle().set("margin-bottom", "10px");

        List<Component> sections = new ArrayList<>();
        sections.add(sectionTitleWrapper);
        sections.add(uploadModelWizardPanel);
        VerticalLayout wrapper = new VerticalLayout(
                sections.toArray(new Component[0]));

        wrapper.addClassName("view-section");
        wrapper.setSpacing(false);
        addClassName("upload-model-view");
        return wrapper;
    }

    @Override
    protected void initLoadData(BeforeEnterEvent event) throws InvalidDataException {
        this.model = ModelUtils.generateNewDefaultModel();
        model.setProjectId(projectId);
        project = projectDAO.getProjectIfAllowed(projectId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access project: " + projectId));
    }

    private void saveAndNavigateToNewExperiment() {
        Experiment experiment = modelService.resumeModelCreation(model, modelNotes);
        experimentId = experiment.getId();
        EventBus.post(new ExperimentCreatedBusEvent(experiment));
        getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, ""+experimentId));
    }

    private void setVisibleWizardPanel(Component wizardPanel) {
        wizardPanels
                .forEach(panel -> panel.setVisible(panel.equals(wizardPanel)));
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(project, model);
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(createBreadcrumbs());
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        String[] segments = parameter.split("/");
        uploadMode = UploadMode.ZIP;

        if (NumberUtils.isDigits(segments[PROJECT_ID_SEGMENT])) {
            this.projectId = Long.parseLong(segments[PROJECT_ID_SEGMENT]);
        }
        if (segments.length > 1) {
            uploadMode = UploadMode.getEnumFromValue(segments[UPLOAD_MODE_SEGMENT]).orElse(UploadMode.ZIP);
            if (uploadMode == UploadMode.RESUME) {
                modelId = Long.parseLong(segments[MODEL_ID_SEGMENT]);
            }
        }
    }
}
