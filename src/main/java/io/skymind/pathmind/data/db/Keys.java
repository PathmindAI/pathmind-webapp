/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db;


import io.skymind.pathmind.data.db.tables.ExecutionProviderMetaData;
import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.Model;
import io.skymind.pathmind.data.db.tables.ModelFile;
import io.skymind.pathmind.data.db.tables.PathmindUser;
import io.skymind.pathmind.data.db.tables.Policy;
import io.skymind.pathmind.data.db.tables.PolicyFile;
import io.skymind.pathmind.data.db.tables.PolicySnapshot;
import io.skymind.pathmind.data.db.tables.Project;
import io.skymind.pathmind.data.db.tables.RewardScore;
import io.skymind.pathmind.data.db.tables.Run;
import io.skymind.pathmind.data.db.tables.TrainerJob;
import io.skymind.pathmind.data.db.tables.TrainingError;
import io.skymind.pathmind.data.db.tables.records.ExecutionProviderMetaDataRecord;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.data.db.tables.records.ModelFileRecord;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import io.skymind.pathmind.data.db.tables.records.PathmindUserRecord;
import io.skymind.pathmind.data.db.tables.records.PolicyFileRecord;
import io.skymind.pathmind.data.db.tables.records.PolicyRecord;
import io.skymind.pathmind.data.db.tables.records.PolicySnapshotRecord;
import io.skymind.pathmind.data.db.tables.records.ProjectRecord;
import io.skymind.pathmind.data.db.tables.records.RewardScoreRecord;
import io.skymind.pathmind.data.db.tables.records.RunRecord;
import io.skymind.pathmind.data.db.tables.records.TrainerJobRecord;
import io.skymind.pathmind.data.db.tables.records.TrainingErrorRecord;

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
        "jOOQ version:3.12.1"
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

    public static final UniqueKey<ExecutionProviderMetaDataRecord> EXECUTION_PROVIDER_META_DATA_PKEY = UniqueKeys0.EXECUTION_PROVIDER_META_DATA_PKEY;
    public static final UniqueKey<ExecutionProviderMetaDataRecord> UNIQUE_PROVIDER_CLASS_TYPE_KEY = UniqueKeys0.UNIQUE_PROVIDER_CLASS_TYPE_KEY;
    public static final UniqueKey<ExperimentRecord> EXPERIMENT_PKEY = UniqueKeys0.EXPERIMENT_PKEY;
    public static final UniqueKey<ModelRecord> MODEL_PKEY = UniqueKeys0.MODEL_PKEY;
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_PKEY = UniqueKeys0.PATHMIND_USER_PKEY;
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_EMAIL_KEY = UniqueKeys0.PATHMIND_USER_EMAIL_KEY;
    public static final UniqueKey<PolicyRecord> POLICY_PKEY = UniqueKeys0.POLICY_PKEY;
    public static final UniqueKey<PolicyRecord> POLICY_RUN_ID_EXTERNAL_ID_KEY = UniqueKeys0.POLICY_RUN_ID_EXTERNAL_ID_KEY;
    public static final UniqueKey<PolicyFileRecord> POLICY_FILE_UNIQUE_POLICY_ID = UniqueKeys0.POLICY_FILE_UNIQUE_POLICY_ID;
    public static final UniqueKey<PolicySnapshotRecord> POLICY_SNAPSHOT_UNIQUE_POLICY_ID = UniqueKeys0.POLICY_SNAPSHOT_UNIQUE_POLICY_ID;
    public static final UniqueKey<ProjectRecord> PROJECT_PKEY = UniqueKeys0.PROJECT_PKEY;
    public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = UniqueKeys0.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID;
    public static final UniqueKey<RunRecord> RUN_PKEY = UniqueKeys0.RUN_PKEY;
    public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = UniqueKeys0.TRAINER_JOB_PKEY;
    public static final UniqueKey<TrainingErrorRecord> TRAINING_ERROR_PKEY = UniqueKeys0.TRAINING_ERROR_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = ForeignKeys0.EXPERIMENT__PM_FK_EXPERIMENT_MODEL;
    public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = ForeignKeys0.MODEL__PM_FK_MODEL_PROJECT;
    public static final ForeignKey<ModelFileRecord, ModelRecord> MODEL_FILE__PM_FK_MODEL_FILE_MODEL = ForeignKeys0.MODEL_FILE__PM_FK_MODEL_FILE_MODEL;
    public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = ForeignKeys0.POLICY__PM_FK_POLICY_RUN;
    public static final ForeignKey<PolicyFileRecord, PolicyRecord> POLICY_FILE__PM_FK_POLICY_FILE_POLICY = ForeignKeys0.POLICY_FILE__PM_FK_POLICY_FILE_POLICY;
    public static final ForeignKey<PolicySnapshotRecord, PolicyRecord> POLICY_SNAPSHOT__PM_FK_POLICY_SNAPSHOT_POLICY = ForeignKeys0.POLICY_SNAPSHOT__PM_FK_POLICY_SNAPSHOT_POLICY;
    public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = ForeignKeys0.PROJECT__PM_FK_PROJECT_PATHMIND_USER;
    public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = ForeignKeys0.REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY;
    public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = ForeignKeys0.RUN__PM_FK_RUN_EXPERIMENT;
    public static final ForeignKey<RunRecord, TrainingErrorRecord> RUN__PM_FK_TRAINING_ERROR = ForeignKeys0.RUN__PM_FK_TRAINING_ERROR;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<ExecutionProviderMetaDataRecord> EXECUTION_PROVIDER_META_DATA_PKEY = Internal.createUniqueKey(ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, "execution_provider_meta_data_pkey", ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.ID);
        public static final UniqueKey<ExecutionProviderMetaDataRecord> UNIQUE_PROVIDER_CLASS_TYPE_KEY = Internal.createUniqueKey(ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, "unique_provider_class_type_key", ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.TYPE, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.KEY);
        public static final UniqueKey<ExperimentRecord> EXPERIMENT_PKEY = Internal.createUniqueKey(Experiment.EXPERIMENT, "experiment_pkey", Experiment.EXPERIMENT.ID);
        public static final UniqueKey<ModelRecord> MODEL_PKEY = Internal.createUniqueKey(Model.MODEL, "model_pkey", Model.MODEL.ID);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_PKEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_pkey", PathmindUser.PATHMIND_USER.ID);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_EMAIL_KEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_email_key", PathmindUser.PATHMIND_USER.EMAIL);
        public static final UniqueKey<PolicyRecord> POLICY_PKEY = Internal.createUniqueKey(Policy.POLICY, "policy_pkey", Policy.POLICY.ID);
        public static final UniqueKey<PolicyRecord> POLICY_RUN_ID_EXTERNAL_ID_KEY = Internal.createUniqueKey(Policy.POLICY, "policy_run_id_external_id_key", Policy.POLICY.RUN_ID, Policy.POLICY.EXTERNAL_ID);
        public static final UniqueKey<PolicyFileRecord> POLICY_FILE_UNIQUE_POLICY_ID = Internal.createUniqueKey(PolicyFile.POLICY_FILE, "policy_file_unique_policy_id", PolicyFile.POLICY_FILE.POLICY_ID);
        public static final UniqueKey<PolicySnapshotRecord> POLICY_SNAPSHOT_UNIQUE_POLICY_ID = Internal.createUniqueKey(PolicySnapshot.POLICY_SNAPSHOT, "policy_snapshot_unique_policy_id", PolicySnapshot.POLICY_SNAPSHOT.POLICY_ID);
        public static final UniqueKey<ProjectRecord> PROJECT_PKEY = Internal.createUniqueKey(Project.PROJECT, "project_pkey", Project.PROJECT.ID);
        public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Internal.createUniqueKey(Project.PROJECT, "unique_project_name_pathmind_user_id", Project.PROJECT.PATHMIND_USER_ID, Project.PROJECT.NAME);
        public static final UniqueKey<RunRecord> RUN_PKEY = Internal.createUniqueKey(Run.RUN, "run_pkey", Run.RUN.ID);
        public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = Internal.createUniqueKey(TrainerJob.TRAINER_JOB, "trainer_job_pkey", TrainerJob.TRAINER_JOB.JOB_ID);
        public static final UniqueKey<TrainingErrorRecord> TRAINING_ERROR_PKEY = Internal.createUniqueKey(TrainingError.TRAINING_ERROR, "training_error_pkey", TrainingError.TRAINING_ERROR.ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.MODEL_PKEY, Experiment.EXPERIMENT, "experiment__pm_fk_experiment_model", Experiment.EXPERIMENT.MODEL_ID);
        public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.PROJECT_PKEY, Model.MODEL, "model__pm_fk_model_project", Model.MODEL.PROJECT_ID);
        public static final ForeignKey<ModelFileRecord, ModelRecord> MODEL_FILE__PM_FK_MODEL_FILE_MODEL = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.MODEL_PKEY, ModelFile.MODEL_FILE, "model_file__pm_fk_model_file_model", ModelFile.MODEL_FILE.MODEL_ID);
        public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.RUN_PKEY, Policy.POLICY, "policy__pm_fk_policy_run", Policy.POLICY.RUN_ID);
        public static final ForeignKey<PolicyFileRecord, PolicyRecord> POLICY_FILE__PM_FK_POLICY_FILE_POLICY = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.POLICY_PKEY, PolicyFile.POLICY_FILE, "policy_file__pm_fk_policy_file_policy", PolicyFile.POLICY_FILE.POLICY_ID);
        public static final ForeignKey<PolicySnapshotRecord, PolicyRecord> POLICY_SNAPSHOT__PM_FK_POLICY_SNAPSHOT_POLICY = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.POLICY_PKEY, PolicySnapshot.POLICY_SNAPSHOT, "policy_snapshot__pm_fk_policy_snapshot_policy", PolicySnapshot.POLICY_SNAPSHOT.POLICY_ID);
        public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.PATHMIND_USER_PKEY, Project.PROJECT, "project__pm_fk_project_pathmind_user", Project.PROJECT.PATHMIND_USER_ID);
        public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.POLICY_PKEY, RewardScore.REWARD_SCORE, "reward_score__pm_fk_reward_score_policy", RewardScore.REWARD_SCORE.POLICY_ID);
        public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.EXPERIMENT_PKEY, Run.RUN, "run__pm_fk_run_experiment", Run.RUN.EXPERIMENT_ID);
        public static final ForeignKey<RunRecord, TrainingErrorRecord> RUN__PM_FK_TRAINING_ERROR = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.TRAINING_ERROR_PKEY, Run.RUN, "run__pm_fk_training_error", Run.RUN.TRAINING_ERROR_ID);
    }
}
