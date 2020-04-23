package io.skymind.pathmind.services.training.cloud.aws;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.CheckPoint;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.ExperimentState;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.versions.AWSFileManager;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.services.training.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.ExecutionProviderClass;
import io.skymind.pathmind.shared.services.training.JobSpec;
import io.skymind.pathmind.shared.services.training.versions.*;
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

import static io.skymind.pathmind.shared.constants.RunStatus.Error;

@Service
@Slf4j
public class AWSExecutionProvider implements ExecutionProvider {
    private final AWSApiClient client;
    private final ObjectMapper objectMapper;
    private final AWSFileManager fileManager;
    private final TrainingErrorDAO trainingErrorDAO;

    private static final String AWS_JOB_ID_PREFIX = "id";

    public AWSExecutionProvider(AWSApiClient client, ObjectMapper objectMapper, TrainingErrorDAO trainingErrorDAO) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.trainingErrorDAO = trainingErrorDAO;
        this.fileManager = AWSFileManager.getInstance();
    }

    @Override
    public String execute(JobSpec job) {
        List<String> instructions = new ArrayList<>();
        List<String> files = new ArrayList<>();

        final ExecutionEnvironment env = job.getEnv();

        // Set up which files are needed, and how to install them
        installJDK(env.getJdkVersion(), instructions, files);
        installConda(env.getCondaVersion(), instructions, files);
        installNativeRL(env.getRllibVersion(), instructions, files);
        installAnyLogic(env.getAnylogicVersion(), instructions, files);
        installHelper(env.getPathmindHelperVersion(), instructions, files);
        installModel(job.getModelFileId(), instructions, files);
        installCheckpoint(job.getCheckpointFileId(), instructions, files);

        // Set up variables
        setupVariables(job, instructions);

        // Set up instructions to run that specific type of job
        runTraining(instructions);

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
        File emptyFile = null;
        try {
            emptyFile = File.createTempFile("pathmind", UUID.randomUUID().toString());
            client.fileUpload(jobHandle + "/output/" + TrainingFile.STOPPING, emptyFile);
            client.jobStop(jobHandle);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        } finally {
            if (emptyFile != null) {
                emptyFile.delete();
            }
        }
    }

    @Override
    public ProviderJobStatus status(String jobHandle) {
        if (outputExist(jobHandle)){
            boolean killed = getFile(jobHandle, TrainingFile.KILLED).isPresent();
            if (killed) {
                return ProviderJobStatus.KILLED;
            }
            boolean stopping = getFile(jobHandle, TrainingFile.STOPPING).isPresent();
            if (stopping) {
                return ProviderJobStatus.STOPPING;
            }

            boolean restarting = getFile(jobHandle, TrainingFile.RESTARTING).isPresent();
            boolean restarted = getFile(jobHandle, TrainingFile.RESTARTED).isPresent();
            if (restarting && !restarted) {
                return ProviderJobStatus.RESTARTING;
            }

            if (restarted) {
                log.debug(jobHandle + " is restarted!!");
            }

            ExperimentState experimentState = getExperimentState(jobHandle);
            List<String> knownErrsCheck = getTrialStatus(jobHandle, TrainingFile.KNOWN_ERROR);

            Map<String, Long> trialStatusCount = Collections.emptyMap();
            if (experimentState != null) {
                trialStatusCount = experimentState.getCheckpoints().stream()
                        .collect(Collectors.groupingBy(CheckPoint::getStatus, Collectors.counting()));
            }

            if (trialStatusCount.getOrDefault("ERROR", 0L) > 0 || knownErrsCheck.size() > 0) {
                final var allErrorsList = knownErrsCheck.stream()
                        .collect(Collectors.toList());
                var oneLineErrors = allErrorsList.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" ; "));
                log.warn("{} error(s) detected for the AWS jobHandle {}: {}", allErrorsList.size(), jobHandle, oneLineErrors);
                return new ProviderJobStatus(Error, allErrorsList);
            }

            if (experimentState != null && experimentState.getCheckpoints() != null && experimentState.getCheckpoints().size() == trialStatusCount.getOrDefault("TERMINATED", 0L)) {
                return ProviderJobStatus.COMPLETED;
            }

            return ProviderJobStatus.RUNNING;
        }

        return ProviderJobStatus.STARTING;
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
    public ExecutionProviderClass executionProviderClass() {
        return ExecutionProviderClass.AWS;
    }

    public Optional<byte[]> getFile(String jobHandle, String fileName) {
        return client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
                .filter(it -> it.getKey().endsWith(fileName))
                .findAny()
                .map(it -> client.fileContents(it.getKey()));
    }

    public boolean outputExist(String jobHandle) {
        return client.listObjects(jobHandle + "/output").getObjectSummaries().size() > 0;
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
                .map(S3ObjectSummary::getKey)
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .map(it -> {
                    try {
                        return objectMapper.readValue(client.fileContents(it), ExperimentState.class);
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

    private void installNativeRL(NativeRL nativerlVersion, List<String> instructions, List<String> files) {
        switch (nativerlVersion) {
            case VERSION_0_7_0:
            case VERSION_0_7_6:
            case VERSION_0_7_6_PBT:
            case VERSION_0_7_6_RESUME:
            case VERSION_1_0_1:
                instructions.addAll(Arrays.asList(
                        // Setup NativeRL
                        "mkdir -p work",
                        "cd work",
                        "unzip ../nativerl-1.0.0-SNAPSHOT-bin.zip > /dev/null",
                        "rm ../nativerl-1.0.0-SNAPSHOT-bin.zip",
                        "mv nativerl-bin/* .",
                        "mv examples/train.sh .",
                        "cd .."
                ));

                files.addAll(fileManager.getFiles(nativerlVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported nativeRL Version: " + nativerlVersion);
        }
    }

    private void installAnyLogic(AnyLogic anylogicVersion, List<String> instructions, List<String> files) {
        switch (anylogicVersion) {
            case VERSION_8_5_1:
            case VERSION_8_5_2:
                instructions.addAll(Arrays.asList(
                        "unzip baseEnv.zip > /dev/null",
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

    private void installJDK(JDK jdkVersion, List<String> instructions, List<String> files) {
        switch (jdkVersion) {
            case VERSION_8_222:
                instructions.addAll(Arrays.asList(
                        // Setup JVM
                        "tar xf OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz > /dev/null",
                        "rm -rf OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz",
                        "export JAVA_HOME=`pwd`/jdk8u222-b10",
                        "export JDK_HOME=$JAVA_HOME",
                        "export JRE_HOME=$JAVA_HOME/jre",
                        "export PATH=$JAVA_HOME/bin:$PATH",
                        "export LD_LIBRARY_PATH=$JAVA_HOME/jre/lib/amd64/server:$JAVA_HOME/jre/lib/amd64/:$LD_LIBRARY_PATH"
                ));

                files.addAll(fileManager.getFiles(jdkVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported JDK Version: " + jdkVersion);
        }
    }

    private void installConda(Conda condaVersion, List<String> instructions, List<String> files) {
        switch (condaVersion) {
            case VERSION_0_7_0:
            case VERSION_0_7_6:
            case VERSION_0_8_1:
                instructions.addAll(Arrays.asList(
                        // Setup Anaconda
                        "mkdir -p conda",
                        "cd conda",
                        "tar xf ../rllibpack.tar.gz > /dev/null",
                        "rm ../rllibpack.tar.gz",
                        "source bin/activate",
                        "cd .."
                ));

                files.addAll(fileManager.getFiles(condaVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported Conda Version: " + condaVersion);
        }

    }

    private void installHelper(PathmindHelper pathmindHelperVersion, List<String> instructions, List<String> files) {
        switch (pathmindHelperVersion) {
            case VERSION_0_0_24:
            case VERSION_0_0_25:
            case VERSION_0_0_25_Multi:
            case VERSION_1_0_1:
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
                "unzip ../model.zip > /dev/null",
                "rm ../model.zip"
        ));
    }

    private void installCheckpoint(String checkpointS3Path, List<String> instructions, List<String> files) {
        if (checkpointS3Path != null) {
            files.add(fileManager.buildCheckpointCopyCmd(checkpointS3Path, "checkpoint.zip"));

            instructions.addAll(Arrays.asList(
                    "mkdir -p checkpoint",
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

    private String varCondition(String name, String value) {
        return "export " + name + "=${" + name + ":='" + value.replace("'", "\\'") + "'}";
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
                var("TEST_ITERATIONS", "0"), // disabled for now

                // Still has to be set, but doesn't actually do something, needs to be removed from train.sh
                var("STEP_TIME", "1"),
                var("STOP_TIME", "420"),
                var("TIME_UNIT", "MINUTE"),
                var("MAX_TIME_IN_SEC", String.valueOf(job.getMaxTimeInSec())),
                var("NUM_SAMPLES", String.valueOf(job.getNumSamples())),
                var("MULTIAGENT", String.valueOf(job.isMultiAgent())),
                varCondition("RESUME", String.valueOf(job.isResume())),
                var("CHECKPOINT_FREQUENCY", String.valueOf(job.getCheckpointFrequency())),
                var("USER_LOG", String.valueOf(job.isUserLog()))
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

    private String startTrainingRun(JobSpec job, List<String> instructions, List<String> files) {
        File script = null;
        File errChecker = null;
        try {
            script = File.createTempFile("pathmind", UUID.randomUUID().toString());
            errChecker = File.createTempFile("pathmind", UUID.randomUUID().toString());

            // generate script.sh
            List<String> finalInstruction = new ArrayList<>();
            finalInstruction.add("set -eo pipefail");
            finalInstruction.addAll(files);
            finalInstruction.addAll(instructions);
            finalInstruction.add("exit $?");

            String scriptStr = String.join(" ;\n", finalInstruction);
            FileUtils.writeStringToFile(script, scriptStr, Charset.defaultCharset());

            // generate errorCheck.sh
            scriptStr = String.join(" ;\n", checkErrors());
            FileUtils.writeStringToFile(errChecker, scriptStr, Charset.defaultCharset());

            String jobId = buildJobId(job.getRunId());

            client.fileUpload(jobId + "/script.sh", script);
            client.fileUpload(jobId + "/errorCheck.sh", errChecker);
            return client.jobSubmit(jobId, job.getType());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (script != null) {
                script.delete();
            }

            if (errChecker != null) {
                errChecker.delete();
            }
        }
    }

    private List<String> checkErrors() {
        List<String> instructions = new ArrayList<>();
        instructions.addAll(trainingErrorDAO.getAllKnownErrorsKeywords().stream()
                .map(msg -> "grep -m 1 \"" + msg + "\" " + TrainingFile.SCRIPT_LOG + " >> " + TrainingFile.KNOWN_ERROR)
                .collect(Collectors.toList()));

        return instructions;
    }

    private String buildJobId(long runId) {
        return AWS_JOB_ID_PREFIX + runId;
    }
}
