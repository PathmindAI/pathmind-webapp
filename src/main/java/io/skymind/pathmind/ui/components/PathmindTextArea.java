package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.textfield.TextArea;

public class PathmindTextArea extends TextArea {

	/**
	 * Enables/Disables browser spell check for text area
	 */
	public void setSpellcheck(boolean isSpellcheck) {
		getElement().setProperty("spellcheck", isSpellcheck);
	}
}
