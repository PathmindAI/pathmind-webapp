package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.textfield.TextArea;

public class PathmindTextArea extends TextArea {

	public PathmindTextArea() {
		super();
	}

	public PathmindTextArea(String label) {
		super(label);
	}
	
	
	/**
	 * Enables/Disables browser spell check for text area
	 */
	public void setSpellcheck(boolean isSpellcheck) {
		getElement().setProperty("spellcheck", isSpellcheck);
	}
}
