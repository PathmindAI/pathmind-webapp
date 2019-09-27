package io.skymind.pathmind.services;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RewardValidationService {

    public static List<String> validateRewardFunction(String rewardFunction){
        final String code = fillInTemplate(rewardFunction);
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
                new CharSequenceJavaFileObject("PathmindEnvironment", code)
        )).call();

        final ArrayList<String> errors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            if(diagnostic.getLineNumber() >= startReward && diagnostic.getLineNumber() <= endReward){
                errors.add(diagnostic.getKind() +": Line "+(diagnostic.getLineNumber() - startReward - 1)+": "+diagnostic.getMessage(Locale.ROOT));
            }
        }

        return errors;
    }

    private static String fillInTemplate(String rewardFunction){
        // This should be as close to the actual code that we are using for training as possible.
        return "package pathmind;\n" +
                "import ai.skymind.nativerl.*;\n" +
                "import com.anylogic.engine.*;\n" +
                "import java.io.File;\n" +
                "import java.nio.charset.Charset;\n" +
                "import java.nio.file.Files;\n" +
                "import java.nio.file.Paths;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.Arrays;\n" +
                "import pathmind.policyhelper.PathmindHelperRegistry;\n" +
                "\n" +
                "public class PathmindEnvironment extends AbstractEnvironment {\n" +
                "    final static Training experiment = new Training(null);\n" +
                "    protected Engine engine;\n" +
                "    protected interconnected_call_centers_ai.Main agent;\n" +
                "    protected PolicyHelper policyHelper;\n" +
                "\n" +
                "\n" +
                "    public PathmindEnvironment() {\n" +
                "        super(125, 70);\n" +
                "        System.setProperty(\"ai.skymind.nativerl.disablePolicyHelper\", \"true\");\n" +
                "    }\n" +
                "\n" +
                "    public PathmindEnvironment(PolicyHelper policyHelper) {\n" +
                "        super(125, 70);\n" +
                "        this.policyHelper = policyHelper;\n" +
                "    }\n" +
                "\n" +
                "    @Override public void close() {\n" +
                "        super.close();\n" +
                "\n" +
                "        // Destroy the model:\n" +
                "        engine.stop();\n" +
                "    }\n" +
                "\n" +
                "    @Override public Array getObservation() {\n" +
                "        double[] obs = PathmindHelperRegistry.getHelper().observationForTraining();\n" +
                "        float[] array = new float[obs.length];\n" +
                "        for (int i = 0; i < obs.length; i++) {\n" +
                "            array[i] = (float)obs[i];\n" +
                "        }\n" +
                "        observation.data().put(array);\n" +
                "        return observation;\n" +
                "    }\n" +
                "\n" +
                "    @Override public boolean isDone() {\n" +
                "        return PathmindHelperRegistry.getHelper().isDone();\n" +
                "    }\n" +
                "\n" +
                "    @Override public void reset() {\n" +
                "        if (engine != null) {\n" +
                "            engine.stop();\n" +
                "        }\n" +
                "        // Create Engine, initialize random number generator:\n" +
                "        engine = experiment.createEngine();\n" +
                "        Simulation sim = new Simulation();\n" +
                "        sim.setupEngine(engine);\n" +
                "        sim.initDefaultRandomNumberGenerator(engine);\n" +
                "        // Create new agent object:\n" +
                "        agent = new interconnected_call_centers_ai.Main(engine, null, null);\n" +
                "        agent.setParametersToDefaultValues();\n" +
                "        PathmindHelperRegistry.setForceLoadPolicy(policyHelper);\n" +
                "\n" +
                "\n" +
                "        engine.start(agent);\n" +
                "        engine.start(agent);\n" +
                "    }\n" +
                "\n" +
                "    @Override public float step(long action) {\n" +
                "        double reward = 0;\n" +
                "        engine.runFast();\n" +
                "        double[] before = PathmindHelperRegistry.getHelper().observationForReward();\n" +
                "        PathmindHelperRegistry.getHelper().doAction((int)action);\n" +
                "        engine.runFast();\n" +
                "        double[] after = PathmindHelperRegistry.getHelper().observationForReward();\n" +
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
                "    public double[] test() {\n" +
                "        double[] metrics = null;\n" +
                "        reset();\n" +
                "        while (!isDone()) {\n" +
                "            engine.runFast();\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        return metrics;\n" +
                "    }\n" +
                "\n" +
                "    public static void main(String[] args) throws Exception {\n" +
                "        PathmindEnvironment e = new PathmindEnvironment(new RLlibPolicyHelper(new File(args[0])));\n" +
                "        ArrayList<String> lines = new ArrayList<String>(0);\n" +
                "        for (int i = 0; i < 0; i++) {\n" +
                "            lines.add(Arrays.toString(e.test()));\n" +
                "        }\n" +
                "        Files.write(Paths.get(args[0], \"metrics.txt\"), lines, Charset.defaultCharset());\n" +
                "    }\n" +
                "}\n";
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
