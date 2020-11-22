/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq;


import io.skymind.pathmind.db.jooq.tables.Experiment;
import io.skymind.pathmind.db.jooq.tables.ExperimentObservation;
import io.skymind.pathmind.db.jooq.tables.Metrics;
import io.skymind.pathmind.db.jooq.tables.MetricsRaw;
import io.skymind.pathmind.db.jooq.tables.Model;
import io.skymind.pathmind.db.jooq.tables.Observation;
import io.skymind.pathmind.db.jooq.tables.PathmindUser;
import io.skymind.pathmind.db.jooq.tables.Policy;
import io.skymind.pathmind.db.jooq.tables.Project;
import io.skymind.pathmind.db.jooq.tables.RewardScore;
import io.skymind.pathmind.db.jooq.tables.RewardVariable;
import io.skymind.pathmind.db.jooq.tables.Run;
import io.skymind.pathmind.db.jooq.tables.RunAdminNote;
import io.skymind.pathmind.db.jooq.tables.TrainerJob;
import io.skymind.pathmind.db.jooq.tables.TrainingError;

import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.experiment</code>.
     */
    public static final Experiment EXPERIMENT = Experiment.EXPERIMENT;

    /**
     * The table <code>public.experiment_observation</code>.
     */
    public static final ExperimentObservation EXPERIMENT_OBSERVATION = ExperimentObservation.EXPERIMENT_OBSERVATION;

    /**
     * The table <code>public.metrics</code>.
     */
    public static final Metrics METRICS = Metrics.METRICS;

    /**
     * The table <code>public.metrics_raw</code>.
     */
    public static final MetricsRaw METRICS_RAW = MetricsRaw.METRICS_RAW;

    /**
     * The table <code>public.model</code>.
     */
    public static final Model MODEL = Model.MODEL;

    /**
     * The table <code>public.observation</code>.
     */
    public static final Observation OBSERVATION = Observation.OBSERVATION;

    /**
     * The table <code>public.pathmind_user</code>.
     */
    public static final PathmindUser PATHMIND_USER = PathmindUser.PATHMIND_USER;

    /**
     * The table <code>public.policy</code>.
     */
    public static final Policy POLICY = Policy.POLICY;

    /**
     * The table <code>public.project</code>.
     */
    public static final Project PROJECT = Project.PROJECT;

    /**
     * The table <code>public.reward_score</code>.
     */
    public static final RewardScore REWARD_SCORE = RewardScore.REWARD_SCORE;

    /**
     * The table <code>public.reward_variable</code>.
     */
    public static final RewardVariable REWARD_VARIABLE = RewardVariable.REWARD_VARIABLE;

    /**
     * The table <code>public.run</code>.
     */
    public static final Run RUN = Run.RUN;

    /**
     * The table <code>public.run_admin_note</code>.
     */
    public static final RunAdminNote RUN_ADMIN_NOTE = RunAdminNote.RUN_ADMIN_NOTE;

    /**
     * The table <code>public.trainer_job</code>.
     */
    public static final TrainerJob TRAINER_JOB = TrainerJob.TRAINER_JOB;

    /**
     * The table <code>public.training_error</code>.
     */
    public static final TrainingError TRAINING_ERROR = TrainingError.TRAINING_ERROR;
}
