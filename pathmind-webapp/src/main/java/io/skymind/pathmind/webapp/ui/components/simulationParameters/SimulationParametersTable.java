package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/simulation-parameters-table.css")
public class SimulationParametersTable extends CustomField<Set<SimulationParameter>> {

    private VerticalLayout container;
    private Set<SimulationParameter> simulationParameters = new HashSet<SimulationParameter>();
    private Set<SimulationParameter> modelSimulationParameters = new HashSet<SimulationParameter>();
    private Boolean isReadOnly = false;

    public SimulationParametersTable(Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("simulation-parameters-table");

        add(container);
    }

    public void setSimulationParameters(List<SimulationParameter> experimentSimulationParameters, List<SimulationParameter> modelSimulationParameters) {
        Collections.sort(experimentSimulationParameters, Comparator.comparing(SimulationParameter::getIndex));
        this.simulationParameters = new HashSet<SimulationParameter>(experimentSimulationParameters);
        Collections.sort(modelSimulationParameters, Comparator.comparing(SimulationParameter::getIndex));
        this.modelSimulationParameters = new HashSet<SimulationParameter>(modelSimulationParameters);

        container.removeAll();

        if (simulationParameters.isEmpty()) {
            Button readMoreButton = new Button("Learn More");
            readMoreButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Anchor intercomArticleLink = new Anchor("http://help.pathmind.com/en/articles/5506514-simulation-parameters", readMoreButton);
            intercomArticleLink.setTarget("_blank");
            VerticalLayout text = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                new Paragraph("This model does not contain any simulation parameters."),
                intercomArticleLink
            );
            text.addClassName("simulation-parameters-instructions");
            container.add(text);
        } else {
            HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("Name"), new Span("Value"));
            headerRow.addClassName("header-row");
            GuiUtils.removeMarginsPaddingAndSpacing(headerRow);
            container.add(headerRow);

            for (int i = 0; i < experimentSimulationParameters.size(); i++) {
                SimulationParameter simulationParam = experimentSimulationParameters.get(i);
                SimulationParametersRowField row = new SimulationParametersRowField(simulationParam, isReadOnly, isSpecialType(simulationParam));
                if (!modelSimulationParameters.get(i).getValue().equals(simulationParam.getValue())) {
                    row.setIsDifferentFromDefault(true);
                }
                container.add(row);
            }
        }
    }

    public void setSimulationParameters(Set<SimulationParameter> simulationParameters, Set<SimulationParameter> modelSimulationParameters) {
        setSimulationParameters(new ArrayList<SimulationParameter>(simulationParameters), new ArrayList<SimulationParameter>(modelSimulationParameters));
    }

    public void setSimulationParameters(Set<SimulationParameter> simulationParameters) {
        setSimulationParameters(simulationParameters, modelSimulationParameters);
    }

    @Override
    protected Set<SimulationParameter> generateModelValue() {
        return simulationParameters;
    }

    @Override
    protected void setPresentationValue(Set<SimulationParameter> newPresentationValue) {
        setSimulationParameters(newPresentationValue);
    }

    private boolean isSpecialType(SimulationParameter simulationParameter) {
        if (simulationParameter.getType().equals(ParamType.OTHERS.getValue()) ||
            simulationParameter.getType().equals(ParamType.STRING.getValue()) && simulationParameter.getValue().equals("NULL_VALUE")) {
            return true;
        }
        return false;
    }
}
