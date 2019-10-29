#!/usr/bin/env bash

if [[ $# -ne 2 ]] ; then
    echo 'set working directory and library path'
    exit 1
fi

workDir=$1
libDir=$2
cd $workDir

export JAVA_HOME=$libDir/jdk8u222-b10 ;
export JDK_HOME=$JAVA_HOME ;
export JRE_HOME=$JAVA_HOME/jre ;
export PATH=$JAVA_HOME/bin:$PATH ;

export MODEL_PACKAGE=$(unzip -l model.jar | grep Main.class | awk '{print $4}' | xargs dirname)
export MODEL_PACKAGE_NAME=$(echo $MODEL_PACKAGE | sed 's/\//\./g')
export ENVIRONMENT_CLASS="$MODEL_PACKAGE_NAME.PathmindEnvironment"
export AGENT_CLASS="$MODEL_PACKAGE_NAME.Main"
PHYSICAL_CPU_COUNT=$(lscpu -p | egrep -v '^#' | sort -u -t, -k 2,4 | wc -l)
let WORKERS=$PHYSICAL_CPU_COUNT-1
export NUM_WORKERS=$WORKERS
export OUTPUT_DIR=$(pwd)

mkdir -p $MODEL_PACKAGE

cat <<EOF > $MODEL_PACKAGE/Training.java
package $MODEL_PACKAGE_NAME;
import com.anylogic.engine.AgentConstants;
import com.anylogic.engine.AnyLogicInternalCodegenAPI;
import com.anylogic.engine.Engine;
import com.anylogic.engine.ExperimentCustom;
import com.anylogic.engine.Utilities;

public class Training extends ExperimentCustom {
    @AnyLogicInternalCodegenAPI
    public static String[] COMMAND_LINE_ARGUMENTS_xjal = new String[0];

    public Training(Object parentExperiment) {
        super(parentExperiment);
        this.setCommandLineArguments_xjal(COMMAND_LINE_ARGUMENTS_xjal);
    }

    public void run() {
    }

    @AnyLogicInternalCodegenAPI
    public void setupEngine_xjal(Engine engine) {
        Simulation sim = new Simulation();
        sim.setupEngine(engine);
        sim.initDefaultRandomNumberGenerator(engine);
    }

    @AnyLogicInternalCodegenAPI
    public static void main(String[] args) {
        COMMAND_LINE_ARGUMENTS_xjal = args;
        Utilities.prepareBeforeExperimentStart_xjal(Training.class);
        Training ex = new Training((Object)null);
        ex.setCommandLineArguments_xjal(args);
        ex.run();
    }
}
EOF

export CLASSPATH=$(find $libDir -iname '*.jar' -printf '%p:')
export CLASSPATH=$PWD:$PWD/model.jar:$CLASSPATH

java ai.skymind.nativerl.AnyLogicHelper \
    --environment-class-name "$ENVIRONMENT_CLASS" \
    --agent-class-name "$AGENT_CLASS" \
    --policy-helper RLlibPolicyHelper


cat <<EOF > $MODEL_PACKAGE/VerifySettings.java
package $MODEL_PACKAGE_NAME;

public class VerifySettings extends PathmindEnvironment {
    public static void main(String[] args) {
        PathmindEnvironment e = new PathmindEnvironment(null);
        e.reset();
        System.out.println(e.agent.pathmindHelper.possibleActionCount);
        System.out.println(e.agent.pathmindHelper.observationForTraining().length);
        System.exit(0);
    }
}
EOF


javac $(find -iname '*.java')

java $MODEL_PACKAGE/VerifySettings