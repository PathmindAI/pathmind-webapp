package io.skymind.pathmind.ui.components.juicy;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import io.skymind.pathmind.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.ui.components.juicy.theme.JuicyAceTheme;

//NOTE: consider to move as separate from project component
@Tag("juicy-ace-editor")
@NpmPackage(value = "ace-builds", version = "^1.2.9")
@JsModule("ace-builds/src-noconflict/ace.js")
@JsModule("ace-builds/src-noconflict/mode-java.js")
@JsModule("ace-builds/src-noconflict/theme-eclipse.js")
@JsModule("ace-builds/src-noconflict/ext-searchbox.js")
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
    }

    @Synchronize({"change"})
    public String getValue() {
        return this.getElement().getProperty("value");
    }
}
