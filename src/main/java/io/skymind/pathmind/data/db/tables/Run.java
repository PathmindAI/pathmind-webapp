/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables;


import io.skymind.pathmind.data.db.Indexes;
import io.skymind.pathmind.data.db.Keys;
import io.skymind.pathmind.data.db.Public;
import io.skymind.pathmind.data.db.tables.records.RunRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class Run extends TableImpl<RunRecord> {

    private static final long serialVersionUID = -1986543581;

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
    public final TableField<RunRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.run.experiment_id</code>.
     */
    public final TableField<RunRecord, Long> EXPERIMENT_ID = createField(DSL.name("experiment_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.run.name</code>.
     */
    public final TableField<RunRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.run.run_type</code>.
     */
    public final TableField<RunRecord, Integer> RUN_TYPE = createField(DSL.name("run_type"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.run.started_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> STARTED_AT = createField(DSL.name("started_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.run.stopped_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> STOPPED_AT = createField(DSL.name("stopped_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.run.status</code>.
     */
    public final TableField<RunRecord, Integer> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.run.notification_sent_at</code>.
     */
    public final TableField<RunRecord, LocalDateTime> NOTIFICATION_SENT_AT = createField(DSL.name("notification_sent_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.run.training_error_id</code>.
     */
    public final TableField<RunRecord, Long> TRAINING_ERROR_ID = createField(DSL.name("training_error_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * Create a <code>public.run</code> table reference
     */
    public Run() {
        this(DSL.name("run"), null);
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

    private Run(Name alias, Table<RunRecord> aliased) {
        this(alias, aliased, null);
    }

    private Run(Name alias, Table<RunRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
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
        return Arrays.<Index>asList(Indexes.RUN_EXPERIMENT_FK_INDEX, Indexes.RUN_PKEY);
    }

    @Override
    public UniqueKey<RunRecord> getPrimaryKey() {
        return Keys.RUN_PKEY;
    }

    @Override
    public List<UniqueKey<RunRecord>> getKeys() {
        return Arrays.<UniqueKey<RunRecord>>asList(Keys.RUN_PKEY);
    }

    @Override
    public List<ForeignKey<RunRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RunRecord, ?>>asList(Keys.RUN__PM_FK_RUN_EXPERIMENT, Keys.RUN__PM_FK_TRAINING_ERROR);
    }

    public Experiment experiment() {
        return new Experiment(this, Keys.RUN__PM_FK_RUN_EXPERIMENT);
    }

    public TrainingError trainingError() {
        return new TrainingError(this, Keys.RUN__PM_FK_TRAINING_ERROR);
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
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, Long, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
