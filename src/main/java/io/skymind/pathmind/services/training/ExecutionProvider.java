package io.skymind.pathmind.services.training;

import io.skymind.pathmind.constants.ProviderJobStatus;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;

import javax.validation.constraints.NotNull;
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
     * Uploads the model file.
     *
     * @param modelFile The modelFile
     * @return The model file id
     */
    String uploadModel(byte[] modelFile);

    /**
     * Uploads the model file.
     *
     * @param runId The Run ID
     * @param modelFile The modelFile
     * @return The model file id
     */
    String uploadModel(long runId, byte[] modelFile);

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
     * Collects the current progress of the training job identified by the given job handle for the given job status
     *
     * @param jobHandle Job Handle
     * @param runStatus Job Status
     *
     * @return Map of training run to the contents of its progress file
     */
    Map<String, String> progress(String jobHandle, RunStatus runStatus);

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
     * upload checkpoint file
     *
     * @param checkpointFile
     * @return rescale file id for the checkpoint file
     */
    String uploadCheckpoint(byte[] checkpointFile);

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
    ExecutionProviderMetaDataDAO.ExecutionProviderClass executionProviderClass();
}
