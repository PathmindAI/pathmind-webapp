/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq;


import io.skymind.pathmind.db.jooq.tables.Experiment;
import io.skymind.pathmind.db.jooq.tables.Model;
import io.skymind.pathmind.db.jooq.tables.PathmindUser;
import io.skymind.pathmind.db.jooq.tables.Policy;
import io.skymind.pathmind.db.jooq.tables.Project;
import io.skymind.pathmind.db.jooq.tables.RewardScore;
import io.skymind.pathmind.db.jooq.tables.RewardVariable;
import io.skymind.pathmind.db.jooq.tables.Run;
import io.skymind.pathmind.db.jooq.tables.RunAdminNote;
import io.skymind.pathmind.db.jooq.tables.TrainerJob;
import io.skymind.pathmind.db.jooq.tables.TrainingError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 24467933;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.experiment</code>.
     */
    public final Experiment EXPERIMENT = io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;

    /**
     * The table <code>public.model</code>.
     */
    public final Model MODEL = io.skymind.pathmind.db.jooq.tables.Model.MODEL;

    /**
     * The table <code>public.pathmind_user</code>.
     */
    public final PathmindUser PATHMIND_USER = io.skymind.pathmind.db.jooq.tables.PathmindUser.PATHMIND_USER;

    /**
     * The table <code>public.policy</code>.
     */
    public final Policy POLICY = io.skymind.pathmind.db.jooq.tables.Policy.POLICY;

    /**
     * The table <code>public.project</code>.
     */
    public final Project PROJECT = io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

    /**
     * The table <code>public.reward_score</code>.
     */
    public final RewardScore REWARD_SCORE = io.skymind.pathmind.db.jooq.tables.RewardScore.REWARD_SCORE;

    /**
     * The table <code>public.reward_variable</code>.
     */
    public final RewardVariable REWARD_VARIABLE = io.skymind.pathmind.db.jooq.tables.RewardVariable.REWARD_VARIABLE;

    /**
     * The table <code>public.run</code>.
     */
    public final Run RUN = io.skymind.pathmind.db.jooq.tables.Run.RUN;

    /**
     * The table <code>public.run_admin_note</code>.
     */
    public final RunAdminNote RUN_ADMIN_NOTE = io.skymind.pathmind.db.jooq.tables.RunAdminNote.RUN_ADMIN_NOTE;

    /**
     * The table <code>public.trainer_job</code>.
     */
    public final TrainerJob TRAINER_JOB = io.skymind.pathmind.db.jooq.tables.TrainerJob.TRAINER_JOB;

    /**
     * The table <code>public.training_error</code>.
     */
    public final TrainingError TRAINING_ERROR = io.skymind.pathmind.db.jooq.tables.TrainingError.TRAINING_ERROR;

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
        List result = new ArrayList();
        result.addAll(getSequences0());
        return result;
    }

    private final List<Sequence<?>> getSequences0() {
        return Arrays.<Sequence<?>>asList(
            Sequences.EXPERIMENT_ID_SEQ,
            Sequences.MODEL_ID_SEQ,
            Sequences.PATHMIND_USER_ID_SEQ,
            Sequences.POLICY_ID_SEQ,
            Sequences.PROJECT_ID_SEQ,
            Sequences.REWARD_VARIABLE_ID_SEQ,
            Sequences.RUN_ID_SEQ,
            Sequences.TRAINING_ERROR_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Experiment.EXPERIMENT,
            Model.MODEL,
            PathmindUser.PATHMIND_USER,
            Policy.POLICY,
            Project.PROJECT,
            RewardScore.REWARD_SCORE,
            RewardVariable.REWARD_VARIABLE,
            Run.RUN,
            RunAdminNote.RUN_ADMIN_NOTE,
            TrainerJob.TRAINER_JOB,
            TrainingError.TRAINING_ERROR);
    }
}
