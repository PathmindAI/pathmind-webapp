package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;

public class SimulationParametersRowField extends HorizontalLayout {

    private SimulationParameter simulationParameter;

    private Component inputField;
    private Span nameSpan;
    private Boolean isReadOnly = false;
    private String differentFromDefaultClassname = "different-from-default";
    private String differentFromComparisonClassname = "different-from-comparison";

    public SimulationParametersRowField(SimulationParameter simulationParameter,
                                        Boolean isReadOnly,
                                        Boolean isSpecialType) {
        this.isReadOnly = isReadOnly;
        setSimulationParameter(simulationParameter);
        nameSpan = LabelFactory.createLabel(simulationParameter.getKey(), "simulation-parameter-name");
        add(nameSpan);
        ParamType dataType = simulationParameter.getType();
        if (isReadOnly) {
            add(getReadonlySpan(dataType));
        } else {
            if (isSpecialType) {
                this.isReadOnly = true;
            }
            inputField = getUserInputField(dataType);
            add(inputField);
        }
        GuiUtils.removeMarginsPaddingAndSpacing(this);
    }

    private Component getUserInputField(ParamType parameterType) {
        switch (parameterType) {
            case BOOLEAN:
                Select<String> booleanSelect = new Select<>();
                booleanSelect.setItems("TRUE", "FALSE");
                booleanSelect.setValue(simulationParameter.getValue().toUpperCase());
                booleanSelect.getElement().setAttribute("theme", "small");
                booleanSelect.setReadOnly(isReadOnly);
                booleanSelect.addValueChangeListener(changeEvent -> simulationParameter.setValue(changeEvent.getValue()));
                return booleanSelect;
            case INTEGER:
                IntegerField integerField = new IntegerField();
                integerField.setHasControls(true);
                integerField.setValue(Integer.parseInt(simulationParameter.getValue()));
                integerField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                integerField.setReadOnly(isReadOnly);
                integerField.addValueChangeListener(changeEvent -> simulationParameter.setValue("" + changeEvent.getValue()));
                return integerField;
            case DOUBLE:
                NumberField doubleField = new NumberField();
                doubleField.setValue(Double.parseDouble(simulationParameter.getValue()));
                doubleField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);
                doubleField.setReadOnly(isReadOnly);
                doubleField.addValueChangeListener(changeEvent -> simulationParameter.setValue("" + changeEvent.getValue()));
                return doubleField;
            case STRING:
                TextField stringField = new TextField();
                stringField.setValue(simulationParameter.getValue());
                stringField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                stringField.setReadOnly(isReadOnly);
                stringField.addValueChangeListener(changeEvent -> simulationParameter.setValue(changeEvent.getValue()));
                return stringField;
            case DATE:
                DateTimePicker dateTimePicker = new DateTimePicker();
                LocalDateTime dateValue = Instant.ofEpochMilli(Long.parseLong(simulationParameter.getValue())).atZone(ZoneId.of("Etc/GMT")).toLocalDateTime();
                dateTimePicker.setDatePlaceholder("Date");
                dateTimePicker.setTimePlaceholder("Time");
                dateTimePicker.setValue(dateValue);
                dateTimePicker.addThemeName("small");
                dateTimePicker.setReadOnly(isReadOnly);
                dateTimePicker.addValueChangeListener(changeEvent ->
                        simulationParameter.setValue("" + changeEvent.getValue().atZone(ZoneId.of("Etc/GMT")).toInstant().toEpochMilli()));
                return dateTimePicker;
            case OTHERS:
            default:
                TextField othersField = new TextField();
                othersField.setValue(simulationParameter.getValue());
                othersField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                othersField.setReadOnly(true);
                return othersField;
        }
    }

    private Span getReadonlySpan(ParamType parameterType) {
        Span textSpan = new Span();
        switch (parameterType) {
            case BOOLEAN:
                textSpan.add(simulationParameter.getValue().toUpperCase());
                break;
            case DATE:
                LocalDateTime dateTime = Instant.ofEpochMilli(Long.parseLong(simulationParameter.getValue())).atZone(ZoneId.of("Etc/GMT")).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
                textSpan.add(dateTime.format(formatter));
                break;
            default:
                textSpan.add(simulationParameter.getValue());
                break;
        }
        return textSpan;
    }

    public SimulationParameter getSimulationParameter() {
        return simulationParameter;
    }

    public void setIsDifferentFromDefault(Boolean isDifferent) {
        if (isDifferent) {
            addClassName(differentFromDefaultClassname);
        } else {
            removeClassName(differentFromDefaultClassname);
        }
    }

    public void setSimulationParameter(SimulationParameter simulationParameter) {
        this.simulationParameter = simulationParameter;
    }

    public void setComparisonParameter(SimulationParameter comparisonParameter) {
        if (comparisonParameter == null || simulationParameter.getValue().equals(comparisonParameter.getValue())) {
            unhighlight();
        } else {
            highlight();
        }
    }

    private void highlight() {
        addClassName(differentFromComparisonClassname);
    }

    public void unhighlight() {
        removeClassName(differentFromComparisonClassname);
    }

}
