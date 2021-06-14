package io.skymind.pathmind.services.training.cloud.aws;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.constant.TrainingFile;
import io.skymind.pathmind.services.training.versions.AWSFileManager;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.rllib.CheckPoint;
import io.skymind.pathmind.shared.data.rllib.ExperimentState;
import io.skymind.pathmind.shared.data.rllib.ExperimentStateOld;
import io.skymind.pathmind.shared.exception.PathMindException;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.JobSpec;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.versions.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.skymind.pathmind.services.training.cloud.aws.BashScriptCreatorUtil.*;
import static io.skymind.pathmind.shared.constants.RunStatus.*;
import static io.skymind.pathmind.shared.constants.RunStatus.Error;

@Service
@Slf4j
public class AWSExecutionProvider implements ExecutionProvider {
    private final AWSApiClient client;
    private final ObjectMapper objectMapper;
    private final AWSFileManager fileManager;
    private final TrainingErrorDAO trainingErrorDAO;

    private static final String AWS_JOB_ID_PREFIX = "id";
    public static final String RLLIB_ERROR_PREFIX = "x-rllib_error";
    public static final String SUCCESS_MESSAGE_PREFIX = "x-success_message";
    public static final String WARNING_MESSAGE_PREFIX = "x-warning_message";
    private static final String OBS_SNIPPET_FILE = "obs.txt";
    
    public static final int RLLIB_MAX_LEN = 1024;
    public static final int SUCCESS_MAX_LEN = 1024;
    public static final int WARNING_MAX_LEN = 1024;
    
    private static final Predicate<String> ERROR_KEY_MATCH = // todo: possible even get a date of error as second match group
            Pattern.compile("(.)*error_(.*)txt$", Pattern.CASE_INSENSITIVE).asMatchPredicate();

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
        installNativeRL(env.getNativerlVersion(), instructions, files);
        installAnyLogic(env.getAnylogicVersion(), instructions, files);
        installHelper(env.getPathmindHelperVersion(), instructions, files);
        installModel(job.getModelFileId(), instructions, files);
        installCheckpoint(job.getCheckpointFileId(), instructions, files);
        installObsSnippet(buildJobId(job.getRunId()) + "/" + OBS_SNIPPET_FILE, instructions, files);

        // Set up variables
        setupVariables(job, instructions);

        // Set up instructions to run that specific type of job
        runTraining(instructions);

        // Start actual execution of the job
        return startTrainingRun(job, instructions, files, env.getEc2InstanceType());
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
        if (outputExist(jobHandle)) {
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

            if (trialStatusCount.getOrDefault(CheckPoint.ERROR, 0L) > 0 || knownErrsCheck.size() > 0) {
                log.warn("{} error(s) detected for the AWS jobHandle {}: {}", knownErrsCheck.size(), jobHandle, knownErrsCheck);
            }

            String rlLibError = getRLlibError(jobHandle);
            if (StringUtils.isNotEmpty(rlLibError)) {
                log.warn("rlLib error detected for the AWS jobHandle {}: {}", jobHandle, rlLibError);
                knownErrsCheck.add(RLLIB_ERROR_PREFIX + rlLibError);
            }

            if (!knownErrsCheck.isEmpty()) {
                return new ProviderJobStatus(Error, knownErrsCheck);
            }

            Optional<String> experimentReport = getExperimentReport(jobHandle);
            boolean isCompletingByReport = experimentReport.isPresent() && experimentReport.get().contains("Success: Training completed successfully");

            if (isCompletingByReport) {
                // make sure every trial of experimentStat has "TERMINATED" status
                if (experimentState != null && experimentState.getCheckpoints() != null) {
                    experimentState.getCheckpoints().stream()
                        .filter(chk -> chk.getStatus().equals(CheckPoint.RUNNING))
                        .forEach(chk -> chk.setStatus(CheckPoint.TERMINATED));
                }

                ProviderJobStatus completingStatus = new ProviderJobStatus(Completing, new ArrayList<>(), experimentState);
                experimentReport.ifPresent(m -> {
                    String[] lines = m.split("\n");
                    Set<String> reasons = new HashSet<>();
                    Arrays.stream(lines)
                        .filter(s -> s.contains("Early Stop Reason"))
                        .forEach(s -> {
                            reasons.add(s.split(":")[1].trim());
                        });

                    if (reasons.size() > 1) {
                        log.info(String.format("The number of early stop reasons is more than 2: %s", reasons));
                    }

                    String reason = reasons.iterator().next();
                    if (reason.equals("Training has converged")) {
                        completingStatus.getDescription().add(SUCCESS_MESSAGE_PREFIX + reason);
                    } else {
                        completingStatus.getDescription().add(WARNING_MESSAGE_PREFIX + reason);
                    }
                });
                return completingStatus;
            }

            return new ProviderJobStatus(Running, experimentState);
        }

