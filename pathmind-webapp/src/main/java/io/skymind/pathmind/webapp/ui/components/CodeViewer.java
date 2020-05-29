package io.skymind.pathmind.webapp.ui.components;

import java.util.List;

import elemental.json.Json;
import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.RewardVariable;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("code-viewer")
@JsModule("/src/experiment/code-viewer.js")
public class CodeViewer extends PolymerTemplate<TemplateModel> implements HasStyle {
    public CodeViewer() {
        super();
    }

	public void setValue(String rewardFunction, List<RewardVariable> rewardVariables) {
        getElement().callJsFunction("setValue", rewardFunction, convertToJson(rewardVariables));
    }

	private JsonObject convertToJson(List<RewardVariable> rewardVariables) {
		JsonObject json = Json.createObject();
        if (rewardVariables != null) {
            rewardVariables.forEach(rewardVariable -> json.put(Integer.toString(rewardVariable.getArrayIndex()), rewardVariable.getName()));
        }
		return json;
    }
}