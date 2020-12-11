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
import io.skymind.pathmind.db.jooq.tables.records.ExperimentObservationRecord;
import io.skymind.pathmind.db.jooq.tables.records.ExperimentRecord;
import io.skymind.pathmind.db.jooq.tables.records.MetricsRawRecord;
import io.skymind.pathmind.db.jooq.tables.records.MetricsRecord;
import io.skymind.pathmind.db.jooq.tables.records.ModelRecord;
import io.skymind.pathmind.db.jooq.tables.records.ObservationRecord;
import io.skymind.pathmind.db.jooq.tables.records.PathmindUserRecord;
import io.skymind.pathmind.db.jooq.tables.records.PolicyRecord;
import io.skymind.pathmind.db.jooq.tables.records.ProjectRecord;
import io.skymind.pathmind.db.jooq.tables.records.RewardScoreRecord;
import io.skymind.pathmind.db.jooq.tables.records.RewardVariableRecord;
import io.skymind.pathmind.db.jooq.tables.records.RunAdminNoteRecord;
import io.skymind.pathmind.db.jooq.tables.records.RunRecord;
import io.skymind.pathmind.db.jooq.tables.records.TrainerJobRecord;
import io.skymind.pathmind.db.jooq.tables.records.TrainingErrorRecord;

import javax.annotation.processing.Generated;

