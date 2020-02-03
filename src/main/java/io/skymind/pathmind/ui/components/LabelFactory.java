package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.html.Span;

public class LabelFactory
{
	// Helper method to save extra code.
	public static Span createLabel(String text, String classname)
	{
		Span span = new Span(text);
		span.setClassName(classname);
		return span;
	}
}
