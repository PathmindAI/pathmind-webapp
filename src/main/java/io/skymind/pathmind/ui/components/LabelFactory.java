package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.html.Label;

public class LabelFactory
{
	// Helper method to save extra code.
	public static Label createLabel(String text, String classname)
	{
		Label label = new Label(text);
		label.setClassName(classname);
		return label;
	}
}
