package io.skymind.pathmind.webapp.ui.views.policyServer.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.data.Experiment;

@Tag("experiment-select-item")
@JsModule("./src/experiment/experiment-select-item.js")
public class ExperimentSelectItem extends PolymerTemplate<ExperimentSelectItem.Model> {
    public ExperimentSelectItem() {
        super();
    }

    public void setProjectName(String projectName) {
        getModel().setProjectName(projectName);
    }

    public void setModelName(String modelName) {
        getModel().setModelName(modelName);
    }

    public void setExperimentName(String experimentName) {
        getModel().setExperimentName(experimentName);
    }

	public interface Model extends TemplateModel
	{
		void setProjectName(String projectName);

        void setModelName(String modelName);
        
        void setExperimentName(String expName);
	}
}