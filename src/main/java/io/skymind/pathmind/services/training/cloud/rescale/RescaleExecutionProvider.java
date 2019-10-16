package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.cloud.rescale.api.RescaleRestApiClient;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.*;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RescaleExecutionProvider implements ExecutionProvider {
    private Logger log = LogManager.getLogger(RescaleExecutionProvider.class);

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
                    "uLWose", // nativerl-1.0.0-SNAPSHOT-bin.zip, 2019-10-08 DH version
                    "fDRBHd"  // OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz
            )
    );

    private final RescaleRestApiClient client;
    private final RescaleMetaDataService metaDataService;

    public RescaleExecutionProvider(RescaleRestApiClient client, RescaleMetaDataService metaDataService) {
        this.client = client;
        this.metaDataService = metaDataService;
    }

    @Override
    public String execute(JobSpec job) {
        List<String> instructions = new ArrayList<>();
        List<FileReference> files = new ArrayList<>();

        // Get model file id, either uploading it if necessary, or just collecting it form the spec if available
        String modelId = uploadModelIfNeeded(job);

        final ExecutionEnvironment env = job.getEnv();

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
        final String rescaleJobId = startTrainingRun(job, instructions, files);
        metaDataService.put(metaDataService.runIdKey(job.getRunId()), rescaleJobId);
        return rescaleJobId;
    }

    @Override
    public void stop(String jobHandle) {
        client.jobStop(jobHandle);
    }

    @Override
    public RunStatus status(String jobHandle) {
        final List<JobStatus> statuses = client.jobStatusHistory(jobHandle).getResults();

        if (statuses.size() > 0) {
            if (statuses.stream().anyMatch(it -> it.getStatus().equals("Completed"))) {
                final JobStatus status = statuses.stream().filter(it -> it.getStatus().equals("Completed")).findFirst().get();

                // file check that has an error message
                Map<String, String> map = client.outputFiles(jobHandle, "1").getResults()
                        .parallelStream()
                        .filter(it -> it.getPath().endsWith("process_output.log"))
                        .map(it -> {
                            final String key = new File(it.getPath()).getParentFile().getName();
                            final String contents = new String(client.fileContents(it.getId()));
                            return Map.entry(key, contents);
                        })
                        // this is the known error message so far, todo i will revisit below after risecamp
                        // raw error logs are https://3.basecamp.com/3684163/buckets/11875773/vaults/2132519274
                        .filter(it ->
                                it.getValue().contains("python3: can't open file 'rllibtrain.py': [Errno 2] No such file or directory")
                                || it.getValue().contains("SyntaxError: invalid syntax")
                                || it.getValue().contains("Fatal Python error: Aborted")
                                || it.getValue().contains("Fatal Python error: Segmentation fault")
                                || it.getValue().contains("Worker crashed during call to train()")
                        )
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                if (status.getStatusReason().equals("Completed successfully") && map.size() == 0) {
                    return RunStatus.Completed;
                } else {
                    if (map.size() > 0) {
                        log.info(jobHandle + " will be considered as an error");
                    }
                    return RunStatus.Error;
                }
            } else if (statuses.stream().anyMatch(it -> it.getStatus().equals("Executing"))) {
                return RunStatus.Running;
            }
        }

        return RunStatus.Starting;
    }

    @Override
    public Map<String, String> progress(String jobHandle) {
        final RunStatus runStatus = status(jobHandle);
        if (runStatus.equals(RunStatus.Completed)) {
            // Job is done, we have to look at finished files
            return client.outputFiles(jobHandle, "1").getResults()
                    .parallelStream()
                    .filter(it -> it.getPath().endsWith("progress.csv"))
                    .map(it -> {
                        final String key = new File(it.getPath()).getParentFile().getName();
                        final String contents = new String(client.fileContents(it.getId()));
                        return Map.entry(key, contents);
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        } else if (runStatus.equals(RunStatus.Running)) {
            // Job is still running, we have to tail files
            try {
                return client.workingFiles(jobHandle, "1")
                        .parallelStream()
                        .filter(it -> it.getPath().endsWith("progress.csv"))
                        .map(it -> {
                            final String key = new File(it.getPath()).getParentFile().getName();
                            final String contents = new String(client.tail(jobHandle, "1", it.getPath()));
                            return Map.entry(key, contents);
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            } catch (Exception e) {
                log.debug("Errors on getting progress.csv" , e);
                return Collections.emptyMap();
            }
        }

        return Collections.emptyMap();
    }

    @Override
    public byte[] policy(String jobHandle, String trainingRun) {
        final RunStatus runStatus = status(jobHandle);

        if (runStatus.equals(RunStatus.Completed)) {
            return client.outputFiles(jobHandle, "1").getResults()
                    .stream()
                    .filter(it -> it.getPath().endsWith("policy_" + trainingRun + ".zip"))
                    .map(it -> client.fileContents(it.getId()))
                    .findFirst().orElseGet(() -> null);
        }

        return null;
    }

    @Override
    public String console(String jobHandle) {
        final RunStatus runStatus = status(jobHandle);

        if (runStatus.equals(RunStatus.Completed)) {
            return client.consoleOutput(jobHandle, "1");
        } else if (runStatus.equals(RunStatus.Running)) {
            return client.tailConsole(jobHandle, "1");
        }

        return null;
    }

    public String consoleAnytime(String jobHandle) {
        try {
            return client.tailConsole(jobHandle, "1");
        } catch (Exception e) {
            try {
                log.debug("consoleAnytime tail: " + e.getMessage(), e);
                return client.consoleOutput(jobHandle, "1");
            } catch (Exception e1) {
                log.debug("consoleAnytime output: " + e1.getMessage(), e1);
                return null;
            }
        }
    }


    /**
     * Get Model file from database if it wasn't uploaded yet.
     */
    private String uploadModelIfNeeded(JobSpec job) {
        final String fileKey = metaDataService.modelFileKey(job.getModelId());
        String fileId = metaDataService.get(fileKey, String.class);
        if (fileId == null) {
            final RescaleFile rescaleFile;
            try {
                rescaleFile = client.fileUpload(job.getModelFile(), "model.zip");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            metaDataService.put(fileKey, rescaleFile.getId());
            fileId = rescaleFile.getId();
        }

        return fileId;
    }

    private String startTrainingRun(JobSpec job, List<String> instructions, List<FileReference> files) {
        final Job rescaleJob = Job.create(
                String.format("user-%d-model-%d-rllib-%d-run-%d", job.getUserId(), job.getModelId(), job.getExperimentId(), job.getRunId()),
                JobAnalysis.create(String.join(" ;\n", instructions), files)
        );

        final Job created = client.jobCreate(rescaleJob);
        client.jobSubmit(created.getId());

        return created.getId();
    }


    private String var(String name, String value) {
        return "export " + name + "='" + value.replace("'", "\\'") + "'";
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
                var("TEST_ITERATIONS", "0"), // disabled for now

                // Not yet picked up by training script
                var("LEARNING_RATES", job.getLearningRates().stream().map(Object::toString).collect(Collectors.joining(","))),
                var("GAMMAS", job.getGammas().stream().map(Object::toString).collect(Collectors.joining(","))),
                var("BATCH_SIZES", job.getBatchSizes().stream().map(Object::toString).collect(Collectors.joining(","))),

                // Still has to be set, but doesn't actually do something, needs to be removed from train.sh
                var("STEP_TIME", "1"),
                var("STOP_TIME", "420"),
                var("TIME_UNIT", "MINUTE"),
                var("MAX_TIME_IN_SEC", String.valueOf(job.getMaxTimeInSec()))
        ));
    }

    private void runTraining(JobSpec job, List<String> instructions) {
        instructions.addAll(Arrays.asList(
                // ensure empty files are there as needed
                "echo > setup.sh",
                "mkdir -p database",
                "touch database/db.properties",

                // actually start training
                "source train.sh",

                // temporary workaround, as train.sh as it is in nativerl with id doRCLd, only takes care of a single policy file
                // by doing this here, we can iterate a bit quicker
                "mkdir -p ../output",
                "for DIR in `find \"$OUTPUT_DIR\" -iname model -type d`; do \n" +
                        "  cd $DIR;\n" +
                        "  mkdir -p $OLDPWD/../output/$(basename `dirname $DIR`)/;\n" +
                        "  cp ../progress.csv $OLDPWD/../output/$(basename `dirname $DIR`)/; \n"+
                        "  zip -r $OLDPWD/../output/policy_$(basename `dirname $DIR`).zip .;\n" +
                        "  cd $OLDPWD;\n" +
                "done"
        ));
    }

    private void cleanup(JobSpec job, List<String> instructions) {
        instructions.addAll(Arrays.asList(
                "cd ..",
                "rm -rf work conda jdk8u222-b10"
        ));
    }

    private void installModel(String modelId, List<String> instructions, List<FileReference> files) {
        files.add(new FileReference(modelId, false));
        instructions.addAll(Arrays.asList(
                "cd work",
                "unzip ../model.zip",
                "rm ../model.zip"
        ));
    }

    private void installRllib(RLLib rllibVersion, List<String> instructions, List<FileReference> files) {
        switch (rllibVersion) {
            case VERSION_0_7_0:
                instructions.addAll(Arrays.asList(
                        // Setup JVM
                        "tar xf OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz",
                        "rm -rf OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz",
                        "export JAVA_HOME=`pwd`/jdk8u222-b10",
                        "export JDK_HOME=$JAVA_HOME",
                        "export JRE_HOME=$JAVA_HOME/jre",
                        "export PATH=$JAVA_HOME/bin:$PATH",
                        "export LD_LIBRARY_PATH=$JAVA_HOME/jre/lib/amd64/server:$JAVA_HOME/jre/lib/amd64/:$LD_LIBRARY_PATH",

                        // Setup Anaconda
                        "mkdir conda",
                        "cd conda",
                        "tar xf ../rllibpack.tar.gz",
                        "rm ../rllibpack.tar.gz",
                        "source bin/activate",
                        "cd ..",

                        // Setup NativeRL
                        "mkdir work",
                        "cd work",
                        "unzip ../nativerl-1.0.0-SNAPSHOT-bin.zip",
                        "rm ../nativerl-1.0.0-SNAPSHOT-bin.zip",
                        "mv nativerl-bin/* .",
                        "mv examples/train.sh .",
                        "cd .."
                ));
                files.addAll(rllibMap.getOrDefault(rllibVersion, List.of())
                        .stream()
                        .map(it -> new FileReference(it, false))
                        .collect(Collectors.toList()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported RLLib Version: " + rllibVersion);
        }
    }

    private void installAnyLogic(AnyLogic anylogicVersion, List<String> instructions, List<FileReference> files) {
        switch (anylogicVersion) {
            case VERSION_8_5:
                instructions.addAll(Arrays.asList(
                        "unzip baseEnv.zip",
                        "rm baseEnv.zip",
                        "mv baseEnv/* work/",
                        "rm -r baseEnv"
                ));
                files.addAll(anylogicMap.getOrDefault(anylogicVersion, List.of())
                        .stream()
                        .map(it -> new FileReference(it, false))
                        .collect(Collectors.toList()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported AnyLogic Version: " + anylogicVersion);
        }
    }

    private void installHelper(PathmindHelper pathmindHelperVersion, List<String> instructions, List<FileReference> files) {
        switch (pathmindHelperVersion) {
            case VERSION_0_0_24:
                instructions.addAll(Arrays.asList(
                        "mv PathmindPolicy.jar work/lib/"
                ));
                files.addAll(pathmindHelperMap.getOrDefault(pathmindHelperVersion, List.of())
                        .stream()
                        .map(it -> new FileReference(it, false))
                        .collect(Collectors.toList()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported Pathmind Helper Version: " + pathmindHelperVersion);
        }
    }


}
