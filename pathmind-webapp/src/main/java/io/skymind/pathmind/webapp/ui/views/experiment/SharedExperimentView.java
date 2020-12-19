package io.skymind.pathmind.webapp.ui.views.experiment;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.constants.ViewPermission;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.security.annotation.Permission;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.policy.ExportPolicyButton;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.SHARED_EXPERIMENT, layout = MainLayout.class)
@Slf4j
@Permission(permissions = ViewPermission.EXTENDED_READ)
public class SharedExperimentView extends ExperimentView {

    @Autowired
    private PolicyFileService policyFileService;
    @Autowired
    private PolicyDAO policyDAO;

    public SharedExperimentView() {
        super();
    }

    @Override
    protected Optional<Experiment> getExperimentForUser(long specificExperimentId) {
        return experimentDAO.getExperimentForSupportIfAllowed(specificExperimentId, SecurityUtils.getUserId());
    }

    // We don't want breadcrumbs in the shared view.
    @Override
    protected Component getTitlePanel() {
        HorizontalLayout readOnlyBanner = WrapperUtils.wrapWidthFullCenterHorizontal(
                LabelFactory.createLabel("Read-Only Mode")
        );
        readOnlyBanner.addClassName("internal-banner");
        return readOnlyBanner;
    }

    protected boolean isShowNavBar() {
        return false;
    }

    @Override
    protected Component[] getActionButtonList() {
        return new Component[]{
                new ExportPolicyButton(segmentIntegrator, policyFileService, policyDAO, () -> getExperiment())
        };
    }

    @Override
    protected void updateComponentEnablements() {
        experimentNotesField.setReadonly(true);
    }
}
