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
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index EXECUTION_PROVIDER_META_DATA_PKEY = Indexes0.EXECUTION_PROVIDER_META_DATA_PKEY;
    public static final Index UNIQUE_PROVIDER_CLASS_TYPE_KEY = Indexes0.UNIQUE_PROVIDER_CLASS_TYPE_KEY;
    public static final Index EXPERIMENT_PKEY = Indexes0.EXPERIMENT_PKEY;
    public static final Index MODEL_PKEY = Indexes0.MODEL_PKEY;
    public static final Index PATHMIND_USER_EMAIL_KEY = Indexes0.PATHMIND_USER_EMAIL_KEY;
    public static final Index PATHMIND_USER_PKEY = Indexes0.PATHMIND_USER_PKEY;
    public static final Index POLICY_PKEY = Indexes0.POLICY_PKEY;
    public static final Index POLICY_RUN_ID_EXTERNAL_ID_KEY = Indexes0.POLICY_RUN_ID_EXTERNAL_ID_KEY;
    public static final Index PROJECT_PKEY = Indexes0.PROJECT_PKEY;
    public static final Index UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Indexes0.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID;
    public static final Index REWARD_SCORE_POLICY_ID_INDEX = Indexes0.REWARD_SCORE_POLICY_ID_INDEX;
    public static final Index RUN_PKEY = Indexes0.RUN_PKEY;
    public static final Index TRAINER_JOB_PKEY = Indexes0.TRAINER_JOB_PKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index EXECUTION_PROVIDER_META_DATA_PKEY = Internal.createIndex("execution_provider_meta_data_pkey", ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, new OrderField[] { ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.ID }, true);
        public static Index UNIQUE_PROVIDER_CLASS_TYPE_KEY = Internal.createIndex("unique_provider_class_type_key", ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA, new OrderField[] { ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.TYPE, ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA.KEY }, true);
        public static Index EXPERIMENT_PKEY = Internal.createIndex("experiment_pkey", Experiment.EXPERIMENT, new OrderField[] { Experiment.EXPERIMENT.ID }, true);
        public static Index MODEL_PKEY = Internal.createIndex("model_pkey", Model.MODEL, new OrderField[] { Model.MODEL.ID }, true);
        public static Index PATHMIND_USER_EMAIL_KEY = Internal.createIndex("pathmind_user_email_key", PathmindUser.PATHMIND_USER, new OrderField[] { PathmindUser.PATHMIND_USER.EMAIL }, true);
        public static Index PATHMIND_USER_PKEY = Internal.createIndex("pathmind_user_pkey", PathmindUser.PATHMIND_USER, new OrderField[] { PathmindUser.PATHMIND_USER.ID }, true);
        public static Index POLICY_PKEY = Internal.createIndex("policy_pkey", Policy.POLICY, new OrderField[] { Policy.POLICY.ID }, true);
        public static Index POLICY_RUN_ID_EXTERNAL_ID_KEY = Internal.createIndex("policy_run_id_external_id_key", Policy.POLICY, new OrderField[] { Policy.POLICY.RUN_ID, Policy.POLICY.EXTERNAL_ID }, true);
        public static Index PROJECT_PKEY = Internal.createIndex("project_pkey", Project.PROJECT, new OrderField[] { Project.PROJECT.ID }, true);
        public static Index UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Internal.createIndex("unique_project_name_pathmind_user_id", Project.PROJECT, new OrderField[] { Project.PROJECT.PATHMIND_USER_ID, Project.PROJECT.NAME }, true);
        public static Index REWARD_SCORE_POLICY_ID_INDEX = Internal.createIndex("reward_score_policy_id_index", RewardScore.REWARD_SCORE, new OrderField[] { RewardScore.REWARD_SCORE.POLICY_ID }, false);
        public static Index RUN_PKEY = Internal.createIndex("run_pkey", Run.RUN, new OrderField[] { Run.RUN.ID }, true);
        public static Index TRAINER_JOB_PKEY = Internal.createIndex("trainer_job_pkey", TrainerJob.TRAINER_JOB, new OrderField[] { TrainerJob.TRAINER_JOB.JOB_ID }, true);
    }
}
