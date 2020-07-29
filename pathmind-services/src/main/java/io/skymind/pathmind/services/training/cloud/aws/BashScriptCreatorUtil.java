package io.skymind.pathmind.services.training.cloud.aws;

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
}
