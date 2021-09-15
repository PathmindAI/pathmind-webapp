package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.List;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import org.apache.commons.lang3.StringUtils;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardFunctionEditorRow extends HorizontalLayout implements RewardTermRow{
    private final NumberField goalField;
    private JuicyAceEditor rewardFunctionJuicyAceEditor;
    private RewardCodeErrorPanel rewardCodeErrorPanel;

    public RewardFunctionEditorRow(List<RewardVariable> rewardVariables) {
        rewardFunctionJuicyAceEditor = new JuicyAceEditor();
        rewardFunctionJuicyAceEditor.setSizeFull();
        rewardFunctionJuicyAceEditor.setTheme(JuicyAceTheme.eclipse);
        rewardFunctionJuicyAceEditor.setMode(JuicyAceMode.java);
        rewardFunctionJuicyAceEditor.setWrapmode(false);
        rewardFunctionJuicyAceEditor.setAutoComplete(rewardVariables);

        rewardCodeErrorPanel = new RewardCodeErrorPanel();

        goalField = new NumberField();
        goalField.setPlaceholder("Weight");
        goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        goalField.addValueChangeListener(event -> {});
        goalField.setHasControls(true);

        setWidthFull();
        setSpacing(false);

        add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                rewardFunctionJuicyAceEditor,
                rewardCodeErrorPanel
            ),
            new Span("x"),
            goalField);
    }

    public void addEditorValueChangeListener(ValueChangeListener<ComponentValueChangeEvent<JuicyAceEditor, String>> valueChangeListener) {
        rewardFunctionJuicyAceEditor.addValueChangeListener(valueChangeListener);
    }

    public void setErrors(List<String> errors) {
        rewardCodeErrorPanel.setErrors(String.join("\n", errors));
    }

    public Double getWeight() {
        return this.goalField.getValue();
    }

    public void setWeight(Double weight) {
        this.goalField.setValue(weight);
    }

    public void setSnippet(String snippet) {
        this.rewardFunctionJuicyAceEditor.setValue(snippet);
    }

    public String getSnippet() {
        return StringUtils.trimToEmpty(this.rewardFunctionJuicyAceEditor.getValue());
    }

    @Override
    public Component asComponent() {
        return this;
    }
}
