package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.function.SerializableConsumer;

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
    
    private List<SerializableConsumer<RewardFunctionValidationEvent>> validationListeners;
    
	public RewardFunctionEditor() {
		super();
		
		validationListeners = new ArrayList<>();
		setSizeFull();
		setTheme(JuicyAceTheme.eclipse);
		setMode(JuicyAceMode.java);
		setWrapmode(false);
		addListener(RewardFunctionValidationEvent.class, this::processRewardFunctionValidationEvent);
	}

    public void setVariableNames(List<RewardVariable> rewardVariables, int variableCount) {
		setVariableNames(convertToJson(rewardVariables), variableCount);
		addVariableNameSupport();
	}
    
    public void addValidationListener(SerializableConsumer<RewardFunctionValidationEvent> listener) {
        validationListeners.add(listener);
    }

	private JsonObject convertToJson(List<RewardVariable> rewardVariables) {
		JsonObject json = Json.createObject();
		rewardVariables.forEach(rewardVariable -> json.put(Integer.toString(rewardVariable.getArrayIndex()), rewardVariable.getName()));
		return json;
	}
	
	private void processRewardFunctionValidationEvent(RewardFunctionValidationEvent evt) {
	    validationListeners.forEach(listener -> listener.accept(evt));
	}
	
}
