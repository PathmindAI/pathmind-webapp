/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.RewardVariableRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
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
public class RewardVariable extends TableImpl<RewardVariableRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.reward_variable</code>
     */
    public static final RewardVariable REWARD_VARIABLE = new RewardVariable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RewardVariableRecord> getRecordType() {
        return RewardVariableRecord.class;
    }

    /**
     * The column <code>public.reward_variable.id</code>.
     */
    public final TableField<RewardVariableRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.reward_variable.model_id</code>.
     */
    public final TableField<RewardVariableRecord, Long> MODEL_ID = createField(DSL.name("model_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.reward_variable.name</code>.
     */
    public final TableField<RewardVariableRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>public.reward_variable.array_index</code>.
     */
    public final TableField<RewardVariableRecord, Integer> ARRAY_INDEX = createField(DSL.name("array_index"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.reward_variable.data_type</code>.
     */
    public final TableField<RewardVariableRecord, String> DATA_TYPE = createField(DSL.name("data_type"), SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.reward_variable.goal_condition_type</code>.
     */
    public final TableField<RewardVariableRecord, String> GOAL_CONDITION_TYPE = createField(DSL.name("goal_condition_type"), SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.reward_variable.goal_value</code>.
     */
    public final TableField<RewardVariableRecord, Double> GOAL_VALUE = createField(DSL.name("goal_value"), SQLDataType.DOUBLE, this, "");

    private RewardVariable(Name alias, Table<RewardVariableRecord> aliased) {
        this(alias, aliased, null);
    }

    private RewardVariable(Name alias, Table<RewardVariableRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.reward_variable</code> table reference
     */
    public RewardVariable(String alias) {
        this(DSL.name(alias), REWARD_VARIABLE);
    }

    /**
     * Create an aliased <code>public.reward_variable</code> table reference
     */
    public RewardVariable(Name alias) {
        this(alias, REWARD_VARIABLE);
    }

    /**
     * Create a <code>public.reward_variable</code> table reference
     */
    public RewardVariable() {
        this(DSL.name("reward_variable"), null);
    }

    public <O extends Record> RewardVariable(Table<O> child, ForeignKey<O, RewardVariableRecord> key) {
        super(child, key, REWARD_VARIABLE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.REWARD_VARIABLE_MODEL_FK_INDEX);
    }

    @Override
    public Identity<RewardVariableRecord, Long> getIdentity() {
        return (Identity<RewardVariableRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<RewardVariableRecord> getPrimaryKey() {
        return Keys.REWARD_VARIABLE_PKEY;
    }

    @Override
    public List<UniqueKey<RewardVariableRecord>> getKeys() {
        return Arrays.<UniqueKey<RewardVariableRecord>>asList(Keys.REWARD_VARIABLE_PKEY, Keys.REWARD_VARIABLE_MODEL_ID_ARRAY_INDEX_KEY);
    }

    @Override
    public List<ForeignKey<RewardVariableRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RewardVariableRecord, ?>>asList(Keys.REWARD_VARIABLE__PM_FK_REWARD_VARIABLE_MODEL);
    }

    private transient Model _model;

    public Model model() {
        if (_model == null)
            _model = new Model(this, Keys.REWARD_VARIABLE__PM_FK_REWARD_VARIABLE_MODEL);

        return _model;
    }

    @Override
    public RewardVariable as(String alias) {
        return new RewardVariable(DSL.name(alias), this);
    }

    @Override
    public RewardVariable as(Name alias) {
        return new RewardVariable(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RewardVariable rename(String name) {
        return new RewardVariable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RewardVariable rename(Name name) {
        return new RewardVariable(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, String, Integer, String, String, Double> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
