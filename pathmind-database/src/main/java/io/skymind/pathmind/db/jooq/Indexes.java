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

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index EXPERIMENT_MODEL_FK_INDEX = Indexes0.EXPERIMENT_MODEL_FK_INDEX;
    public static final Index EXPERIMENT_PKEY = Indexes0.EXPERIMENT_PKEY;
    public static final Index EXPERIMENT_OBSERVATION_PKEY = Indexes0.EXPERIMENT_OBSERVATION_PKEY;
    public static final Index METRICS_POLICY_ID_INDEX = Indexes0.METRICS_POLICY_ID_INDEX;
    public static final Index METRICS_RAW_POLICY_ID_INDEX = Indexes0.METRICS_RAW_POLICY_ID_INDEX;
    public static final Index MODEL_PKEY = Indexes0.MODEL_PKEY;
    public static final Index MODEL_PROJECT_FK_INDEX = Indexes0.MODEL_PROJECT_FK_INDEX;
    public static final Index OBSERVATION_MODEL_FK_INDEX = Indexes0.OBSERVATION_MODEL_FK_INDEX;
    public static final Index OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY = Indexes0.OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY;
    public static final Index OBSERVATION_PKEY = Indexes0.OBSERVATION_PKEY;
    public static final Index PATHMIND_USER_EMAIL_KEY = Indexes0.PATHMIND_USER_EMAIL_KEY;
    public static final Index PATHMIND_USER_PKEY = Indexes0.PATHMIND_USER_PKEY;
    public static final Index POLICY_PKEY = Indexes0.POLICY_PKEY;
    public static final Index POLICY_RUN_FK_INDEX = Indexes0.POLICY_RUN_FK_INDEX;
    public static final Index POLICY_RUN_ID_EXTERNAL_ID_KEY = Indexes0.POLICY_RUN_ID_EXTERNAL_ID_KEY;
    public static final Index PROJECT_PATHMIND_USER_FK_INDEX = Indexes0.PROJECT_PATHMIND_USER_FK_INDEX;
    public static final Index PROJECT_PKEY = Indexes0.PROJECT_PKEY;
    public static final Index UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Indexes0.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID;
    public static final Index REWARD_SCORE_POLICY_ID_INDEX = Indexes0.REWARD_SCORE_POLICY_ID_INDEX;
    public static final Index REWARD_VARIABLE_MODEL_FK_INDEX = Indexes0.REWARD_VARIABLE_MODEL_FK_INDEX;
    public static final Index REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY = Indexes0.REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY;
    public static final Index REWARD_VARIABLE_PKEY = Indexes0.REWARD_VARIABLE_PKEY;
    public static final Index RUN_EXPERIMENT_FK_INDEX = Indexes0.RUN_EXPERIMENT_FK_INDEX;
    public static final Index RUN_PKEY = Indexes0.RUN_PKEY;
    public static final Index PM_RUN_ADMIN_NOTES_RUN_ID_UNQ = Indexes0.PM_RUN_ADMIN_NOTES_RUN_ID_UNQ;
    public static final Index TRAINER_JOB_PKEY = Indexes0.TRAINER_JOB_PKEY;
    public static final Index UNIQUE_JOB_ID_S3BUCKET = Indexes0.UNIQUE_JOB_ID_S3BUCKET;
    public static final Index TRAINING_ERROR_PKEY = Indexes0.TRAINING_ERROR_PKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index EXPERIMENT_MODEL_FK_INDEX = Internal.createIndex("experiment_model_fk_index", Experiment.EXPERIMENT, new OrderField[] { Experiment.EXPERIMENT.MODEL_ID }, false);
        public static Index EXPERIMENT_PKEY = Internal.createIndex("experiment_pkey", Experiment.EXPERIMENT, new OrderField[] { Experiment.EXPERIMENT.ID }, true);
        public static Index EXPERIMENT_OBSERVATION_PKEY = Internal.createIndex("experiment_observation_pkey", ExperimentObservation.EXPERIMENT_OBSERVATION, new OrderField[] { ExperimentObservation.EXPERIMENT_OBSERVATION.ID }, true);
        public static Index METRICS_POLICY_ID_INDEX = Internal.createIndex("metrics_policy_id_index", Metrics.METRICS, new OrderField[] { Metrics.METRICS.POLICY_ID }, false);
        public static Index METRICS_RAW_POLICY_ID_INDEX = Internal.createIndex("metrics_raw_policy_id_index", MetricsRaw.METRICS_RAW, new OrderField[] { MetricsRaw.METRICS_RAW.POLICY_ID }, false);
        public static Index MODEL_PKEY = Internal.createIndex("model_pkey", Model.MODEL, new OrderField[] { Model.MODEL.ID }, true);
        public static Index MODEL_PROJECT_FK_INDEX = Internal.createIndex("model_project_fk_index", Model.MODEL, new OrderField[] { Model.MODEL.PROJECT_ID }, false);
        public static Index OBSERVATION_MODEL_FK_INDEX = Internal.createIndex("observation_model_fk_index", Observation.OBSERVATION, new OrderField[] { Observation.OBSERVATION.MODEL_ID }, false);
        public static Index OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY = Internal.createIndex("observation_model_id_array_index_key", Observation.OBSERVATION, new OrderField[] { Observation.OBSERVATION.MODEL_ID, Observation.OBSERVATION.ARRAY_INDEX }, true);
        public static Index OBSERVATION_PKEY = Internal.createIndex("observation_pkey", Observation.OBSERVATION, new OrderField[] { Observation.OBSERVATION.ID }, true);
        public static Index PATHMIND_USER_EMAIL_KEY = Internal.createIndex("pathmind_user_email_key", PathmindUser.PATHMIND_USER, new OrderField[] { PathmindUser.PATHMIND_USER.EMAIL }, true);
        public static Index PATHMIND_USER_PKEY = Internal.createIndex("pathmind_user_pkey", PathmindUser.PATHMIND_USER, new OrderField[] { PathmindUser.PATHMIND_USER.ID }, true);
        public static Index POLICY_PKEY = Internal.createIndex("policy_pkey", Policy.POLICY, new OrderField[] { Policy.POLICY.ID }, true);
        public static Index POLICY_RUN_FK_INDEX = Internal.createIndex("policy_run_fk_index", Policy.POLICY, new OrderField[] { Policy.POLICY.RUN_ID }, false);
        public static Index POLICY_RUN_ID_EXTERNAL_ID_KEY = Internal.createIndex("policy_run_id_external_id_key", Policy.POLICY, new OrderField[] { Policy.POLICY.RUN_ID, Policy.POLICY.EXTERNAL_ID }, true);
        public static Index PROJECT_PATHMIND_USER_FK_INDEX = Internal.createIndex("project_pathmind_user_fk_index", Project.PROJECT, new OrderField[] { Project.PROJECT.PATHMIND_USER_ID }, false);
        public static Index PROJECT_PKEY = Internal.createIndex("project_pkey", Project.PROJECT, new OrderField[] { Project.PROJECT.ID }, true);
        public static Index UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Internal.createIndex("unique_project_name_pathmind_user_id", Project.PROJECT, new OrderField[] { Project.PROJECT.PATHMIND_USER_ID, Project.PROJECT.NAME }, true);
        public static Index REWARD_SCORE_POLICY_ID_INDEX = Internal.createIndex("reward_score_policy_id_index", RewardScore.REWARD_SCORE, new OrderField[] { RewardScore.REWARD_SCORE.POLICY_ID }, false);
        public static Index REWARD_VARIABLE_MODEL_FK_INDEX = Internal.createIndex("reward_variable_model_fk_index", RewardVariable.REWARD_VARIABLE, new OrderField[] { RewardVariable.REWARD_VARIABLE.MODEL_ID }, false);
        public static Index REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY = Internal.createIndex("reward_variable_model_id_array_index_key", RewardVariable.REWARD_VARIABLE, new OrderField[] { RewardVariable.REWARD_VARIABLE.MODEL_ID, RewardVariable.REWARD_VARIABLE.ARRAY_INDEX }, true);
        public static Index REWARD_VARIABLE_PKEY = Internal.createIndex("reward_variable_pkey", RewardVariable.REWARD_VARIABLE, new OrderField[] { RewardVariable.REWARD_VARIABLE.ID }, true);
        public static Index RUN_EXPERIMENT_FK_INDEX = Internal.createIndex("run_experiment_fk_index", Run.RUN, new OrderField[] { Run.RUN.EXPERIMENT_ID }, false);
        public static Index RUN_PKEY = Internal.createIndex("run_pkey", Run.RUN, new OrderField[] { Run.RUN.ID }, true);
        public static Index PM_RUN_ADMIN_NOTES_RUN_ID_UNQ = Internal.createIndex("pm_run_admin_notes_run_id_unq", RunAdminNote.RUN_ADMIN_NOTE, new OrderField[] { RunAdminNote.RUN_ADMIN_NOTE.RUN_ID }, true);
        public static Index TRAINER_JOB_PKEY = Internal.createIndex("trainer_job_pkey", TrainerJob.TRAINER_JOB, new OrderField[] { TrainerJob.TRAINER_JOB.JOB_ID, TrainerJob.TRAINER_JOB.S3BUCKET }, true);
        public static Index UNIQUE_JOB_ID_S3BUCKET = Internal.createIndex("unique_job_id_s3bucket", TrainerJob.TRAINER_JOB, new OrderField[] { TrainerJob.TRAINER_JOB.JOB_ID, TrainerJob.TRAINER_JOB.S3BUCKET }, true);
        public static Index TRAINING_ERROR_PKEY = Internal.createIndex("training_error_pkey", TrainingError.TRAINING_ERROR, new OrderField[] { TrainingError.TRAINING_ERROR.ID }, true);
    }
}
