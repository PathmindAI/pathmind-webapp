package io.skymind.pathmind.webapp.ui.views.policy;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
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
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.components.policy.ExportPolicyButton;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.components.alp.DownloadModelAlpLink;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPORT_POLICY, layout = MainLayout.class)
public class ExportPolicyView extends PathMindDefaultView implements HasUrlParameter<Long> {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PolicyDAO policyDAO;
    @Autowired
    private PolicyFileService policyFileService;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private final ExportPolicyViewContent exportPolicyViewContent;
    private ExportPolicyButton exportButton;
    private Anchor downloadModelAlpLink;

    private long policyId;
    private Policy policy;

    @Autowired
    public ExportPolicyView(ExportPolicyViewContent exportPolicyViewContent) {
        this.exportPolicyViewContent = exportPolicyViewContent;
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

        exportPolicyViewContent.setFilename(exportButton.getPolicyFilename());

        VerticalLayout exportPolicyViewContentAndButtons = WrapperUtils.wrapFormCenterVertical(
            exportPolicyViewContent,
            exportButton,
            downloadModelAlpLink
        );

        return exportPolicyViewContentAndButtons;
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

    private Breadcrumbs createBreadcrumbs() {
        return new Breadcrumbs(policy.getProject(), policy.getModel(), policy.getExperiment(), "Export");
    }
}
