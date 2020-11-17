package io.skymind.pathmind.webapp.ui.views.policy;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.policy.ExportPolicyButton;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPORT_POLICY_URL, layout = MainLayout.class)
public class ExportPolicyView extends PathMindDefaultView implements HasUrlParameter<Long> {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PolicyDAO policyDAO;
    @Autowired
    private PolicyFileService policyFileService;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private ExportPolicyButton exportButton;
    private Button cancelButton;
    private Anchor downloadModelAlpLink;

    private long policyId;
    private Policy policy;

    public ExportPolicyView() {
        super();
        addClassName("export-policy-view");
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel(createBreadcrumbs());
    }

    @Override
    protected Component getMainContent() {
        exportButton = new ExportPolicyButton(segmentIntegrator, policyFileService, policyDAO, () -> policy);

        downloadModelAlpLink = new DownloadModelAlpLink(policy.getProject().getName(), policy.getModel(), modelService, segmentIntegrator, true);

        Anchor learnMoreLink = new Anchor("https://help.pathmind.com/en/articles/3655157-9-validate-trained-policy", "Learn how to validate your policy");
        learnMoreLink.setTarget("_blank");

        cancelButton = new Button("< Back to Experiment #" + policy.getExperiment().getName(), click -> handleCancelButtonClicked());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        VerticalLayout wrapperContent = WrapperUtils.wrapFormCenterVertical(
                LabelFactory.createLabel("Export Policy", CssPathmindStyles.SECTION_TITLE_LABEL),
                new Image("/frontend/images/exportPolicyIcon.gif", "Export Policy"),
                LabelFactory.createLabel(exportButton.getPolicyFilename()),
                createInstructionsDiv(),
                learnMoreLink,
                exportButton,
                downloadModelAlpLink);
        wrapperContent.setClassName("view-section");
        return WrapperUtils.wrapCenterVertical("100%",
                wrapperContent,
                cancelButton);
    }

    private void handleCancelButtonClicked() {
        getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, policy.getExperiment().getId()));
    }

    @Override
    public void setParameter(BeforeEvent event, Long policyId) {
        this.policyId = policyId;
    }

    @Override
    protected void initLoadData() {
        policy = policyDAO.getPolicyIfAllowed(policyId, SecurityUtils.getUserId())
                .orElseThrow(() -> new InvalidDataException("Attempted to access Policy: " + policyId));
    }

    private Div createInstructionsDiv() {
        Div div = new Div();
        div.getElement().setProperty("innerHTML",
                "<h3>To use your policy:</h3>" +
                        "<ol>" +
                        "<li>Download this file.</li>" +
                        "<li>Return to AnyLogic and open the Pathmind Helper properties in your simulation.</li>" +
                        "<ul>" +
                        "<li>Change the 'Mode' to 'Use Policy'.</li>" +
                        "<li>In 'policyFile', click 'Browse' and select the file you downloaded.</li>" +
                        "</ul>" +
                        "<li>Run the simulation to see the policy in action.</li>" +
                        "</ol>");
        return div;
    }

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(policy.getProject(), policy.getModel(), policy.getExperiment(), "Export");
    }
}
