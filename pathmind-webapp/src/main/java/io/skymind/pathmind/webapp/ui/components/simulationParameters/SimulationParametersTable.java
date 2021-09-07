package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

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
public class SimulationParametersTable extends CustomField<Collection<SimulationParameter>> {

    private final VerticalLayout container;

    private final List<SimulationParameter> simulationParameters = new ArrayList<>();
    private final List<SimulationParameter> modelSimulationParameters = new ArrayList<>();
    private final List<SimulationParameter> comparisonSimulationParameters = new ArrayList<>();

    private final List<SimulationParametersRowField> simulationParametersRowFields = new ArrayList<>();
    private final boolean isReadOnly;

    public SimulationParametersTable(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("simulation-parameters-table");

        add(container);
    }

    public void setSimulationParameters(Collection<SimulationParameter> experimentSimulationParameters, Collection<SimulationParameter> modelSimulationParameters) {

        container.removeAll();
        simulationParametersRowFields.clear();

        this.simulationParameters.clear();
        this.modelSimulationParameters.clear();

        if (experimentSimulationParameters != null) {
            this.simulationParameters.addAll(experimentSimulationParameters);
            this.simulationParameters.sort(Comparator.comparing(SimulationParameter::getIndex));
        }

        if (modelSimulationParameters != null) {
            this.modelSimulationParameters.addAll(modelSimulationParameters);
            this.modelSimulationParameters.sort(Comparator.comparing(SimulationParameter::getIndex));
        }

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

            for (int i = 0; i < this.simulationParameters.size(); i++) {
                SimulationParameter simulationParam = this.simulationParameters.get(i);
                SimulationParametersRowField row = new SimulationParametersRowField(simulationParam, isReadOnly, isSpecialType(simulationParam));
                if (!this.modelSimulationParameters.get(i).getValue().equals(simulationParam.getValue())) {
                    row.setIsDifferentFromDefault(true);
                }
                if (!comparisonSimulationParameters.isEmpty()) {
                    row.setComparisonParameter(comparisonSimulationParameters.get(i));
                }
                container.add(row);
                simulationParametersRowFields.add(row);
            }
        }
    }

    public void setSimulationParameters(Collection<SimulationParameter> simulationParameters) {
        setSimulationParameters(simulationParameters, modelSimulationParameters);
    }

    public void setComparisonParameters(Collection<SimulationParameter> comparisonSimulationParameters) {
        this.comparisonSimulationParameters.clear();
        if (comparisonSimulationParameters != null) {
            this.comparisonSimulationParameters.addAll(comparisonSimulationParameters);
            this.comparisonSimulationParameters.sort(Comparator.comparing(SimulationParameter::getIndex));
        }

        if (!simulationParametersRowFields.isEmpty()) {
            for (int i = 0; i < simulationParametersRowFields.size(); i++) {
                if (this.comparisonSimulationParameters.isEmpty()) {
                    simulationParametersRowFields.get(i).setComparisonParameter(null);
                } else {
                    simulationParametersRowFields.get(i).setComparisonParameter(this.comparisonSimulationParameters.get(i));
                }
            }
        }
    }

    @Override
    protected Collection<SimulationParameter> generateModelValue() {
        return simulationParameters;
    }

    @Override
    protected void setPresentationValue(Collection<SimulationParameter> newPresentationValue) {
        setSimulationParameters(newPresentationValue);
    }

    private boolean isSpecialType(SimulationParameter simulationParameter) {
        return simulationParameter.getType().equals(ParamType.OTHERS.getValue()) || simulationParameter.isNullString();
    }
}
