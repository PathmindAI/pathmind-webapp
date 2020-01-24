package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.cloud.rescale.api.RescaleRestApiClient;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.FileReference;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.Job;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.JobAnalysis;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.JobStatus;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import io.skymind.pathmind.services.training.versions.RescaleFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RescaleExecutionProvider implements ExecutionProvider {

    private static final String DEFAULT_RUN_ID = "1";

    private final RescaleRestApiClient client;
    private final RescaleFileManager fileManager;
    private static final List<String> KNOWN_ERROR_MSGS = new ArrayList<>();

    static {
        KNOWN_ERROR_MSGS.add("python3: can\'t open file \'rllibtrain.py\'");
        KNOWN_ERROR_MSGS.add("SyntaxError: invalid syntax");
        KNOWN_ERROR_MSGS.add("Fatal Python error: Segmentation fault");
        KNOWN_ERROR_MSGS.add("Worker crashed during call to train()");
        KNOWN_ERROR_MSGS.add("java.lang.ArrayIndexOutOfBoundsException");
        KNOWN_ERROR_MSGS.add("NotADirectoryError: ");
    }

    public RescaleExecutionProvider(RescaleRestApiClient client) {
        this.client = client;
        this.fileManager = RescaleFileManager.getInstance();
    }

    @Override
    public String execute(JobSpec job) {
        List<String> instructions = new ArrayList<>();
        List<FileReference> files = new ArrayList<>();

        final ExecutionEnvironment env = job.getEnv();

        // Set up which files are needed, and how to install them
        installRllib(env.getRllibVersion(), instructions, files);
        installAnyLogic(env.getAnylogicVersion(), instructions, files);
        installHelper(env.getPathmindHelperVersion(), instructions, files);
        installModel(job.getModelFileId(), instructions, files);
        installCheckpoint(job.getCheckpointFileId(), instructions, files);

        // Set up variables
        setupVariables(job, instructions);

        // Set up instructions to run that specific type of job
        runTraining(instructions);

        // Clean up working directory, so only the required files stay around for automatic saving by rescale
        cleanup(instructions);

        // Check errors
        checkErrors(instructions);

        instructions.add("echo Done  ;\n");

        // Start actual execution of the job
        return startTrainingRun(job, instructions, files);
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

                List<String> errs = client.outputFiles(jobHandle, DEFAULT_RUN_ID).getResults()
                        .parallelStream()
                        .filter(f -> f.getPath().endsWith(TrainingFile.RAY_TRIAL_ERROR) && f.getDecryptedSize() > 0)
                        .map(f -> new String(client.fileContents(f.getId())))
                        .collect(Collectors.toList());

                // since we added error check logic to the script,
                // errors.log will have the same number of line with the number of KNOWN_ERROR_MSGS
                // if the line number is greater than the size of KNOWN_ERROR_MSGS, it has an error
                String errorLogFileContents = new String(client.outputFile(jobHandle, DEFAULT_RUN_ID, TrainingFile.KNOWN_ERROR));
                if (errorLogFileContents != null && !errorLogFileContents.isEmpty()
                        && errorLogFileContents.split("\n").length > KNOWN_ERROR_MSGS.size()) {
                    errs.add("error!");
                }

                if (status.getStatusReason().equals("Completed successfully") && errs.size() == 0) {
                    return RunStatus.Completed;
                } else {
                    if (errs.size() > 0) {
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
        return progress(jobHandle, runStatus);
    }

    @Override
    public Map<String, String> progress(String jobHandle, RunStatus runStatus) {
        if (runStatus.equals(RunStatus.Completed)) {
            // Job is done, we have to look at finished files
            return client.outputFiles(jobHandle, DEFAULT_RUN_ID).getResults()
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
                return client.workingFiles(jobHandle, DEFAULT_RUN_ID)
                        .parallelStream()
                        .filter(it -> it.getPath().endsWith("progress.csv"))
                        .map(it -> {
                            final String key = new File(it.getPath()).getParentFile().getName();
                            final String contents = new String(client.tail(jobHandle, DEFAULT_RUN_ID, it.getPath()));
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
        return client.outputFiles(jobHandle, DEFAULT_RUN_ID).getResults()
                .stream()
                .filter(it -> it.getPath().endsWith("policy_" + trainingRun + ".zip"))
                .map(it -> client.fileContents(it.getId()))
                .findFirst().orElse(null);
    }

    @Override
    public Map.Entry<@NotNull String, byte[]> snapshot(String jobHandle, String trainingRun) {
        return client.outputFiles(jobHandle, DEFAULT_RUN_ID).getResults()
                .stream()
                .filter(it -> it.getPath().endsWith(trainingRun + "/checkpoint.zip"))
                .map(it -> Map.entry(it.getId(), client.fileContents(it.getId())))
                .findFirst().orElse(null);
    }

    @Override
    public String console(String jobHandle) {
        final RunStatus runStatus = status(jobHandle);

        if (runStatus.equals(RunStatus.Completed)) {
            return client.consoleOutput(jobHandle, DEFAULT_RUN_ID);
        } else if (runStatus.equals(RunStatus.Running)) {
            return client.tailConsole(jobHandle, DEFAULT_RUN_ID);
        }

        return null;
    }

    /**
     * get the contents for the given files regardless of the status of job(running or complete)
     *
     * @param jobHandle
     * @param fileName
     * @return
     */
    public String getFileAnytime(String jobHandle, String fileName) {
        try {
            return client.workingFiles(jobHandle, DEFAULT_RUN_ID)
                    .parallelStream()
                    .filter(it -> it.getPath().endsWith(fileName))
                    .findAny()
                    .map(it -> new String(client.tail(jobHandle, DEFAULT_RUN_ID, it.getPath())))
                    .get();
        } catch (Exception e) {
            try {
                log.debug("getFileAnytime tail: " + e.getMessage(), e);

                return client.outputFiles(jobHandle, DEFAULT_RUN_ID)
                        .getResults()
                        .parallelStream()
                        .filter(it -> it.getPath().endsWith(fileName))
                        .findAny()
                        .map(it -> new String(client.fileContents(it.getId())))
                        .get();
            } catch (Exception e1) {
                log.debug("getFileAnytime output: " + e1.getMessage(), e1);
                return null;
            }
        }
    }

    @Override
    public String uploadModel(byte[] modelFile) {
        try {
            return client.fileUpload(modelFile, "model.zip").getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadCheckpoint(byte[] checkpointFile) {
        try {
            return client.fileUpload(checkpointFile, "checkpoint.zip").getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private String varExp(String name, String value) {
        return "export " + name + "=" + value.replace("'", "\\'");
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

    private void runTraining(List<String> instructions) {
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
                        "  cp ../../*.json $OLDPWD/../output/; \n"+
                        "  zip -r $OLDPWD/../output/policy_$(basename `dirname $DIR`).zip .;\n" +
                        "  cd $OLDPWD;\n" +
                        "  cp trial_* ../output;\n" +
                        "  cd `find \"$DIR\"/.. -iname checkpoint_* -type d | sort -V | tail -1`;\n"+
                        "  zip $OLDPWD/../output/$(basename `dirname $DIR`)/checkpoint.zip ./* ;\n"+
                        "  cd $OLDPWD;\n" +
                "done"
        ));
    }

    private void cleanup(List<String> instructions) {
        instructions.addAll(Arrays.asList(
                "cd ..",
                "rm -rf work conda jdk8u222-b10"
        ));
    }

    private void installCheckpoint(String checkpointId, List<String> instructions, List<FileReference> files) {
        if (checkpointId != null) {
            files.add(new FileReference(checkpointId, false));

            instructions.addAll(Arrays.asList(
                    "mkdir checkpoint",
                    "unzip ../checkpoint.zip -d checkpoint",
                    "rm ../checkpoint.zip"
            ));

            instructions.add(
                    varExp("CHECKPOINT", "$(find checkpoint -name \"checkpoint-*\" ! -name \"checkpoint-*.*\")")
            );
        }
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

                files.addAll(this.fileManager.getFiles(rllibVersion));
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

                files.addAll(this.fileManager.getFiles(anylogicVersion));
                break;
            case VERSION_8_5_1:
                instructions.addAll(Arrays.asList(
                        "unzip baseEnv.zip",
                        "rm baseEnv.zip",
                        "mv baseEnv/* work/",
                        "rm -r baseEnv"
                ));

                files.addAll(this.fileManager.getFiles(anylogicVersion));
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

                files.addAll(this.fileManager.getFiles(pathmindHelperVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported Pathmind Helper Version: " + pathmindHelperVersion);
        }
    }

    private void checkErrors(List<String> instructions) {
        instructions.addAll(KNOWN_ERROR_MSGS.stream()
                .map(msg -> "grep -m 2 \"" + msg + "\" " + TrainingFile.RESCALE_LOG + " >> " + TrainingFile.KNOWN_ERROR)
                .collect(Collectors.toList()));
    }
}