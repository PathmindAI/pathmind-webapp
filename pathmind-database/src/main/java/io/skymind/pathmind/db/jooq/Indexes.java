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
import io.skymind.pathmind.db.jooq.tables.RewardTerm;
import io.skymind.pathmind.db.jooq.tables.RewardVariable;
import io.skymind.pathmind.db.jooq.tables.Run;
import io.skymind.pathmind.db.jooq.tables.SimulationParameter;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index EXPERIMENT_MODEL_FK_INDEX = Internal.createIndex(DSL.name("experiment_model_fk_index"), Experiment.EXPERIMENT, new OrderField[] { Experiment.EXPERIMENT.MODEL_ID }, false);
    public static final Index EXPERIMENT_OBSERVATION_EXPERIMENT_ID_INDEX = Internal.createIndex(DSL.name("experiment_observation_experiment_id_index"), ExperimentObservation.EXPERIMENT_OBSERVATION, new OrderField[] { ExperimentObservation.EXPERIMENT_OBSERVATION.EXPERIMENT_ID }, false);
    public static final Index EXPERIMENT_OBSERVATION_OBSERVATION_ID_INDEX = Internal.createIndex(DSL.name("experiment_observation_observation_id_index"), ExperimentObservation.EXPERIMENT_OBSERVATION, new OrderField[] { ExperimentObservation.EXPERIMENT_OBSERVATION.OBSERVATION_ID }, false);
    public static final Index METRICS_POLICY_ID_INDEX = Internal.createIndex(DSL.name("metrics_policy_id_index"), Metrics.METRICS, new OrderField[] { Metrics.METRICS.POLICY_ID }, false);
    public static final Index METRICS_RAW_POLICY_ID_INDEX = Internal.createIndex(DSL.name("metrics_raw_policy_id_index"), MetricsRaw.METRICS_RAW, new OrderField[] { MetricsRaw.METRICS_RAW.POLICY_ID }, false);
    public static final Index MODEL_PROJECT_FK_INDEX = Internal.createIndex(DSL.name("model_project_fk_index"), Model.MODEL, new OrderField[] { Model.MODEL.PROJECT_ID }, false);
    public static final Index OBSERVATION_MODEL_FK_INDEX = Internal.createIndex(DSL.name("observation_model_fk_index"), Observation.OBSERVATION, new OrderField[] { Observation.OBSERVATION.MODEL_ID }, false);
    public static final Index PATHMIND_USER_API_KEY_CREATED_AT_IDX = Internal.createIndex(DSL.name("pathmind_user_api_key_created_at_idx"), PathmindUser.PATHMIND_USER, new OrderField[] { PathmindUser.PATHMIND_USER.API_KEY_CREATED_AT }, false);
    public static final Index POLICY_RUN_FK_INDEX = Internal.createIndex(DSL.name("policy_run_fk_index"), Policy.POLICY, new OrderField[] { Policy.POLICY.RUN_ID }, false);
    public static final Index PROJECT_PATHMIND_USER_FK_INDEX = Internal.createIndex(DSL.name("project_pathmind_user_fk_index"), Project.PROJECT, new OrderField[] { Project.PROJECT.PATHMIND_USER_ID }, false);
    public static final Index REWARD_SCORE_POLICY_ID_INDEX = Internal.createIndex(DSL.name("reward_score_policy_id_index"), RewardScore.REWARD_SCORE, new OrderField[] { RewardScore.REWARD_SCORE.POLICY_ID }, false);
    public static final Index REWARD_TERM_EXPERIMENT_ID = Internal.createIndex(DSL.name("reward_term_experiment_id"), RewardTerm.REWARD_TERM, new OrderField[] { RewardTerm.REWARD_TERM.EXPERIMENT_ID }, false);
    public static final Index REWARD_VARIABLE_MODEL_FK_INDEX = Internal.createIndex(DSL.name("reward_variable_model_fk_index"), RewardVariable.REWARD_VARIABLE, new OrderField[] { RewardVariable.REWARD_VARIABLE.MODEL_ID }, false);
    public static final Index RUN_EXPERIMENT_FK_INDEX = Internal.createIndex(DSL.name("run_experiment_fk_index"), Run.RUN, new OrderField[] { Run.RUN.EXPERIMENT_ID }, false);
    public static final Index SIMULATION_PARAMETER_MODEL_ID_EXP_ID_INDEX = Internal.createIndex(DSL.name("simulation_parameter_model_id_exp_id_index"), SimulationParameter.SIMULATION_PARAMETER, new OrderField[] { SimulationParameter.SIMULATION_PARAMETER.MODEL_ID, SimulationParameter.SIMULATION_PARAMETER.EXPERIMENT_ID }, false);
}
