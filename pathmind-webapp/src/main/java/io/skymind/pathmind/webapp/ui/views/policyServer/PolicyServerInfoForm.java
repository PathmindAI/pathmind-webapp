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
        List<ExperimentSelectItem> experimentSelectItems = new ArrayList<>();
        experimentSelect = new Select<>();
        experimentSelect.setRenderer(new ComponentRenderer<>(experiment -> {
            ExperimentSelectItem currentItem = new ExperimentSelectItem();
            currentItem.setProjectName(experiment.getProject().getName());
            currentItem.setModelName(experiment.getModel().getName());
            currentItem.setExperimentName(experiment.getName());
            experimentSelectItems.add(currentItem);
            return currentItem;
        }));
        // There's a bug in Flow where the new value cannot get into the polymer component rendered so this hack is used
        experimentSelect.addValueChangeListener(source -> {
            Experiment selectedExperiment = source.getValue();
            String projectName = selectedExperiment.getProject().getName();
            String modelName = selectedExperiment.getModel().getName();
            String experimentName = selectedExperiment.getName();
            experimentSelect.getElement().executeJs("const displayItem = this.shadowRoot.querySelector('experiment-select-item'); displayItem.projectName = $0; displayItem.modelName = $1; displayItem.experimentName = $2;", projectName, modelName, experimentName);
        });
        experimentSelect.setItems(completedExperimentsList);
        experimentSelect.setPlaceholder("Choose a completed experiment");
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