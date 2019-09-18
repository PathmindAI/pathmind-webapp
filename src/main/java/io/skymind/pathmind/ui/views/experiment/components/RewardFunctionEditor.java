package io.skymind.pathmind.ui.views.experiment.components;

import com.juicy.JuicyAceEditor;
import com.juicy.mode.JuicyAceMode;
import com.juicy.theme.JuicyAceTheme;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Created as it's own component so that we can easily swap in AceEditor later with minimal code impact.
 */
public class RewardFunctionEditor extends AbstractCompositeField<VerticalLayout, RewardFunctionEditor, String>
{
	private JuicyAceEditor editor = new JuicyAceEditor();

	public RewardFunctionEditor() {
		super("");

		getContent().setSizeFull();
		editor.setSizeFull();
		editor.setTheme(JuicyAceTheme.eclipse);
		editor.setMode(JuicyAceMode.java);

		getContent().add(
				new Label("Write Your Reward Function"),
				editor
		);
	}

	@Override
	protected void setPresentationValue(String s) {
		editor.setValue(s);
	}
}
