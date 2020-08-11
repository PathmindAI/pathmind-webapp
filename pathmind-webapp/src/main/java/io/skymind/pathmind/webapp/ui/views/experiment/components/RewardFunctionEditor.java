package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.List;

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
		setWrapmode(false);
	}
	
	public void setVariableNames(List<RewardVariable> rewardVariables) {
	    setAutoComplete(rewardVariables);
    }
}
