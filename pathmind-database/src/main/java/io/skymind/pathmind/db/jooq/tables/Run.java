/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.RunRecord;
import io.skymind.pathmind.shared.services.PolicyServerService.DeploymentStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row18;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.EnumConverter;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Run extends TableImpl<RunRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.run</code>
     */
    public static final Run RUN = new Run();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RunRecord> getRecordType() {
        return RunRecord.class;
    }

    /**
     * The column <code>public.run.id</code>.
     */
    public final TableField<RunRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.run.experiment_id</code>.
     */
    public final TableField<RunRecord, Long> EXPERIMENT_ID = createField(DSL.name("experiment_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.run.name</code>.
     */
    public final TableField<RunRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.run.run_type</code>.
     */
    public final TableField<RunRecord, Integer> RUN_TYPE = createField(DSL.name("run_type"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.run.started_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> STARTED_AT = createField(DSL.name("started_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.run.stopped_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> STOPPED_AT = createField(DSL.name("stopped_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.run.status</code>.
     */
    public final TableField<RunRecord, Integer> STATUS = createField(DSL.name("status"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.run.notification_sent_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> NOTIFICATION_SENT_AT = createField(DSL.name("notification_sent_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.run.training_error_id</code>.
     */
    public final TableField<RunRecord, Long> TRAINING_ERROR_ID = createField(DSL.name("training_error_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.run.job_id</code>.
     */
    public final TableField<RunRecord, String> JOB_ID = createField(DSL.name("job_id"), SQLDataType.VARCHAR(36), this, "");

    /**
     * The column <code>public.run.rllib_error</code>.
     */
    public final TableField<RunRecord, String> RLLIB_ERROR = createField(DSL.name("rllib_error"), SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>public.run.ec2_created_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> EC2_CREATED_AT = createField(DSL.name("ec2_created_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.run.success_message</code>.
     */
    public final TableField<RunRecord, String> SUCCESS_MESSAGE = createField(DSL.name("success_message"), SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>public.run.warning_message</code>.
     */
    public final TableField<RunRecord, String> WARNING_MESSAGE = createField(DSL.name("warning_message"), SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>public.run.completing_updates_attempts</code>.
     */
    public final TableField<RunRecord, Integer> COMPLETING_UPDATES_ATTEMPTS = createField(DSL.name("completing_updates_attempts"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.run.policy_server_status</code>.
     */
    public final TableField<RunRecord, DeploymentStatus> POLICY_SERVER_STATUS = createField(DSL.name("policy_server_status"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "", new EnumConverter<Integer, DeploymentStatus>(Integer.class, DeploymentStatus.class));

    /**
     * The column <code>public.run.policy_server_message</code>.
     */
    public final TableField<RunRecord, String> POLICY_SERVER_MESSAGE = createField(DSL.name("policy_server_message"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.run.policy_server_url</code>.
     */
    public final TableField<RunRecord, String> POLICY_SERVER_URL = createField(DSL.name("policy_server_url"), SQLDataType.VARCHAR(255), this, "");

    private Run(Name alias, Table<RunRecord> aliased) {
        this(alias, aliased, null);
    }

    private Run(Name alias, Table<RunRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.run</code> table reference
     */
    public Run(String alias) {
        this(DSL.name(alias), RUN);
    }

    /**
     * Create an aliased <code>public.run</code> table reference
     */
    public Run(Name alias) {
        this(alias, RUN);
    }

    /**
     * Create a <code>public.run</code> table reference
     */
    public Run() {
        this(DSL.name("run"), null);
    }

    public <O extends Record> Run(Table<O> child, ForeignKey<O, RunRecord> key) {
        super(child, key, RUN);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.RUN_EXPERIMENT_FK_INDEX);
    }

    @Override
    public Identity<RunRecord, Long> getIdentity() {
        return (Identity<RunRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<RunRecord> getPrimaryKey() {
        return Keys.RUN_PKEY;
    }

    @Override
    public List<UniqueKey<RunRecord>> getKeys() {
        return Arrays.<UniqueKey<RunRecord>>asList(Keys.RUN_PKEY, Keys.POLICY_SERVER_URL_UNIQUE);
    }

    @Override
    public List<ForeignKey<RunRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RunRecord, ?>>asList(Keys.RUN__PM_FK_RUN_EXPERIMENT, Keys.RUN__PM_FK_TRAINING_ERROR);
    }

    private transient Experiment _experiment;
    private transient TrainingError _trainingError;

    public Experiment experiment() {
        if (_experiment == null)
            _experiment = new Experiment(this, Keys.RUN__PM_FK_RUN_EXPERIMENT);

        return _experiment;
    }

    public TrainingError trainingError() {
        if (_trainingError == null)
            _trainingError = new TrainingError(this, Keys.RUN__PM_FK_TRAINING_ERROR);

        return _trainingError;
    }

    @Override
    public Run as(String alias) {
        return new Run(DSL.name(alias), this);
    }

    @Override
    public Run as(Name alias) {
        return new Run(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Run rename(String name) {
        return new Run(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Run rename(Name name) {
        return new Run(name, null);
    }

    // -------------------------------------------------------------------------
    // Row18 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row18<Long, Long, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, Long, String, String, LocalDateTime, String, String, Integer, DeploymentStatus, String, String> fieldsRow() {
        return (Row18) super.fieldsRow();
    }
}
