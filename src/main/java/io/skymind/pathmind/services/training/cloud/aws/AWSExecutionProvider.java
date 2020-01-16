package io.skymind.pathmind.services.training.cloud.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.ExperimentState;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.versions.AWSFileManager;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AWSExecutionProvider implements ExecutionProvider {
    private final AWSApiClient client;
    private final ObjectMapper objectMapper;
    private final AWSFileManager fileManager;

    private static final String AWS_JOB_ID_PREFIX = "id";

    public AWSExecutionProvider(AWSApiClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.fileManager = AWSFileManager.getInstance();
    }

    @Override
    public String execute(JobSpec job) {
        List<String> instructions = new ArrayList<>();
        List<String> files = new ArrayList<>();

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
//        cleanup(instructions);

        // Check errors
//        checkErrors(instructions);

        // Start actual execution of the job
        return startTrainingRun(job, instructions, files);
    }

    @Override
    public String uploadModel(byte[] modelFile) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public String uploadModel(long runId, byte[] modelFile) {
        File model = null;
        try {
            model = File.createTempFile("pathmind", UUID.randomUUID().toString());
            FileUtils.writeByteArrayToFile(model, modelFile);
            return client.fileUpload(buildJobId(runId)+ "/model.zip", model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (model != null) {
                model.delete();
            }
        }
    }

    @Override
    public void stop(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public RunStatus status(String jobHandle) {
        List<String> errors = getTrialStatus(jobHandle, TrainingFile.RAY_TRIAL_ERROR);
        List<String> completes = getTrialStatus(jobHandle, TrainingFile.RAY_TRIAL_COMPLETE);
        List<String> trials = getTrialStatus(jobHandle, TrainingFile.RAY_TRIAL_LIST).stream()
                .filter(it -> !it.endsWith(".json"))
                .collect(Collectors.toList());

        // todo need to change to use database once Daniel create proper database(TRAINER_JOB)
        ExperimentState experimentState = getExperimentState(jobHandle);

        if (experimentState != null) {
            if (errors.size() > 0) {
                return RunStatus.Error;
            }

            if (completes.size() > 0 && completes.size() == trials.size()) {
                return RunStatus.Completed;
            }

            return RunStatus.Running;
        }

        return RunStatus.Starting;
    }

    @Override
    public Map<String, String> progress(String jobHandle) {
        return client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
                .filter(it -> it.getKey().endsWith("progress.csv"))
                .map(it -> {
                    final String key = new File(it.getKey()).getParentFile().getName();
                    final String contents = new String(client.fileContents(it.getKey()));
                    return Map.entry(key, contents);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, String> progress(String jobHandle, RunStatus runStatus) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public byte[] policy(String jobHandle, String trainingRun) {
        Optional<byte[]> optional = getFile(jobHandle, "policy_" + trainingRun + ".zip");
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Map.Entry<@NotNull String, byte[]> snapshot(String jobHandle, String trainingRun) {
        Optional<byte[]> optional = getFile(jobHandle, "checkpoint.zip");
        return optional.isPresent() ? Map.entry(jobHandle, optional.get()): null;
    }

    @Override
    public String uploadCheckpoint(byte[] checkpointFile) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public String console(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public ExecutionProviderMetaDataDAO.ExecutionProviderClass executionProviderClass() {
        return ExecutionProviderMetaDataDAO.ExecutionProviderClass.AWS;
    }

    public Optional<byte[]> getFile(String jobHandle, String fileName) {
        return client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
                .filter(it -> it.getKey().endsWith(fileName))
                .findAny()
                .map(it -> client.fileContents(it.getKey()));
    }

    public List<String> getTrialStatus(String jobHandle, String fileName) {
        Optional<byte[]> listOpt = getFile(jobHandle, fileName);
        if (listOpt.isPresent()) {
            String contents = new String(listOpt.get());
            if (!contents.isEmpty()) {
                return Arrays.stream(contents.split("\n"))
                    .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    private ExperimentState getExperimentState(String jobHandle) {
        Optional<ExperimentState> expOpt = client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
                .filter(it -> it.getKey().endsWith(".json") && it.getKey().contains("experiment_state-"))
                .findAny()
                .map(it -> {
                    try {
                        return objectMapper.readValue(client.fileContents(it.getKey()), ExperimentState.class);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                        return  null;
                    }
                });

        return expOpt != null && expOpt.isPresent() ? expOpt.get() : null;
    }

    public Map<String, LocalDateTime> getTerminatedTrials(String jobHandle) {
        ExperimentState experimentState = getExperimentState(jobHandle);

        if (experimentState != null) {
            return experimentState.getCheckpoints().stream()
                    .filter(checkPoint -> checkPoint.getStatus().equals("TERMINATED"))
                    .map(it -> {
                        final String key = it.getId();
                        final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.getLastUpdateTime()), ZoneId.systemDefault());
                        return Map.entry(key, date);
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return Collections.emptyMap();
    }

    private void installRllib(RLLib rllibVersion, List<String> instructions, List<String> files) {
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

                files.addAll(fileManager.getFiles(rllibVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported RLLib Version: " + rllibVersion);
        }
    }

    private void installAnyLogic(AnyLogic anylogicVersion, List<String> instructions, List<String> files) {
        switch (anylogicVersion) {
            case VERSION_8_5_1:
                instructions.addAll(Arrays.asList(
                        "unzip baseEnv.zip",
                        "rm baseEnv.zip",
                        "mv baseEnv/* work/",
                        "rm -r baseEnv"
                ));

                files.addAll(fileManager.getFiles(anylogicVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported AnyLogic Version: " + anylogicVersion);
        }
    }

    private void installHelper(PathmindHelper pathmindHelperVersion, List<String> instructions, List<String> files) {
        switch (pathmindHelperVersion) {
            case VERSION_0_0_24:
                instructions.addAll(Arrays.asList(
                        "mv PathmindPolicy.jar work/lib/"
                ));

                files.addAll(fileManager.getFiles(pathmindHelperVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported Pathmind Helper Version: " + pathmindHelperVersion);
        }
    }

    private void installModel(String modelId, List<String> instructions, List<String> files) {
        instructions.addAll(Arrays.asList(
                "cd work",
                "unzip ../model.zip",
                "rm ../model.zip"
        ));
    }

    private void installCheckpoint(String checkpointS3Path, List<String> instructions, List<String> files) {
        if (checkpointS3Path != null) {
            files.add(fileManager.buildCheckpointCopyCmd(checkpointS3Path, "checkpoint.zip"));

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
                "source train.sh"
        ));
    }

    private void cleanup(List<String> instructions) {
        instructions.addAll(Arrays.asList(
                "cd ..",
                "rm -rf work conda jdk8u222-b10"
        ));
    }

    private String startTrainingRun(JobSpec job, List<String> instructions, List<String> files) {
        File script = null;
        try {
            script = File.createTempFile("pathmind", UUID.randomUUID().toString());
            files.addAll(instructions);
            String scriptStr = String.join(" ;\n", files);

            FileUtils.writeStringToFile(script, scriptStr, Charset.defaultCharset());

            String jobId = buildJobId(job.getRunId());

            client.fileUpload(jobId + "/script.sh", script);
            return client.jobSubmit(jobId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (script != null) {
                script.delete();
            }
        }
    }

    private String buildJobId(long runId) {
        return AWS_JOB_ID_PREFIX + runId;
    }
}
