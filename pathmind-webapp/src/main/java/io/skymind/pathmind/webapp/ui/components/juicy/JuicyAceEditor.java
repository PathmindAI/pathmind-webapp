package io.skymind.pathmind.webapp.ui.components.juicy;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import elemental.json.JsonObject;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;

//NOTE: consider to move as separate from project component
@Tag("juicy-ace-editor")
// Using CDN here as a workaround for an issue with webpack + ace-builds: https://github.com/DanielSchaffer/webpack-babel-multi-target-plugin/issues/39
@JavaScript("./src/juicy-ace-editor/ace/ace.js")
@JavaScript("./src/juicy-ace-editor/ace/ext-searchbox.js")
@JavaScript("./src/juicy-ace-editor/ace/ext-beautify.js")
@JavaScript("./src/juicy-ace-editor/ace/mode/mode-java.js")
@JavaScript("./src/juicy-ace-editor/ace/theme/theme-pathmind.js")
@JavaScript("./src/juicy-ace-editor/juicy-ace-editor-variable-names.js")
@JsModule("./src/juicy-ace-editor/juicy-ace-editor-npm.min.js")
public class JuicyAceEditor extends AbstractSinglePropertyField<JuicyAceEditor, String> implements HasSize, Focusable<JuicyAceEditor> {
	public JuicyAceEditor() {
		super("value", "", false);
	}

	public void setMode(JuicyAceMode mode) {
		this.getElement().setAttribute("mode", "ace/mode/" + mode);
	}

	public void setTheme(JuicyAceTheme theme) {
		this.getElement().setAttribute("theme", "ace/theme/" + theme);
	}

	public void setFontsize(Integer fontsize) {
		this.getElement().setAttribute("fontsize", String.valueOf(fontsize));
	}

	public void setSofttabs(Boolean softtabs) {
		this.getElement().setAttribute("softtabs", softtabs);
	}

	public void setTabsize(Integer tabsize) {
		this.getElement().setAttribute("tabsize", String.valueOf(tabsize));
	}

	public void setReadonly(Boolean readonly) {
		this.getElement().setAttribute("readonly", readonly);
	}

	public void setWrapmode(Boolean wrapmode) {
		this.getElement().setAttribute("wrapmode", wrapmode);
	}

	public void setMaxLines(Integer maxLines) {
		this.getElement().setAttribute("max-lines", String.valueOf(maxLines));
	}

	public void setMinLines(Integer minLines) {
		this.getElement().setAttribute("min-lines", String.valueOf(minLines));
	}

	public void setShadowStyle(String shadowStyle) {
		this.getElement().setAttribute("shadow-style", String.valueOf(shadowStyle));
	}

	public void clear() {
		this.getElement().setProperty("value", "");
	}

	public void setValue(String value) {
		this.getElement().setProperty("value", value);
		getElement().executeJs("$0.editor.clearSelection()");
	}

	@Synchronize({"change"})
	public String getValue() {
		return this.getElement().getProperty("value");
	}
	
	protected void addVariableNameSupport() {
		getElement().executeJs("window.Pathmind.CodeEditor.addVariableNamesSupport($0)");
	}
	
	protected void setVariableNames(JsonObject variableNames) {
		getElement().executeJs("window.Pathmind.CodeEditor.setVariableNames($0)", variableNames);
	}
}
