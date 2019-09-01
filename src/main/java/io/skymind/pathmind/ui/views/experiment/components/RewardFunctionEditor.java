package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

/**
 * Created as it's own component so that we can easily swap in AceEditor later with minimal code impact.
 */
public class RewardFunctionEditor extends VerticalLayout
{
	private TextArea rewardFunctionTextArea = new TextArea("Write Your Reward Function");

	public RewardFunctionEditor() {
		setSizeFull();
		rewardFunctionTextArea.setSizeFull();
		add(rewardFunctionTextArea);
	}

	public void setRewardFunction(String rewardFunction) {
		rewardFunctionTextArea.setValue(rewardFunction);
	}
}