import org.jooq.ForeignKey;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ExperimentRecord> EXPERIMENT_PKEY = UniqueKeys0.EXPERIMENT_PKEY;
    public static final UniqueKey<ExperimentObservationRecord> PK_EXPERIMENT_OBSERVATION = UniqueKeys0.PK_EXPERIMENT_OBSERVATION;
    public static final UniqueKey<ModelRecord> MODEL_PKEY = UniqueKeys0.MODEL_PKEY;
    public static final UniqueKey<ObservationRecord> OBSERVATION_PKEY = UniqueKeys0.OBSERVATION_PKEY;
    public static final UniqueKey<ObservationRecord> OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY = UniqueKeys0.OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY;
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_PKEY = UniqueKeys0.PATHMIND_USER_PKEY;
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_EMAIL_KEY = UniqueKeys0.PATHMIND_USER_EMAIL_KEY;
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_API_KEY_KEY = UniqueKeys0.PATHMIND_USER_API_KEY_KEY;
    public static final UniqueKey<PolicyRecord> POLICY_PKEY = UniqueKeys0.POLICY_PKEY;
    public static final UniqueKey<PolicyRecord> POLICY_RUN_ID_EXTERNAL_ID_KEY = UniqueKeys0.POLICY_RUN_ID_EXTERNAL_ID_KEY;
    public static final UniqueKey<ProjectRecord> PROJECT_PKEY = UniqueKeys0.PROJECT_PKEY;
    public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = UniqueKeys0.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID;
    public static final UniqueKey<RewardVariableRecord> REWARD_VARIABLE_PKEY = UniqueKeys0.REWARD_VARIABLE_PKEY;
    public static final UniqueKey<RewardVariableRecord> REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY = UniqueKeys0.REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY;
    public static final UniqueKey<RunRecord> RUN_PKEY = UniqueKeys0.RUN_PKEY;
    public static final UniqueKey<RunAdminNoteRecord> PM_RUN_ADMIN_NOTES_RUN_ID_UNQ = UniqueKeys0.PM_RUN_ADMIN_NOTES_RUN_ID_UNQ;
    public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = UniqueKeys0.TRAINER_JOB_PKEY;
    public static final UniqueKey<TrainerJobRecord> UNIQUE_JOB_ID_S3BUCKET = UniqueKeys0.UNIQUE_JOB_ID_S3BUCKET;
    public static final UniqueKey<TrainingErrorRecord> TRAINING_ERROR_PKEY = UniqueKeys0.TRAINING_ERROR_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = ForeignKeys0.EXPERIMENT__PM_FK_EXPERIMENT_MODEL;
    public static final ForeignKey<ExperimentObservationRecord, ExperimentRecord> EXPERIMENT_OBSERVATION__PM_FK_EO_EXPERIMENT = ForeignKeys0.EXPERIMENT_OBSERVATION__PM_FK_EO_EXPERIMENT;
    public static final ForeignKey<ExperimentObservationRecord, ObservationRecord> EXPERIMENT_OBSERVATION__PM_FK_EO_OBSERVATION = ForeignKeys0.EXPERIMENT_OBSERVATION__PM_FK_EO_OBSERVATION;
    public static final ForeignKey<MetricsRecord, PolicyRecord> METRICS__PM_FK_METRICS_POLICY = ForeignKeys0.METRICS__PM_FK_METRICS_POLICY;
    public static final ForeignKey<MetricsRawRecord, PolicyRecord> METRICS_RAW__PM_FK_METRICS_RAW_POLICY = ForeignKeys0.METRICS_RAW__PM_FK_METRICS_RAW_POLICY;
    public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = ForeignKeys0.MODEL__PM_FK_MODEL_PROJECT;
    public static final ForeignKey<ObservationRecord, ModelRecord> OBSERVATION__PM_FK_OBSERVATION_MODEL = ForeignKeys0.OBSERVATION__PM_FK_OBSERVATION_MODEL;
    public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = ForeignKeys0.POLICY__PM_FK_POLICY_RUN;
    public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = ForeignKeys0.PROJECT__PM_FK_PROJECT_PATHMIND_USER;
    public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = ForeignKeys0.REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY;
    public static final ForeignKey<RewardVariableRecord, ModelRecord> REWARD_VARIABLE__PM_FK_REWARD_VARIABLE_MODEL = ForeignKeys0.REWARD_VARIABLE__PM_FK_REWARD_VARIABLE_MODEL;
    public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = ForeignKeys0.RUN__PM_FK_RUN_EXPERIMENT;
    public static final ForeignKey<RunRecord, TrainingErrorRecord> RUN__PM_FK_TRAINING_ERROR = ForeignKeys0.RUN__PM_FK_TRAINING_ERROR;
    public static final ForeignKey<RunAdminNoteRecord, RunRecord> RUN_ADMIN_NOTE__PM_FK_RUN_ADMIN_NOTES = ForeignKeys0.RUN_ADMIN_NOTE__PM_FK_RUN_ADMIN_NOTES;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<ExperimentRecord> EXPERIMENT_PKEY = Internal.createUniqueKey(Experiment.EXPERIMENT, "experiment_pkey", Experiment.EXPERIMENT.ID);
        public static final UniqueKey<ExperimentObservationRecord> PK_EXPERIMENT_OBSERVATION = Internal.createUniqueKey(ExperimentObservation.EXPERIMENT_OBSERVATION, "pk_experiment_observation", ExperimentObservation.EXPERIMENT_OBSERVATION.EXPERIMENT_ID, ExperimentObservation.EXPERIMENT_OBSERVATION.OBSERVATION_ID);
        public static final UniqueKey<ModelRecord> MODEL_PKEY = Internal.createUniqueKey(Model.MODEL, "model_pkey", Model.MODEL.ID);
        public static final UniqueKey<ObservationRecord> OBSERVATION_PKEY = Internal.createUniqueKey(Observation.OBSERVATION, "observation_pkey", Observation.OBSERVATION.ID);
        public static final UniqueKey<ObservationRecord> OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY = Internal.createUniqueKey(Observation.OBSERVATION, "observation_model_id_array_index_key", Observation.OBSERVATION.MODEL_ID, Observation.OBSERVATION.ARRAY_INDEX);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_PKEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_pkey", PathmindUser.PATHMIND_USER.ID);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_EMAIL_KEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_email_key", PathmindUser.PATHMIND_USER.EMAIL);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_API_KEY_KEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_api_key_key", PathmindUser.PATHMIND_USER.API_KEY);
        public static final UniqueKey<PolicyRecord> POLICY_PKEY = Internal.createUniqueKey(Policy.POLICY, "policy_pkey", Policy.POLICY.ID);
        public static final UniqueKey<PolicyRecord> POLICY_RUN_ID_EXTERNAL_ID_KEY = Internal.createUniqueKey(Policy.POLICY, "policy_run_id_external_id_key", Policy.POLICY.RUN_ID, Policy.POLICY.EXTERNAL_ID);
        public static final UniqueKey<ProjectRecord> PROJECT_PKEY = Internal.createUniqueKey(Project.PROJECT, "project_pkey", Project.PROJECT.ID);
        public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Internal.createUniqueKey(Project.PROJECT, "unique_project_name_pathmind_user_id", Project.PROJECT.PATHMIND_USER_ID, Project.PROJECT.NAME);
        public static final UniqueKey<RewardVariableRecord> REWARD_VARIABLE_PKEY = Internal.createUniqueKey(RewardVariable.REWARD_VARIABLE, "reward_variable_pkey", RewardVariable.REWARD_VARIABLE.ID);
        public static final UniqueKey<RewardVariableRecord> REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY = Internal.createUniqueKey(RewardVariable.REWARD_VARIABLE, "reward_variable_model_id_array_index_key", RewardVariable.REWARD_VARIABLE.MODEL_ID, RewardVariable.REWARD_VARIABLE.ARRAY_INDEX);
        public static final UniqueKey<RunRecord> RUN_PKEY = Internal.createUniqueKey(Run.RUN, "run_pkey", Run.RUN.ID);
        public static final UniqueKey<RunAdminNoteRecord> PM_RUN_ADMIN_NOTES_RUN_ID_UNQ = Internal.createUniqueKey(RunAdminNote.RUN_ADMIN_NOTE, "pm_run_admin_notes_run_id_unq", RunAdminNote.RUN_ADMIN_NOTE.RUN_ID);
        public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = Internal.createUniqueKey(TrainerJob.TRAINER_JOB, "trainer_job_pkey", TrainerJob.TRAINER_JOB.JOB_ID, TrainerJob.TRAINER_JOB.S3BUCKET);
        public static final UniqueKey<TrainerJobRecord> UNIQUE_JOB_ID_S3BUCKET = Internal.createUniqueKey(TrainerJob.TRAINER_JOB, "unique_job_id_s3bucket", TrainerJob.TRAINER_JOB.JOB_ID, TrainerJob.TRAINER_JOB.S3BUCKET);
        public static final UniqueKey<TrainingErrorRecord> TRAINING_ERROR_PKEY = Internal.createUniqueKey(TrainingError.TRAINING_ERROR, "training_error_pkey", TrainingError.TRAINING_ERROR.ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.MODEL_PKEY, Experiment.EXPERIMENT, "experiment__pm_fk_experiment_model", Experiment.EXPERIMENT.MODEL_ID);
        public static final ForeignKey<ExperimentObservationRecord, ExperimentRecord> EXPERIMENT_OBSERVATION__PM_FK_EO_EXPERIMENT = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.EXPERIMENT_PKEY, ExperimentObservation.EXPERIMENT_OBSERVATION, "experiment_observation__pm_fk_eo_experiment", ExperimentObservation.EXPERIMENT_OBSERVATION.EXPERIMENT_ID);
        public static final ForeignKey<ExperimentObservationRecord, ObservationRecord> EXPERIMENT_OBSERVATION__PM_FK_EO_OBSERVATION = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.OBSERVATION_PKEY, ExperimentObservation.EXPERIMENT_OBSERVATION, "experiment_observation__pm_fk_eo_observation", ExperimentObservation.EXPERIMENT_OBSERVATION.OBSERVATION_ID);
        public static final ForeignKey<MetricsRecord, PolicyRecord> METRICS__PM_FK_METRICS_POLICY = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.POLICY_PKEY, Metrics.METRICS, "metrics__pm_fk_metrics_policy", Metrics.METRICS.POLICY_ID);
        public static final ForeignKey<MetricsRawRecord, PolicyRecord> METRICS_RAW__PM_FK_METRICS_RAW_POLICY = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.POLICY_PKEY, MetricsRaw.METRICS_RAW, "metrics_raw__pm_fk_metrics_raw_policy", MetricsRaw.METRICS_RAW.POLICY_ID);
        public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.PROJECT_PKEY, Model.MODEL, "model__pm_fk_model_project", Model.MODEL.PROJECT_ID);
        public static final ForeignKey<ObservationRecord, ModelRecord> OBSERVATION__PM_FK_OBSERVATION_MODEL = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.MODEL_PKEY, Observation.OBSERVATION, "observation__pm_fk_observation_model", Observation.OBSERVATION.MODEL_ID);
        public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.RUN_PKEY, Policy.POLICY, "policy__pm_fk_policy_run", Policy.POLICY.RUN_ID);
        public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.PATHMIND_USER_PKEY, Project.PROJECT, "project__pm_fk_project_pathmind_user", Project.PROJECT.PATHMIND_USER_ID);
        public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.POLICY_PKEY, RewardScore.REWARD_SCORE, "reward_score__pm_fk_reward_score_policy", RewardScore.REWARD_SCORE.POLICY_ID);
        public static final ForeignKey<RewardVariableRecord, ModelRecord> REWARD_VARIABLE__PM_FK_REWARD_VARIABLE_MODEL = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.MODEL_PKEY, RewardVariable.REWARD_VARIABLE, "reward_variable__pm_fk_reward_variable_model", RewardVariable.REWARD_VARIABLE.MODEL_ID);
        public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.EXPERIMENT_PKEY, Run.RUN, "run__pm_fk_run_experiment", Run.RUN.EXPERIMENT_ID);
        public static final ForeignKey<RunRecord, TrainingErrorRecord> RUN__PM_FK_TRAINING_ERROR = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.TRAINING_ERROR_PKEY, Run.RUN, "run__pm_fk_training_error", Run.RUN.TRAINING_ERROR_ID);
        public static final ForeignKey<RunAdminNoteRecord, RunRecord> RUN_ADMIN_NOTE__PM_FK_RUN_ADMIN_NOTES = Internal.createForeignKey(io.skymind.pathmind.db.jooq.Keys.RUN_PKEY, RunAdminNote.RUN_ADMIN_NOTE, "run_admin_note__pm_fk_run_admin_notes", RunAdminNote.RUN_ADMIN_NOTE.RUN_ID);
    }
}
