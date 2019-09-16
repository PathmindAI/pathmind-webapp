package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.metadata.ExecutionProviderMetaDataService;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.cloud.rescale.api.*;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RescaleExecutionProvider implements ExecutionProvider {
    private static final Map<PathmindHelper, List<String>> pathmindHelperMap = Map.of(
            PathmindHelper.VERSION_0_0_24, Arrays.asList(
                    "kuQJAd" // PathmindPolicy.jar, 2019-08-28
            )
    );

    private static final Map<AnyLogic, List<String>> anylogicMap = Map.of(
            AnyLogic.VERSION_8_5, Arrays.asList(
                    "nbwVpg" // Anylogic 8.5 Base Environment: baseEnv.zip
            )
    );

    private static final Map<RLLib, List<String>> rllibMap = Map.of(
            RLLib.VERSION_0_7_0, Arrays.asList(
                    "LZAENb", // conda
                    "doRCLd", // nativerl-1.0.0-SNAPSHOT-bin.zip, 2019-08-22
                    "fDRBHd"  // OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz
            )
    );

    private final RescaleRestApiClient client;
    private final ExecutionProviderMetaDataService metaDataService;

    public RescaleExecutionProvider(RescaleRestApiClient client, ExecutionProviderMetaDataService metaDataService) {
        this.client = client;
        this.metaDataService = metaDataService;
    }

    @Override
    public String execute(JobSpec job, ExecutionEnvironment env) {
        List<String> instructions = new ArrayList<>();
        List<FileReference> files = new ArrayList<>();

        // Get model file id, either uploading it if necessary, or just collecting it form the spec if available
        String modelId = uploadModelIfNeeded(job);

        // Set up which files are needed, and how to install them
        installRllib(env.getRllibVersion(), instructions, files);
        installAnyLogic(env.getAnylogicVersion(), instructions, files);
        installHelper(env.getPathmindHelperVersion(), instructions, files);
        installModel(modelId, instructions, files);

        // Set up variables
        setupVariables(job, instructions);

        // Set up instructions to run that specific type of job
        runTraining(job, instructions);

        // Clean up working directory, so only the required files stay around for automatic saving by rescale
        cleanup(job, instructions);


        // Start actual execution of the job
        return startTrainingRun(job, instructions, files);
    }


    /**
     * Get Model file from database if it wasn't uploaded yet.
     */
    private String uploadModelIfNeeded(JobSpec job) {
        final String fileKey = "modelFileId:" + job.getModelId();
        String fileId = metaDataService.get(this.getClass(), fileKey, String.class);
        if(fileId == null){
            final RescaleFile rescaleFile;
            try {
                rescaleFile = client.fileUpload(job.getModelInputStream(), "model.zip");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            metaDataService.put(this.getClass(), fileKey, rescaleFile.getId());
            fileId = rescaleFile.getId();
        }

        return fileId;
    }

    private String startTrainingRun(JobSpec job, List<String> instructions, List<FileReference> files) {
        final Job rescaleJob = Job.create(
                "user-" + job.getUserId() + "-model-" + job.getModelId() + "-rllib-" + job.getExperimentId(),
                JobAnalysis.create(String.join(" ;\n", instructions), files)
        );

        final Job created = client.jobCreate(rescaleJob);
        client.jobSubmit(created.getId());

        return created.getId();
    }

    private void cleanup(JobSpec job, List<String> instructions) {
        instructions.addAll(Arrays.asList(
                "mv policy.zip ..;",
                "cd ..;",
                "rm -rf work conda jdk8u222-b10;"
        ));
    }


    private String var(String name, String value){
        return "export "+name+"='"+value.replace("'", "\\'")+"'; ";
    }

    private void setupVariables(JobSpec job, List<String> instructions) {
        instructions.addAll(Arrays.asList(
                var("CLASS_SNIPPET", job.getVariables()),
                var("RESET_SNIPPET", job.getReset()),
                var("REWARD_SNIPPET", job.getReward()),
                var("METRICS_SNIPPET", job.getMetrics()),
                var("DISCRETE_ACTIONS", String.valueOf(job.getActions())),
                var("CONTINUOUS_OBSERVATIONS", String.valueOf(job.getObservations())),
                var("MAX_ITERATIONS", String.valueOf(job.getIterations())),
                var("RANDOM_SEED", "1"),
                var("MAX_REWARD_MEAN", String.valueOf(Integer.MAX_VALUE)), // disabled for now
                var("TEST_ITERATIONS", "0") // disabled for now
        ));
    }

    private void runTraining(JobSpec job, List<String> instructions) {
        instructions.addAll(Arrays.asList(
                // ensure empty files are there as needed
                "echo > setup.sh;",
                "mkdir -p database;",
                "touch database/db.properties;",

                // actually start training
                "source train.sh;"
        ));
    }

    private void installModel(String modelId, List<String> instructions, List<FileReference> files) {
        files.add(new FileReference(modelId, false));
        instructions.addAll(Arrays.asList(
                "cd work;",
                "unzip ../model.zip;",
                "rm ../model.zip;"
        ));
    }

    private void installRllib(RLLib rllibVersion, List<String> instructions, List<FileReference> files) {
        switch (rllibVersion){
            case VERSION_0_7_0:
                instructions.addAll(Arrays.asList(
                        // Setup JVM
                        "tar xf OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz;",
                        "rm -rf OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz;",
                        "export JAVA_HOME=`pwd`/jdk8u222-b10;",
                        "export JDK_HOME=$JAVA_HOME;",
                        "export JRE_HOME=$JAVA_HOME/jre;",
                        "export PATH=$JAVA_HOME/bin:$PATH;",
                        "export LD_LIBRARY_PATH=$JAVA_HOME/jre/lib/amd64/server:$JAVA_HOME/jre/lib/amd64/:$LD_LIBRARY_PATH;",

                        // Setup Anaconda
                        "mkdir conda;",
                        "cd conda;",
                        "tar xf ../rllibpack.tar.gz;",
                        "rm ../rllibpack.tar.gz;",
                        "source bin/activate;",
                        "cd ..;",

                        // Setup NativeRL
                        "mkdir work;",
                        "cd work;",
                        "unzip ../nativerl-1.0.0-SNAPSHOT-bin.zip;" +
                        "rm ../nativerl-1.0.0-SNAPSHOT-bin.zip;" +
                        "mv nativerl-bin/* .;" +
                        "mv examples/train.sh .;",
                        "cd .."
                        ));
                files.addAll(rllibMap.getOrDefault(rllibVersion, List.of())
                        .stream()
                        .map(it -> new FileReference(it, false))
                        .collect(Collectors.toList()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported RLLib Version: "+rllibVersion);
        }
    }

    private void installAnyLogic(AnyLogic anylogicVersion, List<String> instructions, List<FileReference> files) {
        switch (anylogicVersion){
            case VERSION_8_5:
                instructions.addAll(Arrays.asList(
                        "unzip baseEnv.zip;",
                        "rm baseEnv.zip;",
                        "mv baseEnv/* work/;",
                        "rm -r baseEnv;"
                ));
                files.addAll(anylogicMap.getOrDefault(anylogicVersion, List.of())
                        .stream()
                        .map(it -> new FileReference(it, false))
                        .collect(Collectors.toList()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported AnyLogic Version: "+anylogicVersion);
        }
    }

    private void installHelper(PathmindHelper pathmindHelperVersion, List<String> instructions, List<FileReference> files) {
        switch (pathmindHelperVersion){
            case VERSION_0_0_24:
                instructions.addAll(Arrays.asList(
                        "mv PathmindPolicy.jar work/lib/;"
                ));
                files.addAll(pathmindHelperMap.getOrDefault(pathmindHelperVersion, List.of())
                        .stream()
                        .map(it -> new FileReference(it, false))
                        .collect(Collectors.toList()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported Pathmind Helper Version: "+pathmindHelperVersion);
        }
    }


}
