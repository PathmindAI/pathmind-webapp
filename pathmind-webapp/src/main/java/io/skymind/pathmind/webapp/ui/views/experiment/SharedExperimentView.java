package io.skymind.pathmind.webapp.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.constants.ViewPermission;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.security.annotation.Permission;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.policy.ExportPolicyButton;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = Routes.SHARED_EXPERIMENT, layout = MainLayout.class)
@Slf4j
@Permission(permissions = ViewPermission.EXTENDED_READ)
public class SharedExperimentView extends ExperimentView
{
    @Autowired
    private PolicyFileService policyFileService;

    public SharedExperimentView() {
        super();
    }

    // For the shared view we remove the subscribers we don't want to listen to such as the new experiment created subscriber. It's a more maintainable
    // to remove the ones we don't want then to have to remember to add new ones over time to both views.
    @Override
    protected List<EventBusSubscriber> getViewSubscribers() {
        return super.getViewSubscribers()
                .stream().filter(eventBusSubscriber -> eventBusSubscriber instanceof ExperimentViewExperimentCreatedSubscriber)
                .collect(Collectors.toList());
    }

    protected Optional<Experiment> getExperimentForUser() {
        return experimentDAO.getExperimentForSupportIfAllowed(getExperimentId(), SecurityUtils.getUserId());
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
        return new Component[] {
                new ExportPolicyButton(segmentIntegrator, policyFileService, policyDAO, () -> getBestPolicy())
        };
    }

    private Policy getBestPolicy() {
        return PolicyUtils.selectBestPolicy(getExperiment().getPolicies()).orElse(null);
    }

    protected void updateScreenComponents() {
        super.updateScreenComponents();
        notesField.setReadOnly();
    }
}
