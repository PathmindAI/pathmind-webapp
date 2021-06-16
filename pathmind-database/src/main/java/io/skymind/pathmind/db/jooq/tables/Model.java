/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.ModelRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row21;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Model extends TableImpl<ModelRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.model</code>
     */
    public static final Model MODEL = new Model();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ModelRecord> getRecordType() {
        return ModelRecord.class;
    }

    /**
     * The column <code>public.model.id</code>.
     */
    public final TableField<ModelRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.model.project_id</code>.
     */
    public final TableField<ModelRecord, Long> PROJECT_ID = createField(DSL.name("project_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.model.name</code>.
     */
    public final TableField<ModelRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false).defaultValue(DSL.field("'Initial Model Revision'::character varying", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.model.date_created</code>.
     */
    public final TableField<ModelRecord, LocalDateTime> DATE_CREATED = createField(DSL.name("date_created"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.model.last_activity_date</code>.
     */
    public final TableField<ModelRecord, LocalDateTime> LAST_ACTIVITY_DATE = createField(DSL.name("last_activity_date"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.model.number_of_observations</code>.
     */
    public final TableField<ModelRecord, Integer> NUMBER_OF_OBSERVATIONS = createField(DSL.name("number_of_observations"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.model.archived</code>.
     */
    public final TableField<ModelRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("false", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.model.user_notes</code>.
     */
    public final TableField<ModelRecord, String> USER_NOTES = createField(DSL.name("user_notes"), SQLDataType.VARCHAR(1000).nullable(false).defaultValue(DSL.field("''::character varying", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.model.reward_variables_count</code>.
     */
    public final TableField<ModelRecord, Integer> REWARD_VARIABLES_COUNT = createField(DSL.name("reward_variables_count"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.draft</code>.
     */
    public final TableField<ModelRecord, Boolean> DRAFT = createField(DSL.name("draft"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("true", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.model.package_name</code>.
     */
    public final TableField<ModelRecord, String> PACKAGE_NAME = createField(DSL.name("package_name"), SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>public.model.action_tuple_size</code>.
     */
    public final TableField<ModelRecord, Integer> ACTION_TUPLE_SIZE = createField(DSL.name("action_tuple_size"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.invalid_model</code>.
     */
    public final TableField<ModelRecord, Integer> INVALID_MODEL = createField(DSL.name("invalid_model"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.model.has_goals</code>.
     */
    public final TableField<ModelRecord, Boolean> HAS_GOALS = createField(DSL.name("has_goals"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("false", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.model.model_type</code>.
     */
    public final TableField<ModelRecord, Integer> MODEL_TYPE = createField(DSL.name("model_type"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.number_of_agents</code>.
     */
    public final TableField<ModelRecord, Integer> NUMBER_OF_AGENTS = createField(DSL.name("number_of_agents"), SQLDataType.INTEGER.defaultValue(DSL.field("1", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.pathmind_helper</code>.
     */
    public final TableField<ModelRecord, String> PATHMIND_HELPER = createField(DSL.name("pathmind_helper"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.main_agent</code>.
     */
    public final TableField<ModelRecord, String> MAIN_AGENT = createField(DSL.name("main_agent"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.experiment_class</code>.
     */
    public final TableField<ModelRecord, String> EXPERIMENT_CLASS = createField(DSL.name("experiment_class"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.experiment_type</code>.
     */
    public final TableField<ModelRecord, String> EXPERIMENT_TYPE = createField(DSL.name("experiment_type"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.actionmask</code>.
     */
    public final TableField<ModelRecord, Boolean> ACTIONMASK = createField(DSL.name("actionmask"), SQLDataType.BOOLEAN.defaultValue(DSL.field("false", SQLDataType.BOOLEAN)), this, "");

    private Model(Name alias, Table<ModelRecord> aliased) {
        this(alias, aliased, null);
    }

    private Model(Name alias, Table<ModelRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.model</code> table reference
     */
    public Model(String alias) {
        this(DSL.name(alias), MODEL);
    }

    /**
     * Create an aliased <code>public.model</code> table reference
     */
    public Model(Name alias) {
        this(alias, MODEL);
    }

    /**
     * Create a <code>public.model</code> table reference
     */
    public Model() {
        this(DSL.name("model"), null);
    }

    public <O extends Record> Model(Table<O> child, ForeignKey<O, ModelRecord> key) {
        super(child, key, MODEL);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.MODEL_PROJECT_FK_INDEX);
    }

    @Override
    public Identity<ModelRecord, Long> getIdentity() {
        return (Identity<ModelRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<ModelRecord> getPrimaryKey() {
        return Keys.MODEL_PKEY;
    }

    @Override
    public List<UniqueKey<ModelRecord>> getKeys() {
        return Arrays.<UniqueKey<ModelRecord>>asList(Keys.MODEL_PKEY);
    }

    @Override
    public List<ForeignKey<ModelRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ModelRecord, ?>>asList(Keys.MODEL__PM_FK_MODEL_PROJECT);
    }

    private transient Project _project;

    public Project project() {
        if (_project == null)
            _project = new Project(this, Keys.MODEL__PM_FK_MODEL_PROJECT);

        return _project;
    }

    @Override
    public Model as(String alias) {
        return new Model(DSL.name(alias), this);
    }

    @Override
    public Model as(Name alias) {
        return new Model(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Model rename(String name) {
        return new Model(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Model rename(Name name) {
        return new Model(name, null);
    }

    // -------------------------------------------------------------------------
    // Row21 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row21<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Boolean, String, Integer, Boolean, String, Integer, Integer, Boolean, Integer, Integer, String, String, String, String, Boolean> fieldsRow() {
        return (Row21) super.fieldsRow();
    }
}
