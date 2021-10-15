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
import io.skymind.pathmind.db.jooq.tables.SimulationParameter;
import io.skymind.pathmind.db.jooq.tables.TrainerJob;
import io.skymind.pathmind.db.jooq.tables.TrainingError;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.experiment</code>.
     */
    public final Experiment EXPERIMENT = Experiment.EXPERIMENT;

    /**
     * The table <code>public.experiment_observation</code>.
     */
    public final ExperimentObservation EXPERIMENT_OBSERVATION = ExperimentObservation.EXPERIMENT_OBSERVATION;

    /**
     * The table <code>public.metrics</code>.
     */
    public final Metrics METRICS = Metrics.METRICS;

    /**
     * The table <code>public.metrics_raw</code>.
     */
    public final MetricsRaw METRICS_RAW = MetricsRaw.METRICS_RAW;

    /**
     * The table <code>public.model</code>.
     */
    public final Model MODEL = Model.MODEL;

    /**
     * The table <code>public.observation</code>.
     */
    public final Observation OBSERVATION = Observation.OBSERVATION;

    /**
     * The table <code>public.pathmind_user</code>.
     */
    public final PathmindUser PATHMIND_USER = PathmindUser.PATHMIND_USER;

    /**
     * The table <code>public.policy</code>.
     */
    public final Policy POLICY = Policy.POLICY;

    /**
     * The table <code>public.project</code>.
     */
    public final Project PROJECT = Project.PROJECT;

    /**
     * The table <code>public.reward_score</code>.
     */
    public final RewardScore REWARD_SCORE = RewardScore.REWARD_SCORE;

    /**
     * The table <code>public.reward_variable</code>.
     */
    public final RewardVariable REWARD_VARIABLE = RewardVariable.REWARD_VARIABLE;

    /**
     * The table <code>public.run</code>.
     */
    public final Run RUN = Run.RUN;

    /**
     * The table <code>public.run_admin_note</code>.
     */
    public final RunAdminNote RUN_ADMIN_NOTE = RunAdminNote.RUN_ADMIN_NOTE;

    /**
     * The table <code>public.simulation_parameter</code>.
     */
    public final SimulationParameter SIMULATION_PARAMETER = SimulationParameter.SIMULATION_PARAMETER;

    /**
     * The table <code>public.trainer_job</code>.
     */
    public final TrainerJob TRAINER_JOB = TrainerJob.TRAINER_JOB;

    /**
     * The table <code>public.training_error</code>.
     */
    public final TrainingError TRAINING_ERROR = TrainingError.TRAINING_ERROR;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Arrays.<Sequence<?>>asList(
            Sequences.EXPERIMENT_ID_SEQ,
            Sequences.MODEL_ID_SEQ,
            Sequences.PATHMIND_USER_ID_SEQ,
            Sequences.POLICY_ID_SEQ,
            Sequences.PROJECT_ID_SEQ,
            Sequences.RUN_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Experiment.EXPERIMENT,
            ExperimentObservation.EXPERIMENT_OBSERVATION,
            Metrics.METRICS,
            MetricsRaw.METRICS_RAW,
            Model.MODEL,
            Observation.OBSERVATION,
            PathmindUser.PATHMIND_USER,
            Policy.POLICY,
            Project.PROJECT,
            RewardScore.REWARD_SCORE,
            RewardVariable.REWARD_VARIABLE,
            Run.RUN,
            RunAdminNote.RUN_ADMIN_NOTE,
            SimulationParameter.SIMULATION_PARAMETER,
            TrainerJob.TRAINER_JOB,
            TrainingError.TRAINING_ERROR);
    }
}
