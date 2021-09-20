package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.data.RewardTerm;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardFunctionEditorRow extends CustomField<RewardTerm> implements RewardTermRow{
    private final NumberField weightField;
    private JuicyAceEditor rewardFunctionJuicyAceEditor;
    private RewardCodeErrorPanel rewardCodeErrorPanel;
    private RewardTerm rewardTerm;
    private Binder<RewardTerm> binder;
    private Command changeHandler;

    public RewardFunctionEditorRow(List<RewardVariable> rewardVariables, RewardValidationService rewardValidationService, Command changeHandler) {
        this.changeHandler = changeHandler;
        rewardFunctionJuicyAceEditor = new JuicyAceEditor();
        rewardFunctionJuicyAceEditor.setSizeFull();
        rewardFunctionJuicyAceEditor.setTheme(JuicyAceTheme.eclipse);
        rewardFunctionJuicyAceEditor.setMode(JuicyAceMode.java);
        rewardFunctionJuicyAceEditor.setWrapmode(false);
        rewardFunctionJuicyAceEditor.setAutoComplete(rewardVariables);
        rewardFunctionJuicyAceEditor.addValueChangeListener(changeEvent -> {
            List<String> rewardFunctionErrors = rewardValidationService.validateRewardFunction(changeEvent.getValue(), rewardVariables);
            setErrors(rewardFunctionErrors);
        });

        rewardCodeErrorPanel = new RewardCodeErrorPanel();

        weightField = new NumberField();
        weightField.setPlaceholder("Weight");
        weightField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        weightField.setHasControls(true);

        HorizontalLayout wrapper = WrapperUtils.wrapWidthFullHorizontalNoSpacing(
            WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                rewardFunctionJuicyAceEditor,
                rewardCodeErrorPanel
            ),
            new Span("x"),
            weightField
        );

        add(wrapper);
    }

    private void initBinder() {
        binder = new Binder<>();
        binder.bind(rewardFunctionJuicyAceEditor, RewardTerm::getRewardSnippet, RewardTerm::setRewardSnippet);
        binder.bind(weightField, RewardTerm::getWeight, RewardTerm::setWeight);
        binder.addValueChangeListener(event -> changeHandler.execute());
        binder.setBean(rewardTerm);
    }

    public void setErrors(List<String> errors) {
        rewardCodeErrorPanel.setErrors(String.join("\n", errors));
    }

    @Override
    public Component asComponent() {
        return this;
    }

    @Override
    protected RewardTerm generateModelValue() {
        return rewardTerm;
    }

    @Override
    protected void setPresentationValue(RewardTerm rewardTerm) {
        this.rewardTerm = rewardTerm;
        if (binder == null) {
            initBinder();
        }
    }
}
