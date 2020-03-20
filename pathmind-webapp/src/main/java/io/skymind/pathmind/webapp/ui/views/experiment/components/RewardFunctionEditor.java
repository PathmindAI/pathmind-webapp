package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.List;

import elemental.json.Json;
import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;

/**
 * Created as it's own component so that we can easily swap in AceEditor later
 * with minimal code impact. I extended it so that binding code etc, would work
 * as expected and be consistent with other components.
 */
public class RewardFunctionEditor extends JuicyAceEditor {
	public RewardFunctionEditor() {
		super();

		setSizeFull();
		setTheme(JuicyAceTheme.eclipse);
		setMode(JuicyAceMode.java);
		setWrapmode(true);
	}

	public void setVariableNames(List<RewardVariable> rewardVariables) {
		setVariableNames(convertToJson(rewardVariables));
		addVariableNameSupport();
	}

	private JsonObject convertToJson(List<RewardVariable> rewardVariables) {
		JsonObject json = Json.createObject();
		rewardVariables.forEach(rewardVariable -> json.put(Integer.toString(rewardVariable.getArrayIndex()), rewardVariable.getName()));
		return json;
	}
}
