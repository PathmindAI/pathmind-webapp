package io.skymind.pathmind.services.training;

import io.skymind.pathmind.constants.RunStatus;

import java.util.Map;

public interface ExecutionProvider {
    /**
     * Starts the execution of the specified training job
     *
     * @param job Job Definition
     * @return Job Handle to be used with other methods
     */
    public String execute(JobSpec job);

    /**
     * Stops the execution of the training  job identified by the given job handle.
     * Does nothing if the job is already stopped
     *
     * @param jobHandle Job Handle
     */
    public void stop(String jobHandle);

    /**
     * Collect the status of the training job identified by the given job handle.
     * @param jobHandle Job Handle
     * @return The current status
     */
    public RunStatus status(String jobHandle);

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
     * @param jobHandle Job Handle
     * @return Map of training run to the contents of its policy file
     */
    public Map<String, String> progress(String jobHandle);

    /**
     * Download the policy file from the given jobHandle and trainingRun
     *
     * @param jobHandle Job Handle
     * @param trainingRun Training Run Name, as given as a key by `progress(jobHandle)`
     * @return policy file contents or null if no such file is available
     */
    public byte[] policy(String jobHandle, String trainingRun);

    /**
     *  Download the current console output for the given jobHandle. Best used for debugging purposes; usually not
     *  exposed to end users.
     *
     * @param jobHandle Job Handle
     * @return Console Output
     */
    public String console(String jobHandle);
}
