package io.skymind.pathmind.webapp.ui.views.policyServer;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import io.skymind.pathmind.webapp.ui.views.model.components.ActionsPanel;
import io.skymind.pathmind.webapp.ui.views.model.components.ObservationsPanel;
import io.skymind.pathmind.webapp.ui.views.policyServer.components.ExperimentSelectItem;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.db.dao.ExperimentDAO;

@CssImport("./styles/views/policy-server.css")
@Route(value = Routes.POLICY_SERVER_FORM, layout = MainLayout.class)
public class PolicyServerInfoForm extends PathMindDefaultView implements HasUrlParameter<Long> {

    private List<Experiment> completedExperimentsList = new ArrayList<>();

    private Select<Experiment> experimentSelect;
    private ActionsPanel actionsPanel;
    private ObservationsPanel observationsPanel;

    @Autowired
    private ExperimentDAO experimentDAO;

    public PolicyServerInfoForm() {
        super();
    }

    @Override
    protected Component getMainContent() {
		actionsPanel = new ActionsPanel();
        observationsPanel = new ObservationsPanel();
        
        setupExperimentSelect();
        
        VerticalLayout wrapper = new VerticalLayout(
            LabelFactory.createLabel("Set Up Policy Server", SECTION_TITLE_LABEL),
            experimentSelect,
            actionsPanel,
            observationsPanel);

        wrapper.addClassName("view-section");
        wrapper.setSpacing(false);
        addClassName("policy-server-form");
        return wrapper;
    }

    private void setupExperimentSelect() {
        experimentSelect = new Select<>();
        experimentSelect.setItems(completedExperimentsList);
        experimentSelect.setValue(completedExperimentsList.get(0));
        experimentSelect.setRenderer(new ComponentRenderer<>(experiment -> {
            ExperimentSelectItem currentItem = new ExperimentSelectItem();
            currentItem.setProjectName(experiment.getProject().getName());
            currentItem.setModelName(experiment.getModel().getName());
            currentItem.setExperimentName(experiment.getName());
            return currentItem;
        }));
    }

	protected VerticalLayout getTitlePanel() {
		return null;
    }
    
    @Override
    protected void initLoadData() throws InvalidDataException {
        completedExperimentsList = experimentDAO.getCompletedExperimentsForUser(SecurityUtils.getUserId());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter == null) {

        } else {
            
        }
    }
}