        return ProviderJobStatus.STARTING;
    }

    @Override
    public Map<String, String> progress(String jobHandle) {
        return client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
                .filter(it -> !it.getKey().contains("/freezing/"))
                .filter(it -> it.getKey().endsWith("progress.csv"))
                .map(it -> {
                    final String key = new File(it.getKey()).getParentFile().getName();
                    final String contents = new String(client.fileContents(it.getKey()));
                    return Map.entry(key, contents);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, String> progress(String jobHandle, List<String> validExtIds) {
        Map<String, String> progressMap = new HashMap<>();

        validExtIds.stream()
                .forEach(id -> {
                    byte[] contents = client.fileContents(jobHandle + "/output/" + id + "/progress.csv", true);
                    if (contents != null) {
                        progressMap.put(id, new String(contents));
                    }
                });

        return progressMap;
    }

    @Override
    public byte[] policy(String jobHandle, String trainingRun) {
        Optional<byte[]> optional;
        if (trainingRun.equals("freezing")) {
            optional = getFile(jobHandle, "policy_" + trainingRun + ".zip", null);
        } else {
            optional = getFile(jobHandle, "policy_" + trainingRun + ".zip");
        }
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Map.Entry<@NotNull String, byte[]> snapshot(String jobHandle, String trainingRun) {
        Optional<byte[]> optional = getFile(jobHandle, "checkpoint.zip");
        return optional.isPresent() ? Map.entry(jobHandle, optional.get()) : null;
    }

    @Override
    public String console(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    public Optional<byte[]> getFile(String jobHandle, String fileName) {
        return getFile(jobHandle, fileName, "/freezing/");
    }

    private Optional<byte[]> getFile(String jobHandle, String fileName, String exclude) {
        return client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
            .filter(it -> exclude == null || !it.getKey().contains(exclude))
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

        return new ArrayList<>();
    }

    private ExperimentState getExperimentState(String jobHandle) {
        Optional<ExperimentState> expOpt = client.listObjects(jobHandle + "/output/").getObjectSummaries().parallelStream()
                .filter(it -> !it.getKey().contains("/freezing/"))
                .filter(it -> it.getKey().endsWith(".json") && it.getKey().contains("experiment_state-"))
                .map(S3ObjectSummary::getKey)
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .map(it -> {
                    try {
                        return objectMapper.readValue(client.fileContents(it), ExperimentState.class);
                    } catch (IOException e) {
                        try {
                            // to support old format(ray 1.0)
                            log.info("trying to parse OLD style format of ExperimentState.json");
                            ExperimentStateOld stateOldFormat = objectMapper.readValue(client.fileContents(it), ExperimentStateOld.class);
                            ExperimentState experimentState = new ExperimentState(stateOldFormat.getCheckpoints());
                            return experimentState;
                        } catch (IOException e1) {
                            log.error(e1.getMessage(), e1);
                            return null;
                        }
                    }
                });

        return expOpt != null && expOpt.isPresent() ? expOpt.get() : null;
    }

    public String getRLlibError(String jobHandle) {
        return client.listObjects(jobHandle + "/output/")
                        .getObjectSummaries().parallelStream()
                        .filter(it -> !it.getKey().contains("/freezing/"))
                        .map(S3ObjectSummary::getKey)
                        .filter(ERROR_KEY_MATCH)
                        .map(it -> {
                            byte[] bytes = client.fileContents(it);
                            try (BufferedReader r = new BufferedReader(new InputStreamReader(
                                    new ByteArrayInputStream(bytes), StandardCharsets.UTF_8))
                            ) {
                                List<String> lines = r.lines().collect(Collectors.toList());
                                return findLineWithException(lines);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .distinct()
                .collect(Collectors.joining(";\n"));
    }

    private static final Predicate<String> ERROR_STRING_MATCH = Pattern.compile("^(\\w+\\.)*\\w+:(.*)$", Pattern.CASE_INSENSITIVE).asMatchPredicate();

    static String findLineWithException(List<String> lines) {
        String exceptionLine = null;
        ListIterator<String> stringListIterator = lines.listIterator(lines.size());
        while (exceptionLine == null && stringListIterator.hasPrevious()) {
            String line = stringListIterator.previous();
            if (ERROR_STRING_MATCH.test(line)) {
                exceptionLine = line;
            }
        }
        return exceptionLine;
    }

    public Optional<String> getExperimentReport(String jobHandle) {
        return getFile(jobHandle, TrainingFile.REPORT_FILE)
            .map(bytes -> new String(bytes, StandardCharsets.UTF_8).trim());
    }

    public String getBestFreezingProgress(String jobHandle) {
        Optional<String> report = getExperimentReport(jobHandle);
        if (report.isPresent() && report.get().contains("Best Freezing:")) {
            // example of bestFreezingLine : Best Freezing: /app/work/PPO/freezing/PPO/PPO_PathmindEnvironment_7fd09_00000_0_2021-03-24_23-34-38
            Optional<String> bestFreezingLine = Arrays.stream(report.get().split("\n")).filter(line -> line.contains("Best Freezing:")).findFirst();
            if (bestFreezingLine.isPresent()) {
                String bestFreezingPath = bestFreezingLine.get().split(":")[1];
                String[] split = bestFreezingPath.split("/");
                Optional<byte[]> content =  getFile(jobHandle, split[split.length-1] + "/progress.csv", null);
                return content.isPresent() ? new String(content.get()) : null;
            }
        }

        return null;
    }

    public Map<String, LocalDateTime> getTerminatedTrials(ExperimentState experimentState) {
        if (experimentState != null) {
            return experimentState.getCheckpoints().stream()
                    .filter(checkPoint -> checkPoint.getStatus().equals(CheckPoint.TERMINATED))
                    .collect(
                            Collectors.toMap(
                                    CheckPoint::getId,
                                    cp -> LocalDateTime.ofInstant(Instant.ofEpochMilli(cp.getLastUpdateTime()), ZoneId.systemDefault())
                            )
                    );
        }
        return Collections.emptyMap();
    }

    private void installNativeRL(NativeRL nativerlVersion, List<String> instructions, List<String> files) {
        switch (nativerlVersion) {
            case VERSION_1_0_7:
            case VERSION_1_1_0:
            case VERSION_1_1_1:
            case VERSION_1_2_0:
            case VERSION_1_3_0:
            case VERSION_1_4_0:
            case VERSION_1_5_0:
            case VERSION_1_6_0:
            case VERSION_1_6_1:
            case VERSION_1_6_2:
                nativerlVersion.fileNames().forEach(filename -> {
                    instructions.addAll(Arrays.asList(
                        // Setup NativeRL
                        "mkdir -p work",
                        "cd work",
                        String.format("unzip ../%s > /dev/null", filename),
                        String.format("rm ../%s", filename),
                        "mv nativerl-bin/* .",
                        "mv examples/train.sh .",
                        "cd .."));
                });

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
            case VERSION_8_6_0:
            case VERSION_8_6_1:
            case VERSION_8_7_0:
            case VERSION_8_7_3:
            case VERSION_8_7_4:
            case VERSION_8_7_5:
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
            case VERSION_0_8_5:
            case VERSION_0_8_6:
            case VERSION_0_8_7:
            case VERSION_1_0_0:
            case VERSION_1_3_0:
            case VERSION_1_4_0:
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
            case VERSION_1_2_0:
                instructions.addAll(Arrays.asList(
                        // Setup Anaconda
                        "mkdir -p conda",
                        "cd conda",
                        "tar xf ../rllibpack.tar.gz > /dev/null",
                        "rm ../rllibpack.tar.gz",
                        "source bin/activate",
                        "aws s3 cp s3://public-pathmind.com/ray_fix/simple_list_collector.py ./lib/python3.7/site-packages/ray/rllib/evaluation/collectors/ > /dev/null",
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
            case VERSION_1_0_2:
            case VERSION_1_2_0:
            case VERSION_1_3_0:
            case VERSION_1_4_0:
            case VERSION_1_5_0:
            case VERSION_1_6_0:
            case VERSION_1_6_1:
                instructions.addAll(Arrays.asList(
                        "mv PathmindPolicy.jar work/lib/"
                ));

                files.addAll(fileManager.getFiles(pathmindHelperVersion));
                break;
            default:
                throw new IllegalArgumentException("Unsupported Pathmind Helper Version: " + pathmindHelperVersion);
        }
    }

    private void installModel(String modelFileId, List<String> instructions, List<String> files) {
        files.add(fileManager.buildS3CopyCmd(client.getBucketName(), modelFileId, "model.zip"));

        instructions.addAll(Arrays.asList(
                "cd work",
                "unzip -n ../model.zip > /dev/null",
                "rm ../model.zip"
        ));
    }

    private void installCheckpoint(String checkpointS3Path, List<String> instructions, List<String> files) {
        if (checkpointS3Path != null) {
            files.add(fileManager.buildS3CopyCmd(client.getBucketName(), checkpointS3Path, "checkpoint.zip"));

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

    private void installObsSnippet(String obsSnippetS3Path, List<String> instructions, List<String> files) {
        if (obsSnippetS3Path != null) {
            files.add(fileManager.buildS3CopyCmd(client.getBucketName(), obsSnippetS3Path, OBS_SNIPPET_FILE));

            instructions.add("mv ../obs.txt .");
        }
    }

    private void setupVariables(JobSpec job, List<String> instructions) {
        instructions.addAll(Arrays.asList(
                var("CLASS_SNIPPET", job.getVariables()),
                var("RESET_SNIPPET", job.getReset()),
                var("REWARD_SNIPPET", job.getReward()),
                var("OBSERVATION_SNIPPET", "file:" + OBS_SNIPPET_FILE),
                var("METRICS_SNIPPET", job.getMetrics()),
                var("TEST_ITERATIONS", "0"), // disabled for now
                var("MAX_TIME_IN_SEC", String.valueOf(job.getMaxTimeInSec())),
                var("NUM_SAMPLES", String.valueOf(job.getNumSamples())),
                var("MULTIAGENT", String.valueOf(job.isMultiAgent())),
                varCondition("RESUME", String.valueOf(job.isResume())),
                var("CHECKPOINT_FREQUENCY", String.valueOf(job.getCheckpointFrequency())),
                var("ENTROPY_SLOPE", "1"), // turn off for now
                var("VF_LOSS_RANGE", "0"), // turn off for now
                var("VALUE_PRED", "1"),
                var("USER_LOG", String.valueOf(job.getEnv().isUserLog())),
                var("DEBUGMETRICS", String.valueOf(job.isRecordMetricsRaw())),
                var("NAMED_VARIABLE", String.valueOf(job.isNamedVariables())),
                var("MAX_MEMORY_IN_MB", String.valueOf(job.getEnv().getMaxMemory())),
                var("NUM_HIDDEN_NODES", String.valueOf(job.getEnv().getHiddenNode())),
                var("NUM_HIDDEN_LAYERS", String.valueOf(job.getEnv().getHiddenLayer())),
                var("MAIN_AGENT", job.getMainAgentName()),
                var("EXPERIMENT_CLASS", job.getExpClassName()),
                var("EXPERIMENT_TYPE", job.getExpClassType()),
                var("FREEZING", String.valueOf(job.getEnv().isFreezing())),
                var("RAY_DEBUG", String.valueOf(job.getEnv().isRayDebug())),
                var("SCHEDULER", String.valueOf(job.getEnv().getScheduler())),
                var("TUNE_DISABLE_AUTO_CALLBACK_LOGGERS", "1"),
                var("ACTIONMASKS", String.valueOf(job.isActionMask()))
        ));

        if (job.getEnv().isLongerTraining()) {
            instructions.add(var("MAX_ITERATIONS", "1500"));
            instructions.add(var("EPISODE_REWARD_RANGE", "0.005"));
            instructions.add(var("CONVERGENCE_CHECK_START_ITERATION", String.valueOf(job.getEnv().getStartCheckIterationForLongerTraining())));
        } else {
            instructions.add(var("MAX_ITERATIONS", String.valueOf(job.getIterations())));
            instructions.add(var("EPISODE_REWARD_RANGE", "0.01"));
        }

        if (ModelType.isPythonModel(job.getModelType()) || ModelType.isPathmindModel(job.getModelType())) {
            instructions.add(var("ENVIRONMENT_NAME", job.getEnvironment()));
            instructions.add(var("USE_PY_NATIVERL", Boolean.TRUE.toString()));
            instructions.add(var("IS_GYM", Boolean.valueOf(ModelType.isPythonModel(job.getModelType())).toString()));
            instructions.add(var("IS_PATHMIND_SIMULATION", Boolean.valueOf(ModelType.isPathmindModel(job.getModelType())).toString()));
            if (job.getObsSelection() != null) {
                instructions.add(var("OBS_SELECTION", job.getObsSelection()));
            }
            if (job.getRewFctName() != null) {
                instructions.add(var("REW_FCT_NAME", job.getRewFctName()));
            }
            //todo if we need to validate requirements, we'd rather create another script to check it. the current script is just install requirements.txt
            instructions.add("if [[ ! -z \"$ENVIRONMENT_NAME\" ]]; then find . -maxdepth 1 -name requirements.txt -exec pip install -r '{}' \\; 2>/dev/null ; fi");
        }


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

    private String startTrainingRun(JobSpec job, List<String> instructions, List<String> files, EC2InstanceType ec2InstanceType) {
        File script = null;
        File errChecker = null;
        File obsSnippet = null;
        try {
            script = File.createTempFile("pathmind", UUID.randomUUID().toString());
            errChecker = File.createTempFile("pathmind", UUID.randomUUID().toString());
            obsSnippet = File.createTempFile("pathmind", UUID.randomUUID().toString());

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

            // generate obs.txt
            scriptStr = BashScriptCreatorUtil.createObservationSnippet(job.getSelectedObservations());
            FileUtils.writeStringToFile(obsSnippet, scriptStr, Charset.defaultCharset());

            String jobId = buildJobId(job.getRunId());

            client.fileUpload(jobId + "/script.sh", script);
            client.fileUpload(jobId + "/errorCheck.sh", errChecker);
            client.fileUpload(jobId + "/" + OBS_SNIPPET_FILE, obsSnippet);
            return client.jobSubmit(jobId, job.getType(), ec2InstanceType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PathMindException("Failed to start training");
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
