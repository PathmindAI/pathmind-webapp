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

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row20;
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
public class Model extends TableImpl<ModelRecord> {

    private static final long serialVersionUID = -142575343;

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
    public final TableField<ModelRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.model.project_id</code>.
     */
    public final TableField<ModelRecord, Long> PROJECT_ID = createField(DSL.name("project_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.model.name</code>.
     */
    public final TableField<ModelRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("'Initial Model Revision'::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.model.date_created</code>.
     */
    public final TableField<ModelRecord, LocalDateTime> DATE_CREATED = createField(DSL.name("date_created"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.model.last_activity_date</code>.
     */
    public final TableField<ModelRecord, LocalDateTime> LAST_ACTIVITY_DATE = createField(DSL.name("last_activity_date"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.model.number_of_observations</code>.
     */
    public final TableField<ModelRecord, Integer> NUMBER_OF_OBSERVATIONS = createField(DSL.name("number_of_observations"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.model.archived</code>.
     */
    public final TableField<ModelRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.model.user_notes</code>.
     */
    public final TableField<ModelRecord, String> USER_NOTES = createField(DSL.name("user_notes"), org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.model.reward_variables_count</code>.
     */
    public final TableField<ModelRecord, Integer> REWARD_VARIABLES_COUNT = createField(DSL.name("reward_variables_count"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.draft</code>.
     */
    public final TableField<ModelRecord, Boolean> DRAFT = createField(DSL.name("draft"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.model.package_name</code>.
     */
    public final TableField<ModelRecord, String> PACKAGE_NAME = createField(DSL.name("package_name"), org.jooq.impl.SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>public.model.action_tuple_size</code>.
     */
    public final TableField<ModelRecord, Integer> ACTION_TUPLE_SIZE = createField(DSL.name("action_tuple_size"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.invalid_model</code>.
     */
    public final TableField<ModelRecord, Integer> INVALID_MODEL = createField(DSL.name("invalid_model"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.model.has_goals</code>.
     */
    public final TableField<ModelRecord, Boolean> HAS_GOALS = createField(DSL.name("has_goals"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.model.model_type</code>.
     */
    public final TableField<ModelRecord, Integer> MODEL_TYPE = createField(DSL.name("model_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.number_of_agents</code>.
     */
    public final TableField<ModelRecord, Integer> NUMBER_OF_AGENTS = createField(DSL.name("number_of_agents"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("1", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.model.pathmind_helper</code>.
     */
    public final TableField<ModelRecord, String> PATHMIND_HELPER = createField(DSL.name("pathmind_helper"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.main_agent</code>.
     */
    public final TableField<ModelRecord, String> MAIN_AGENT = createField(DSL.name("main_agent"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.experiment_class</code>.
     */
    public final TableField<ModelRecord, String> EXPERIMENT_CLASS = createField(DSL.name("experiment_class"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.model.experiment_type</code>.
     */
    public final TableField<ModelRecord, String> EXPERIMENT_TYPE = createField(DSL.name("experiment_type"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * Create a <code>public.model</code> table reference
     */
    public Model() {
        this(DSL.name("model"), null);
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

    private Model(Name alias, Table<ModelRecord> aliased) {
        this(alias, aliased, null);
    }

    private Model(Name alias, Table<ModelRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
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
        return Arrays.<Index>asList(Indexes.MODEL_PKEY, Indexes.MODEL_PROJECT_FK_INDEX);
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

    public Project project() {
        return new Project(this, Keys.MODEL__PM_FK_MODEL_PROJECT);
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
    // Row20 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row20<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Boolean, String, Integer, Boolean, String, Integer, Integer, Boolean, Integer, Integer, String, String, String, String> fieldsRow() {
        return (Row20) super.fieldsRow();
    }
}
