package io.skymind.pathmind.shared.services.training;

import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.constants.RunStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface ExecutionProvider {
    /**
     * Starts the execution of the specified training job
     *
     * @param job Job Definition
     * @return Job Handle to be used with other methods
     */
    String execute(JobSpec job);

    /**
     * Stops the execution of the training  job identified by the given job handle.
     * Does nothing if the job is already stopped
     *
     * @param jobHandle Job Handle
     */
    void stop(String jobHandle);

    /**
     * Collect the status of the training job identified by the given job handle.
     * @param jobHandle Job Handle
     * @return The current status
     */
    ProviderJobStatus status(String jobHandle);

    /**
     * Collects the current progress of the training job identified by the given job handle.
     *
     * Because each training job can produce multiple training runs, this matches up the training run to the contents
     * of its progress.csv file.
     *
     * If called while a training job is still running, this may result incomplete results, both in not producing full
     * progress files as well as adding new training runs as they are started.
     *
     * The training run keys typically contain all tuned training parameters and a unique id.
     *
     * To interpret the results of this method take a look at {@link ProgressInterpreter}.
     *
     * @param jobHandle Job Handle
     * @return Map of training run to the contents of its progress file
     */
    Map<String, String> progress(String jobHandle);

    /**
     * Collects the current progress of the training job identified by the given job handle.
     * Will ignore policies if it doesn't exist in valid external ids list
     *
     * @param jobHandle Job Handle
     * @param validExternalIds Valid external Id list
     * @return Map of training run to the contents of its progress file
     */
    Map<String, String> progress(String jobHandle, List<String> validExternalIds);

    /**
     * Download the policy file from the given jobHandle and trainingRun
     *
     * @param jobHandle Job Handle
     * @param trainingRun Training Run Name, as given as a key by `progress(jobHandle)`
     * @return policy file contents or null if no such file is available
     */
    byte[] policy(String jobHandle, String trainingRun);

    /**
     * Download the last checkpoint file from the given jobHandle and trainingRun
     *
     * @param jobHandle
     * @param trainingRun
     * @return
     */
    Map.Entry<@NotNull String, byte[]> snapshot(String jobHandle, String trainingRun);

    /**
     *  Download the current console output for the given jobHandle. Best used for debugging purposes; usually not
     *  exposed to end users.
     *
     * @param jobHandle Job Handle
     * @return Console Output
     */
    String console(String jobHandle);

    /**
     * get current Execution Provider class enum
     *
     * @return
     */
    ExecutionProviderClass executionProviderClass();
}
