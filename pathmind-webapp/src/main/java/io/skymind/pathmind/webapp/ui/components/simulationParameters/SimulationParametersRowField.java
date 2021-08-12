package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;

import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;

public class SimulationParametersRowField extends HorizontalLayout {

    private SimulationParameter simulationParameter;

    private Binder<SimulationParameter> binder;

    private Component inputField;
    private Span nameSpan;

    public SimulationParametersRowField(SimulationParameter simulationParameter, 
                                        Boolean isReadOnly) {
        setSimulationParameter(simulationParameter);
        nameSpan = LabelFactory.createLabel(simulationParameter.getKey(), "simulation-parameter-name");
        add(nameSpan);
        int dataType = simulationParameter.getDataType();
        inputField = getUserInputField(dataType);
        initBinder(this.simulationParameter);
        add(inputField);
        GuiUtils.removeMarginsPaddingAndSpacing(this);
    }

    private void initBinder(SimulationParameter simulationParameter) {
        binder = new Binder<>();
        // TODO -> binder.bind()
        binder.setBean(simulationParameter);
    }

    private Component getUserInputField(int parameterType) {
        switch(parameterType) {
            case 0:
                Select<String> booleanSelect = new Select<>();
                booleanSelect.setItems("TRUE", "FALSE");
                booleanSelect.setValue(simulationParameter.getValue().toUpperCase());
                booleanSelect.getElement().setAttribute("theme", "small");
                return booleanSelect;
            case 1:
                IntegerField integerField = new IntegerField();
                integerField.setHasControls(true);
                integerField.setValue(Integer.parseInt(simulationParameter.getValue()));
                integerField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                return integerField;
            case 2:
                NumberField doubleField = new NumberField();
                doubleField.setValue(Double.parseDouble(simulationParameter.getValue()));
                doubleField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);
                return doubleField;
            case 3:
                TextField stringField = new TextField();
                stringField.setValue(simulationParameter.getValue());
                stringField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                return stringField;
            case 4:
            default:
                TextField othersField = new TextField();
                othersField.setValue(simulationParameter.getValue());
                othersField.setReadOnly(true);
                othersField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
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
