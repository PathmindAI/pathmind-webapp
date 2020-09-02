package io.skymind.pathmind.services.training.cloud.aws;

import java.util.ArrayList;
import java.util.List;

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
    public static String createObservationSnippet(List<String> selectedObservations) {
        assert selectedObservations != null && !selectedObservations.isEmpty();
        List<String> statements = new ArrayList<>();
        statements.add(String.format("out = new double[%s];", selectedObservations.size()));
        for (int i = 0; i < selectedObservations.size(); i++) {
            statements.add(String.format("out[%s] = in.%s;", i, selectedObservations.get(i)));
        }
        return String.join("\n", statements);
    }
}
