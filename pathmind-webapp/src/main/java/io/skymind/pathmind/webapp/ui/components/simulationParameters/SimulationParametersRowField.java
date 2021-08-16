package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;

public class SimulationParametersRowField extends HorizontalLayout {

    private SimulationParameter simulationParameter;

    private Component inputField;
    private Span nameSpan;
    private Boolean isReadOnly = false;

    public SimulationParametersRowField(SimulationParameter simulationParameter, 
                                        Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        setSimulationParameter(simulationParameter);
        nameSpan = LabelFactory.createLabel(simulationParameter.getKey(), "simulation-parameter-name");
        add(nameSpan);
        int dataType = simulationParameter.getType();
        inputField = getUserInputField(dataType);
        add(inputField);
        GuiUtils.removeMarginsPaddingAndSpacing(this);
    }

    private Component getUserInputField(int parameterType) {
        switch(parameterType) {
            case 0:
                Select<String> booleanSelect = new Select<>();
                booleanSelect.setItems("TRUE", "FALSE");
                booleanSelect.setValue(simulationParameter.getValue().toUpperCase());
                booleanSelect.getElement().setAttribute("theme", "small");
                booleanSelect.setReadOnly(isReadOnly);
                booleanSelect.addValueChangeListener(changeEvent -> simulationParameter.setValue(changeEvent.getValue()));
                return booleanSelect;
            case 1:
                IntegerField integerField = new IntegerField();
                integerField.setHasControls(true);
                integerField.setValue(Integer.parseInt(simulationParameter.getValue()));
                integerField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                integerField.setReadOnly(isReadOnly);
                integerField.addValueChangeListener(changeEvent -> simulationParameter.setValue(""+changeEvent.getValue()));
                return integerField;
            case 2:
                NumberField doubleField = new NumberField();
                doubleField.setValue(Double.parseDouble(simulationParameter.getValue()));
                doubleField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);
                doubleField.setReadOnly(isReadOnly);
                doubleField.addValueChangeListener(changeEvent -> simulationParameter.setValue(""+changeEvent.getValue()));
                return doubleField;
            case 3:
                TextField stringField = new TextField();
                stringField.setValue(simulationParameter.getValue());
                stringField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                stringField.setReadOnly(isReadOnly);
                stringField.addValueChangeListener(changeEvent -> simulationParameter.setValue(changeEvent.getValue()));
                return stringField;
            case 4:
            default:
                TextField othersField = new TextField();
                othersField.setValue(simulationParameter.getValue());
                othersField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                othersField.setReadOnly(true);
                return othersField;
        }
    }

    public SimulationParameter getSimulationParameter() {
        return simulationParameter;
    }

    public void setSimulationParameter(SimulationParameter simulationParameter) {
        this.simulationParameter = simulationParameter;
    }

}
