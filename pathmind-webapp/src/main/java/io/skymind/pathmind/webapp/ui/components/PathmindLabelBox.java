package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

@CssImport("./styles/components/label-box.css")
public class PathmindLabelBox extends Div {
	private Span label;
	private Span content;
	
	public PathmindLabelBox(String labelText) {
		this();
		label.setText(labelText);
	}
	
	public PathmindLabelBox() {
		label = LabelFactory.createLabel("");
		label.setId("label");
		content = LabelFactory.createLabel("");
		content.setId("content");
		addClassName("label-box");
		add(label, content);
	}
	
	public void setValue(String value) {
		content.setText(value);
	}
}
