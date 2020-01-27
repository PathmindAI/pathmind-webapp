/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db;


import io.skymind.pathmind.data.db.tables.ExecutionProviderMetaData;
import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.Model;
import io.skymind.pathmind.data.db.tables.PathmindUser;
import io.skymind.pathmind.data.db.tables.Policy;
import io.skymind.pathmind.data.db.tables.Project;
import io.skymind.pathmind.data.db.tables.RewardScore;
import io.skymind.pathmind.data.db.tables.Run;
import io.skymind.pathmind.data.db.tables.TrainerJob;
import io.skymind.pathmind.data.db.tables.records.ExecutionProviderMetaDataRecord;
import io.skymind.pathmind.data.db.tables.records.ExperimentRecord;
import io.skymind.pathmind.data.db.tables.records.ModelRecord;
import io.skymind.pathmind.data.db.tables.records.PathmindUserRecord;
import io.skymind.pathmind.data.db.tables.records.PolicyRecord;
import io.skymind.pathmind.data.db.tables.records.ProjectRecord;
import io.skymind.pathmind.data.db.tables.records.RewardScoreRecord;
import io.skymind.pathmind.data.db.tables.records.RunRecord;
import io.skymind.pathmind.data.db.tables.records.TrainerJobRecord;

import javax.annotation.processing.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
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

    public static final Identity<ExecutionProviderMetaDataRecord, Long> IDENTITY_EXECUTION_PROVIDER_META_DATA = Identities0.IDENTITY_EXECUTION_PROVIDER_META_DATA;
    public static final Identity<ExperimentRecord, Long> IDENTITY_EXPERIMENT = Identities0.IDENTITY_EXPERIMENT;
    public static final Identity<ModelRecord, Long> IDENTITY_MODEL = Identities0.IDENTITY_MODEL;
    public static final Identity<PathmindUserRecord, Long> IDENTITY_PATHMIND_USER = Identities0.IDENTITY_PATHMIND_USER;
    public static final Identity<PolicyRecord, Long> IDENTITY_POLICY = Identities0.IDENTITY_POLICY;
    public static final Identity<ProjectRecord, Long> IDENTITY_PROJECT = Identities0.IDENTITY_PROJECT;
    public static final Identity<RunRecord, Long> IDENTITY_RUN = Identities0.IDENTITY_RUN;

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
    public static final UniqueKey<ProjectRecord> PROJECT_PKEY = UniqueKeys0.PROJECT_PKEY;
    public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = UniqueKeys0.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID;
    public static final UniqueKey<RunRecord> RUN_PKEY = UniqueKeys0.RUN_PKEY;
    public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = UniqueKeys0.TRAINER_JOB_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = ForeignKeys0.EXPERIMENT__PM_FK_EXPERIMENT_MODEL;
    public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = ForeignKeys0.MODEL__PM_FK_MODEL_PROJECT;
    public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = ForeignKeys0.POLICY__PM_FK_POLICY_RUN;
    public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = ForeignKeys0.PROJECT__PM_FK_PROJECT_PATHMIND_USER;
    public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = ForeignKeys0.REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY;
    public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = ForeignKeys0.RUN__PM_FK_RUN_EXPERIMENT;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<ExecutionProviderMetaDataRecord, Long> IDENTITY_EXECUTION_PROVIDER_META_DATA = Internal.createIdentity(ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.ID);
        public static Identity<ExperimentRecord, Long> IDENTITY_EXPERIMENT = Internal.createIdentity(Experiment.EXPERIMENT, Experiment.EXPERIMENT.ID);
        public static Identity<ModelRecord, Long> IDENTITY_MODEL = Internal.createIdentity(Model.MODEL, Model.MODEL.ID);
        public static Identity<PathmindUserRecord, Long> IDENTITY_PATHMIND_USER = Internal.createIdentity(PathmindUser.PATHMIND_USER, PathmindUser.PATHMIND_USER.ID);
        public static Identity<PolicyRecord, Long> IDENTITY_POLICY = Internal.createIdentity(Policy.POLICY, Policy.POLICY.ID);
        public static Identity<ProjectRecord, Long> IDENTITY_PROJECT = Internal.createIdentity(Project.PROJECT, Project.PROJECT.ID);
        public static Identity<RunRecord, Long> IDENTITY_RUN = Internal.createIdentity(Run.RUN, Run.RUN.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<ExecutionProviderMetaDataRecord> EXECUTION_PROVIDER_META_DATA_PKEY = Internal.createUniqueKey(ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, "execution_provider_meta_data_pkey", ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.ID);
        public static final UniqueKey<ExecutionProviderMetaDataRecord> UNIQUE_PROVIDER_CLASS_TYPE_KEY = Internal.createUniqueKey(ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, "unique_provider_class_type_key", ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.TYPE, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.KEY);
        public static final UniqueKey<ExperimentRecord> EXPERIMENT_PKEY = Internal.createUniqueKey(Experiment.EXPERIMENT, "experiment_pkey", Experiment.EXPERIMENT.ID);
        public static final UniqueKey<ModelRecord> MODEL_PKEY = Internal.createUniqueKey(Model.MODEL, "model_pkey", Model.MODEL.ID);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_PKEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_pkey", PathmindUser.PATHMIND_USER.ID);
        public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_EMAIL_KEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, "pathmind_user_email_key", PathmindUser.PATHMIND_USER.EMAIL);
        public static final UniqueKey<PolicyRecord> POLICY_PKEY = Internal.createUniqueKey(Policy.POLICY, "policy_pkey", Policy.POLICY.ID);
        public static final UniqueKey<PolicyRecord> POLICY_RUN_ID_EXTERNAL_ID_KEY = Internal.createUniqueKey(Policy.POLICY, "policy_run_id_external_id_key", Policy.POLICY.RUN_ID, Policy.POLICY.EXTERNAL_ID);
        public static final UniqueKey<ProjectRecord> PROJECT_PKEY = Internal.createUniqueKey(Project.PROJECT, "project_pkey", Project.PROJECT.ID);
        public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Internal.createUniqueKey(Project.PROJECT, "unique_project_name_pathmind_user_id", Project.PROJECT.PATHMIND_USER_ID, Project.PROJECT.NAME);
        public static final UniqueKey<RunRecord> RUN_PKEY = Internal.createUniqueKey(Run.RUN, "run_pkey", Run.RUN.ID);
        public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = Internal.createUniqueKey(TrainerJob.TRAINER_JOB, "trainer_job_pkey", TrainerJob.TRAINER_JOB.JOB_ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.MODEL_PKEY, Experiment.EXPERIMENT, "experiment__pm_fk_experiment_model", Experiment.EXPERIMENT.MODEL_ID);
        public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.PROJECT_PKEY, Model.MODEL, "model__pm_fk_model_project", Model.MODEL.PROJECT_ID);
        public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.RUN_PKEY, Policy.POLICY, "policy__pm_fk_policy_run", Policy.POLICY.RUN_ID);
        public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.PATHMIND_USER_PKEY, Project.PROJECT, "project__pm_fk_project_pathmind_user", Project.PROJECT.PATHMIND_USER_ID);
        public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.POLICY_PKEY, RewardScore.REWARD_SCORE, "reward_score__pm_fk_reward_score_policy", RewardScore.REWARD_SCORE.POLICY_ID);
        public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = Internal.createForeignKey(io.skymind.pathmind.data.db.Keys.EXPERIMENT_PKEY, Run.RUN, "run__pm_fk_run_experiment", Run.RUN.EXPERIMENT_ID);
    }
}
