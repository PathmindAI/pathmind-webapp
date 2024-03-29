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
import io.skymind.pathmind.db.jooq.tables.RunAdminNote;
import io.skymind.pathmind.db.jooq.tables.SimulationParameter;
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
import io.skymind.pathmind.db.jooq.tables.records.RewardTermRecord;
import io.skymind.pathmind.db.jooq.tables.records.RewardVariableRecord;
import io.skymind.pathmind.db.jooq.tables.records.RunAdminNoteRecord;
import io.skymind.pathmind.db.jooq.tables.records.RunRecord;
import io.skymind.pathmind.db.jooq.tables.records.SimulationParameterRecord;
import io.skymind.pathmind.db.jooq.tables.records.TrainerJobRecord;
import io.skymind.pathmind.db.jooq.tables.records.TrainingErrorRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ExperimentRecord> EXPERIMENT_PKEY = Internal.createUniqueKey(Experiment.EXPERIMENT, DSL.name("experiment_pkey"), new TableField[] { Experiment.EXPERIMENT.ID }, true);
    public static final UniqueKey<ExperimentObservationRecord> PK_EXPERIMENT_OBSERVATION = Internal.createUniqueKey(ExperimentObservation.EXPERIMENT_OBSERVATION, DSL.name("pk_experiment_observation"), new TableField[] { ExperimentObservation.EXPERIMENT_OBSERVATION.EXPERIMENT_ID, ExperimentObservation.EXPERIMENT_OBSERVATION.OBSERVATION_ID }, true);
    public static final UniqueKey<ModelRecord> MODEL_PKEY = Internal.createUniqueKey(Model.MODEL, DSL.name("model_pkey"), new TableField[] { Model.MODEL.ID }, true);
    public static final UniqueKey<ObservationRecord> OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY = Internal.createUniqueKey(Observation.OBSERVATION, DSL.name("observation_model_id_array_index_key"), new TableField[] { Observation.OBSERVATION.MODEL_ID, Observation.OBSERVATION.ARRAY_INDEX }, true);
    public static final UniqueKey<ObservationRecord> OBSERVATION_PKEY = Internal.createUniqueKey(Observation.OBSERVATION, DSL.name("observation_pkey"), new TableField[] { Observation.OBSERVATION.ID }, true);
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_API_KEY_KEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, DSL.name("pathmind_user_api_key_key"), new TableField[] { PathmindUser.PATHMIND_USER.API_KEY }, true);
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_EMAIL_KEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, DSL.name("pathmind_user_email_key"), new TableField[] { PathmindUser.PATHMIND_USER.EMAIL }, true);
    public static final UniqueKey<PathmindUserRecord> PATHMIND_USER_PKEY = Internal.createUniqueKey(PathmindUser.PATHMIND_USER, DSL.name("pathmind_user_pkey"), new TableField[] { PathmindUser.PATHMIND_USER.ID }, true);
    public static final UniqueKey<PolicyRecord> POLICY_PKEY = Internal.createUniqueKey(Policy.POLICY, DSL.name("policy_pkey"), new TableField[] { Policy.POLICY.ID }, true);
    public static final UniqueKey<PolicyRecord> POLICY_RUN_ID_EXTERNAL_ID_KEY = Internal.createUniqueKey(Policy.POLICY, DSL.name("policy_run_id_external_id_key"), new TableField[] { Policy.POLICY.RUN_ID, Policy.POLICY.EXTERNAL_ID }, true);
    public static final UniqueKey<ProjectRecord> PROJECT_PKEY = Internal.createUniqueKey(Project.PROJECT, DSL.name("project_pkey"), new TableField[] { Project.PROJECT.ID }, true);
    public static final UniqueKey<ProjectRecord> UNIQUE_PROJECT_NAME_PATHMIND_USER_ID = Internal.createUniqueKey(Project.PROJECT, DSL.name("unique_project_name_pathmind_user_id"), new TableField[] { Project.PROJECT.PATHMIND_USER_ID, Project.PROJECT.NAME }, true);
    public static final UniqueKey<RewardTermRecord> REWARD_TERM_EXPERIMENT_ID_INDEX_KEY = Internal.createUniqueKey(RewardTerm.REWARD_TERM, DSL.name("reward_term_experiment_id_index_key"), new TableField[] { RewardTerm.REWARD_TERM.EXPERIMENT_ID, RewardTerm.REWARD_TERM.INDEX }, true);
    public static final UniqueKey<RewardVariableRecord> REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY = Internal.createUniqueKey(RewardVariable.REWARD_VARIABLE, DSL.name("reward_variable_model_id_array_index_key"), new TableField[] { RewardVariable.REWARD_VARIABLE.MODEL_ID, RewardVariable.REWARD_VARIABLE.ARRAY_INDEX }, true);
    public static final UniqueKey<RewardVariableRecord> REWARD_VARIABLE_PKEY = Internal.createUniqueKey(RewardVariable.REWARD_VARIABLE, DSL.name("reward_variable_pkey"), new TableField[] { RewardVariable.REWARD_VARIABLE.ID }, true);
    public static final UniqueKey<RunRecord> POLICY_SERVER_URL_UNIQUE = Internal.createUniqueKey(Run.RUN, DSL.name("policy_server_url_unique"), new TableField[] { Run.RUN.POLICY_SERVER_URL }, true);
    public static final UniqueKey<RunRecord> RUN_PKEY = Internal.createUniqueKey(Run.RUN, DSL.name("run_pkey"), new TableField[] { Run.RUN.ID }, true);
    public static final UniqueKey<RunAdminNoteRecord> PM_RUN_ADMIN_NOTES_RUN_ID_UNQ = Internal.createUniqueKey(RunAdminNote.RUN_ADMIN_NOTE, DSL.name("pm_run_admin_notes_run_id_unq"), new TableField[] { RunAdminNote.RUN_ADMIN_NOTE.RUN_ID }, true);
    public static final UniqueKey<SimulationParameterRecord> SIMULATION_PARAMETER_MODEL_ID_EXPERIMENT_ID_INDEX_KEY_KEY = Internal.createUniqueKey(SimulationParameter.SIMULATION_PARAMETER, DSL.name("simulation_parameter_model_id_experiment_id_index_key_key"), new TableField[] { SimulationParameter.SIMULATION_PARAMETER.MODEL_ID, SimulationParameter.SIMULATION_PARAMETER.EXPERIMENT_ID, SimulationParameter.SIMULATION_PARAMETER.INDEX, SimulationParameter.SIMULATION_PARAMETER.KEY }, true);
    public static final UniqueKey<TrainerJobRecord> TRAINER_JOB_PKEY = Internal.createUniqueKey(TrainerJob.TRAINER_JOB, DSL.name("trainer_job_pkey"), new TableField[] { TrainerJob.TRAINER_JOB.JOB_ID, TrainerJob.TRAINER_JOB.S3BUCKET }, true);
    public static final UniqueKey<TrainerJobRecord> UNIQUE_JOB_ID_S3BUCKET = Internal.createUniqueKey(TrainerJob.TRAINER_JOB, DSL.name("unique_job_id_s3bucket"), new TableField[] { TrainerJob.TRAINER_JOB.JOB_ID, TrainerJob.TRAINER_JOB.S3BUCKET }, true);
    public static final UniqueKey<TrainingErrorRecord> TRAINING_ERROR_PKEY = Internal.createUniqueKey(TrainingError.TRAINING_ERROR, DSL.name("training_error_pkey"), new TableField[] { TrainingError.TRAINING_ERROR.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ExperimentRecord, ModelRecord> EXPERIMENT__PM_FK_EXPERIMENT_MODEL = Internal.createForeignKey(Experiment.EXPERIMENT, DSL.name("pm_fk_experiment_model"), new TableField[] { Experiment.EXPERIMENT.MODEL_ID }, Keys.MODEL_PKEY, new TableField[] { Model.MODEL.ID }, true);
    public static final ForeignKey<ExperimentObservationRecord, ExperimentRecord> EXPERIMENT_OBSERVATION__PM_FK_EO_EXPERIMENT = Internal.createForeignKey(ExperimentObservation.EXPERIMENT_OBSERVATION, DSL.name("pm_fk_eo_experiment"), new TableField[] { ExperimentObservation.EXPERIMENT_OBSERVATION.EXPERIMENT_ID }, Keys.EXPERIMENT_PKEY, new TableField[] { Experiment.EXPERIMENT.ID }, true);
    public static final ForeignKey<ExperimentObservationRecord, ObservationRecord> EXPERIMENT_OBSERVATION__PM_FK_EO_OBSERVATION = Internal.createForeignKey(ExperimentObservation.EXPERIMENT_OBSERVATION, DSL.name("pm_fk_eo_observation"), new TableField[] { ExperimentObservation.EXPERIMENT_OBSERVATION.OBSERVATION_ID }, Keys.OBSERVATION_PKEY, new TableField[] { Observation.OBSERVATION.ID }, true);
    public static final ForeignKey<MetricsRecord, PolicyRecord> METRICS__PM_FK_METRICS_POLICY = Internal.createForeignKey(Metrics.METRICS, DSL.name("pm_fk_metrics_policy"), new TableField[] { Metrics.METRICS.POLICY_ID }, Keys.POLICY_PKEY, new TableField[] { Policy.POLICY.ID }, true);
    public static final ForeignKey<MetricsRawRecord, PolicyRecord> METRICS_RAW__PM_FK_METRICS_RAW_POLICY = Internal.createForeignKey(MetricsRaw.METRICS_RAW, DSL.name("pm_fk_metrics_raw_policy"), new TableField[] { MetricsRaw.METRICS_RAW.POLICY_ID }, Keys.POLICY_PKEY, new TableField[] { Policy.POLICY.ID }, true);
    public static final ForeignKey<ModelRecord, ProjectRecord> MODEL__PM_FK_MODEL_PROJECT = Internal.createForeignKey(Model.MODEL, DSL.name("pm_fk_model_project"), new TableField[] { Model.MODEL.PROJECT_ID }, Keys.PROJECT_PKEY, new TableField[] { Project.PROJECT.ID }, true);
    public static final ForeignKey<ObservationRecord, ModelRecord> OBSERVATION__PM_FK_OBSERVATION_MODEL = Internal.createForeignKey(Observation.OBSERVATION, DSL.name("pm_fk_observation_model"), new TableField[] { Observation.OBSERVATION.MODEL_ID }, Keys.MODEL_PKEY, new TableField[] { Model.MODEL.ID }, true);
    public static final ForeignKey<PolicyRecord, RunRecord> POLICY__PM_FK_POLICY_RUN = Internal.createForeignKey(Policy.POLICY, DSL.name("pm_fk_policy_run"), new TableField[] { Policy.POLICY.RUN_ID }, Keys.RUN_PKEY, new TableField[] { Run.RUN.ID }, true);
    public static final ForeignKey<ProjectRecord, PathmindUserRecord> PROJECT__PM_FK_PROJECT_PATHMIND_USER = Internal.createForeignKey(Project.PROJECT, DSL.name("pm_fk_project_pathmind_user"), new TableField[] { Project.PROJECT.PATHMIND_USER_ID }, Keys.PATHMIND_USER_PKEY, new TableField[] { PathmindUser.PATHMIND_USER.ID }, true);
    public static final ForeignKey<RewardScoreRecord, PolicyRecord> REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY = Internal.createForeignKey(RewardScore.REWARD_SCORE, DSL.name("pm_fk_reward_score_policy"), new TableField[] { RewardScore.REWARD_SCORE.POLICY_ID }, Keys.POLICY_PKEY, new TableField[] { Policy.POLICY.ID }, true);
    public static final ForeignKey<RewardTermRecord, ExperimentRecord> REWARD_TERM__FK_REWARD_TERMS_EXPERIMENT = Internal.createForeignKey(RewardTerm.REWARD_TERM, DSL.name("fk_reward_terms_experiment"), new TableField[] { RewardTerm.REWARD_TERM.EXPERIMENT_ID }, Keys.EXPERIMENT_PKEY, new TableField[] { Experiment.EXPERIMENT.ID }, true);
    public static final ForeignKey<RewardVariableRecord, ModelRecord> REWARD_VARIABLE__PM_FK_REWARD_VARIABLE_MODEL = Internal.createForeignKey(RewardVariable.REWARD_VARIABLE, DSL.name("pm_fk_reward_variable_model"), new TableField[] { RewardVariable.REWARD_VARIABLE.MODEL_ID }, Keys.MODEL_PKEY, new TableField[] { Model.MODEL.ID }, true);
    public static final ForeignKey<RunRecord, ExperimentRecord> RUN__PM_FK_RUN_EXPERIMENT = Internal.createForeignKey(Run.RUN, DSL.name("pm_fk_run_experiment"), new TableField[] { Run.RUN.EXPERIMENT_ID }, Keys.EXPERIMENT_PKEY, new TableField[] { Experiment.EXPERIMENT.ID }, true);
    public static final ForeignKey<RunRecord, TrainingErrorRecord> RUN__PM_FK_TRAINING_ERROR = Internal.createForeignKey(Run.RUN, DSL.name("pm_fk_training_error"), new TableField[] { Run.RUN.TRAINING_ERROR_ID }, Keys.TRAINING_ERROR_PKEY, new TableField[] { TrainingError.TRAINING_ERROR.ID }, true);
    public static final ForeignKey<RunAdminNoteRecord, RunRecord> RUN_ADMIN_NOTE__PM_FK_RUN_ADMIN_NOTES = Internal.createForeignKey(RunAdminNote.RUN_ADMIN_NOTE, DSL.name("pm_fk_run_admin_notes"), new TableField[] { RunAdminNote.RUN_ADMIN_NOTE.RUN_ID }, Keys.RUN_PKEY, new TableField[] { Run.RUN.ID }, true);
    public static final ForeignKey<SimulationParameterRecord, ExperimentRecord> SIMULATION_PARAMETER__FK_SIMULATION_PARAMETER_EXPERIMENT = Internal.createForeignKey(SimulationParameter.SIMULATION_PARAMETER, DSL.name("fk_simulation_parameter_experiment"), new TableField[] { SimulationParameter.SIMULATION_PARAMETER.EXPERIMENT_ID }, Keys.EXPERIMENT_PKEY, new TableField[] { Experiment.EXPERIMENT.ID }, true);
    public static final ForeignKey<SimulationParameterRecord, ModelRecord> SIMULATION_PARAMETER__FK_SIMULATION_PARAMETER_MODEL = Internal.createForeignKey(SimulationParameter.SIMULATION_PARAMETER, DSL.name("fk_simulation_parameter_model"), new TableField[] { SimulationParameter.SIMULATION_PARAMETER.MODEL_ID }, Keys.MODEL_PKEY, new TableField[] { Model.MODEL.ID }, true);
}
