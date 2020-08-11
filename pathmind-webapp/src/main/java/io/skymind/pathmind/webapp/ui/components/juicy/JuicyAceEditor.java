package io.skymind.pathmind.webapp.ui.components.juicy;

import java.util.List;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;

//NOTE: consider to move as separate from project component
@Tag("juicy-ace-editor")
@JavaScript("./src/juicy-ace-editor/ace/ace.js")
@JavaScript("./src/juicy-ace-editor/ace/ext-searchbox.js")
@JavaScript("./src/juicy-ace-editor/ace/ext-beautify.js")
@JavaScript("./src/juicy-ace-editor/ace/ext-language_tools.js")
@JavaScript("./src/juicy-ace-editor/ace/mode/mode-java.js")
@JavaScript("./src/juicy-ace-editor/ace/theme/theme-pathmind.js")
@JsModule("./src/juicy-ace-editor/juicy-ace-editor-npm.min.js")
@JsModule("./src/juicy-ace-editor/juicy-ace-editor-autocomplete.js")
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
	
	public void setAutoComplete(List<RewardVariable> rewardVariables) {
	    JsonArray localVariables = generateLocalVariablesAutocompleteOption(rewardVariables);
        getElement().executeJs("window.Pathmind.autocomplete.enableAutoComplete($0, $1)", getElement(), localVariables);
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
	
	private JsonArray generateLocalVariablesAutocompleteOption(List<RewardVariable> rewardVariables) {
        JsonArray localVariables = Json.createArray();
        localVariables.set(localVariables.length(), createVariableAutocompleteOption("reward", "double"));
        for (RewardVariable rewardVariable : rewardVariables) {
            localVariables.set(localVariables.length(), createVariableAutocompleteOption("before."+rewardVariable.getName(), rewardVariable.getDataType()));
            localVariables.set(localVariables.length(), createVariableAutocompleteOption("after."+rewardVariable.getName(), rewardVariable.getDataType()));
        }
        return localVariables;
    }

    private JsonObject createVariableAutocompleteOption(String variable, String dataType) {
        JsonObject autocompleteOption = Json.createObject();
        autocompleteOption.put("caption", variable);
        autocompleteOption.put("value", variable);
        autocompleteOption.put("meta", dataType);
        return autocompleteOption;
    }
}
