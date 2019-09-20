package io.skymind.pathmind.ui.views.experiment.components;

import com.juicy.JuicyAceEditor;
import com.juicy.mode.JuicyAceMode;
import com.juicy.theme.JuicyAceTheme;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Created as it's own component so that we can easily swap in AceEditor later with minimal code impact.
 * I extended it so that binding code etc, would work as expected and be consistent with other components.
 */
public class RewardFunctionEditor extends JuicyAceEditor
{
	public RewardFunctionEditor()
	{
		super();

		setSizeFull();
		setTheme(JuicyAceTheme.eclipse);
		setMode(JuicyAceMode.java);
	}
}
