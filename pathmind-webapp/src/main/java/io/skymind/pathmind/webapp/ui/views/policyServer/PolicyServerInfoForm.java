package io.skymind.pathmind.webapp.ui.views.policyServer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.model.components.ActionsPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;

@Route(value = Routes.POLICY_SERVER_FORM, layout = MainLayout.class)
public class PolicyServerInfoForm extends PathMindDefaultView implements HasUrlParameter<Long> {

    private ActionsPanel actionsPanel;
    private ObservationsPanel observationsPanel;

    public PolicyServerInfoForm() {
        super();
    }

    @Override
    protected Component getMainContent() {
		actionsPanel = new ActionsPanel();
		observationsPanel = new ObservationsPanel();
        
        VerticalLayout wrapper = new VerticalLayout(
            actionsPanel,
            observationsPanel);

        wrapper.addClassName("view-section");
        wrapper.setSpacing(false);
        addClassName("policy-server-form");
        return wrapper;
    }

	protected VerticalLayout getTitlePanel() {
		return null;
	}

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter == null) {

        } else {
            
        }
    }
}