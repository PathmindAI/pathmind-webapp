/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.ExperimentRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row13;
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
public class Experiment extends TableImpl<ExperimentRecord> {

    private static final long serialVersionUID = -1082679460;

    /**
     * The reference instance of <code>public.experiment</code>
     */
    public static final Experiment EXPERIMENT = new Experiment();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ExperimentRecord> getRecordType() {
        return ExperimentRecord.class;
    }

    /**
     * The column <code>public.experiment.id</code>.
     */
    public final TableField<ExperimentRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('experiment_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.experiment.model_id</code>.
     */
    public final TableField<ExperimentRecord, Long> MODEL_ID = createField(DSL.name("model_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.experiment.name</code>.
     */
    public final TableField<ExperimentRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.experiment.reward_function</code>.
     */
    public final TableField<ExperimentRecord, String> REWARD_FUNCTION = createField(DSL.name("reward_function"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>public.experiment.date_created</code>.
     */
    public final TableField<ExperimentRecord, LocalDateTime> DATE_CREATED = createField(DSL.name("date_created"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.experiment.last_activity_date</code>.
     */
    public final TableField<ExperimentRecord, LocalDateTime> LAST_ACTIVITY_DATE = createField(DSL.name("last_activity_date"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.experiment.archived</code>.
     */
    public final TableField<ExperimentRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.experiment.user_notes</code>.
     */
    public final TableField<ExperimentRecord, String> USER_NOTES = createField(DSL.name("user_notes"), org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.experiment.is_favorite</code>.
     */
    public final TableField<ExperimentRecord, Boolean> IS_FAVORITE = createField(DSL.name("is_favorite"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.experiment.goals_reached</code>.
     */
    public final TableField<ExperimentRecord, Boolean> GOALS_REACHED = createField(DSL.name("goals_reached"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.experiment.has_goals</code>.
     */
    public final TableField<ExperimentRecord, Boolean> HAS_GOALS = createField(DSL.name("has_goals"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.experiment.training_status</code>.
     */
    public final TableField<ExperimentRecord, Integer> TRAINING_STATUS = createField(DSL.name("training_status"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.experiment.shared_with_support</code>.
     */
    public final TableField<ExperimentRecord, Boolean> SHARED_WITH_SUPPORT = createField(DSL.name("shared_with_support"), org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>public.experiment</code> table reference
     */
    public Experiment() {
        this(DSL.name("experiment"), null);
    }

    /**
     * Create an aliased <code>public.experiment</code> table reference
     */
    public Experiment(String alias) {
        this(DSL.name(alias), EXPERIMENT);
    }

    /**
     * Create an aliased <code>public.experiment</code> table reference
     */
    public Experiment(Name alias) {
        this(alias, EXPERIMENT);
    }

    private Experiment(Name alias, Table<ExperimentRecord> aliased) {
        this(alias, aliased, null);
    }

    private Experiment(Name alias, Table<ExperimentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Experiment(Table<O> child, ForeignKey<O, ExperimentRecord> key) {
        super(child, key, EXPERIMENT);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EXPERIMENT_MODEL_FK_INDEX, Indexes.EXPERIMENT_PKEY);
    }

    @Override
    public Identity<ExperimentRecord, Long> getIdentity() {
        return Keys.IDENTITY_EXPERIMENT;
    }

    @Override
    public UniqueKey<ExperimentRecord> getPrimaryKey() {
        return Keys.EXPERIMENT_PKEY;
    }

    @Override
    public List<UniqueKey<ExperimentRecord>> getKeys() {
        return Arrays.<UniqueKey<ExperimentRecord>>asList(Keys.EXPERIMENT_PKEY);
    }

    @Override
    public List<ForeignKey<ExperimentRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ExperimentRecord, ?>>asList(Keys.EXPERIMENT__PM_FK_EXPERIMENT_MODEL);
    }

    public Model model() {
        return new Model(this, Keys.EXPERIMENT__PM_FK_EXPERIMENT_MODEL);
    }

    @Override
    public Experiment as(String alias) {
        return new Experiment(DSL.name(alias), this);
    }

    @Override
    public Experiment as(Name alias) {
        return new Experiment(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Experiment rename(String name) {
        return new Experiment(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Experiment rename(Name name) {
        return new Experiment(name, null);
    }

    // -------------------------------------------------------------------------
    // Row13 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean, String, Boolean, Boolean, Boolean, Integer, Boolean> fieldsRow() {
        return (Row13) super.fieldsRow();
    }
}
