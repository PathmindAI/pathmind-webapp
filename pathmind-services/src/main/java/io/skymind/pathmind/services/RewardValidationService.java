package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.RewardVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static io.skymind.pathmind.shared.utils.VariableParserUtils.isArray;
import static io.skymind.pathmind.shared.utils.VariableParserUtils.removeArrayIndexFromVariableName;

@Slf4j
@Service
public class RewardValidationService {
    public List<String> validateRewardFunction(String rewardFunction, List<RewardVariable> rewardVariables) {
        final ArrayList<String> errors = new ArrayList<>();
        if (containsOnlyComments(rewardFunction)) {
            errors.add("Training cannot be started when the reward function consists of only comments.");
            return errors;
        }
        final String code = fillInTemplate(rewardFunction, rewardVariables);
        final String[] lines = code.split("\n");
        int startReward = 0;
        int endReward = 0;
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].equals("// START CUSTOM RESET 0119724160")) startReward = i;
            if(lines[i].equals("// END CUSTOM RESET 0119724160")) endReward = i;
        }

        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.getTask(null, null, diagnostics, null, null, Arrays.asList(
                new CharSequenceJavaFileObject("Environment", code)
        )).call();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            if(diagnostic.getLineNumber() >= startReward && diagnostic.getLineNumber() <= endReward){
                errors.add(diagnostic.getKind() +": Line "+(diagnostic.getLineNumber() - startReward - 1)+": "+diagnostic.getMessage(Locale.ROOT));
            }
        }
        // getDiagnostics() should be empty if everything is fine,
        // otherwise, return a generic error
        if (errors.isEmpty() && !diagnostics.getDiagnostics().isEmpty()) {
            errors.add("ERROR: Invalid reward function");
        }
        return errors;
    }

    private static Boolean containsOnlyComments(String rewardFunction) {
        final String multilineCommentRe = "(?:\\/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+\\/)";
        final String commentRe = "\\/\\*(.|[\\r\\n])*?\\*\\/|(\\/\\/).+";
        String processedRewardFunction = rewardFunction;
        processedRewardFunction = processedRewardFunction.replaceAll(multilineCommentRe, "");
        processedRewardFunction = processedRewardFunction.replaceAll(commentRe, "");
        processedRewardFunction = processedRewardFunction.replaceAll("[\\s\\r\\n]", "");
        return processedRewardFunction.isEmpty();
    }

    private static String fillInTemplate(String rewardFunction, List<RewardVariable> rewardVariables){
        return "package pathmind;\n" +
                "\n" +
                "public class Environment {\n" +
                "    \n" +
                "    public boolean isSkip(long agentId) {\n" +
                "        return false;\n" +
                "    }\n" +
                "    \n" +
                "    public boolean isDone(long agentId) {\n" +
                "        return false;\n" +
                "    }\n" +
                "    \n" +
                "    public long getNumberOfAgents() {\n" +
                "        return 0;\n" +
                "    }\n" +
                "    \n" +
                "    public void step() {\n" +
                "    }\n" +
                "    \n" +
                "    public double[] test() {\n" +
                "        return null;\n" +
                "    }\n" +
                "    public float step(long action) {\n" +
                "        double reward = 0;\n" +
                "        Model before = new Model();\n" +
                "        Model after = new Model();\n" +
                "\n" +
                "\n" +
                "// START CUSTOM RESET 0119724160\n" +
                rewardFunction +
                "\n" +
                "// END CUSTOM RESET 0119724160\n" +
                "\n" +
                "        return (float)reward;\n" +
                "    }\n" +
                "\n" +
                createCustomClassTemplate(rewardVariables) +
                "}\n";
    }

    private static String createCustomClassTemplate(List<RewardVariable> rewardVariables) {
        StringBuilder builder = new StringBuilder();
        builder.append("    private static class Model {\n");
        normalizeVariables(rewardVariables)
            .forEach(rv -> builder.append(String.format("        public %s %s;\n", rv.getDataType(), rv.getName())));
        builder.append("    }\n");
        return builder.toString();
    }
    
    private static List<RewardVariable> normalizeVariables(List<RewardVariable> rewardVariables){
        List<RewardVariable> normalizedRewardVariables = new ArrayList<>();
        for (RewardVariable rewardVariable : rewardVariables) {
            if (isArray(rewardVariable.getName())) {
                String arrayName = removeArrayIndexFromVariableName(rewardVariable.getName());
                boolean alreadyExist = normalizedRewardVariables.stream().anyMatch(rv -> rv.getName().equals(arrayName));
                if (!alreadyExist) {
                    RewardVariable arrayVariable = new RewardVariable();
                    arrayVariable.setName(arrayName);
                    arrayVariable.setDataType(rewardVariable.getDataType() + "[]");
                    normalizedRewardVariables.add(arrayVariable);
                }
            } else {
                normalizedRewardVariables.add(rewardVariable);
            }
        }
        return normalizedRewardVariables;
    }
    
    
    private static class CharSequenceJavaFileObject extends SimpleJavaFileObject {

        private final CharSequence content;

        CharSequenceJavaFileObject(String className, CharSequence content) {
            super(URI.create("string:///"+className.replace('.', '/')+JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }
    }
}
