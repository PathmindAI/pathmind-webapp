package io.skymind.pathmind.services.training.cloud.aws;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.SimulationParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: we should consider refactoring the whole AWSExecutionProvider code related with script.sh creation and move
// the operations to a BashScriptCreator class. I won't do that now because it will generate a lot of changes and my
// objective now is to just add a single unit test for 'var'.
public class BashScriptCreatorUtil {
    private static String escapeSingleQuote(String value) {
        return value.replace("'", "'\"'\"'");
    }

    public static String var(String name, String value) {
        return "export " + name + "='" + escapeSingleQuote(value) + "'";
    }

    public static String varExp(String name, String value) {
        return "export " + name + "=" + escapeSingleQuote(value);
    }

    public static String varCondition(String name, String value) {
        return "export " + name + "=${" + name + ":='" + escapeSingleQuote(value) + "'}";
    }

    // this method probably doesn't belong here since it creates a java snippet code that, in turn, will later on be
    // added to a bash script. But, I will let it here till we find a better place to move it to.
    public static String createObservationSnippet(List<Observation> selectedObservations) {
        assert selectedObservations != null && !selectedObservations.isEmpty();
        List<String> selectedObservationsVars = new ArrayList<>();
        selectedObservations.stream()
            .filter(obs -> !obs.getVariable().equals(Observation.ACTION_MASKING)) // filter out action mask observation
            .forEach(o -> {
            if (ObservationDataType.isArray(o.getDataTypeEnum())) {
                if (o.getDataTypeEnum() == ObservationDataType.BOOLEAN_ARRAY) {
                    for (int i = 0; i < o.getMaxItems(); i++) {
                        selectedObservationsVars.add(String.format("%s[%s] ? 1.0 : 0.0", o.getVariable(), i));
                    }
                } else {
                    for (int i = 0; i < o.getMaxItems(); i++) {
                        selectedObservationsVars.add(String.format("%s[%s]", o.getVariable(), i));
                    }
                }
            } else {
                if (o.getDataTypeEnum() == ObservationDataType.BOOLEAN) {
                    selectedObservationsVars.add(String.format("%s ? 1.0 : 0.0", o.getVariable()));
                } else {
                    selectedObservationsVars.add(o.getVariable());
                }
            }
        });

        List<String> statements = new ArrayList<>();
        statements.add(String.format("out = new double[%s];", selectedObservationsVars.size()));
        for (int i = 0; i < selectedObservationsVars.size(); i++) {
            statements.add(String.format("out[%s] = in.%s;", i, selectedObservationsVars.get(i)));
        }
        return String.join("\n", statements);
    }

    public static String createSimulationParameterSnippet(List<SimulationParameter> simulationParameters) {
        assert simulationParameters != null && !simulationParameters.isEmpty();
        List<String> statements = simulationParameters.stream()
            .filter(p -> p.getType() != ParamType.OTHERS.getValue())
            .filter(p -> p.getType() != ParamType.STRING.getValue() || !p.getValue().equals("NULL_VALUE"))
            .map(p -> String.format("agent.setParameter(\"%s\", %s, false);", p.getKey(), p.getWrappedValue()))
            .collect(Collectors.toList());

        return String.join("\n", statements);
    }
}
