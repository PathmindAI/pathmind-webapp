/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.RewardTermRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
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
public class RewardTerm extends TableImpl<RewardTermRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.reward_term</code>
     */
    public static final RewardTerm REWARD_TERM = new RewardTerm();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RewardTermRecord> getRecordType() {
        return RewardTermRecord.class;
    }

    /**
     * The column <code>public.reward_term.experiment_id</code>.
     */
    public final TableField<RewardTermRecord, Long> EXPERIMENT_ID = createField(DSL.name("experiment_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.reward_term.index</code>.
     */
    public final TableField<RewardTermRecord, Integer> INDEX = createField(DSL.name("index"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.reward_term.weight</code>.
     */
    public final TableField<RewardTermRecord, Double> WEIGHT = createField(DSL.name("weight"), SQLDataType.DOUBLE.nullable(false).defaultValue(DSL.field("1", SQLDataType.DOUBLE)), this, "");

    /**
     * The column <code>public.reward_term.reward_variable_array_index</code>.
     */
    public final TableField<RewardTermRecord, Integer> REWARD_VARIABLE_ARRAY_INDEX = createField(DSL.name("reward_variable_array_index"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.reward_term.goal_condition_type</code>.
     */
    public final TableField<RewardTermRecord, String> GOAL_CONDITION_TYPE = createField(DSL.name("goal_condition_type"), SQLDataType.VARCHAR(16), this, "");

    /**
     * The column <code>public.reward_term.snippet</code>.
     */
    public final TableField<RewardTermRecord, String> SNIPPET = createField(DSL.name("snippet"), SQLDataType.CLOB, this, "");

    private RewardTerm(Name alias, Table<RewardTermRecord> aliased) {
        this(alias, aliased, null);
    }

    private RewardTerm(Name alias, Table<RewardTermRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.reward_term</code> table reference
     */
    public RewardTerm(String alias) {
        this(DSL.name(alias), REWARD_TERM);
    }

    /**
     * Create an aliased <code>public.reward_term</code> table reference
     */
    public RewardTerm(Name alias) {
        this(alias, REWARD_TERM);
    }

    /**
     * Create a <code>public.reward_term</code> table reference
     */
    public RewardTerm() {
        this(DSL.name("reward_term"), null);
    }

    public <O extends Record> RewardTerm(Table<O> child, ForeignKey<O, RewardTermRecord> key) {
        super(child, key, REWARD_TERM);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.REWARD_TERM_EXPERIMENT_ID);
    }

    @Override
    public List<UniqueKey<RewardTermRecord>> getKeys() {
        return Arrays.<UniqueKey<RewardTermRecord>>asList(Keys.REWARD_TERM_EXPERIMENT_ID_INDEX_KEY);
    }

    @Override
    public List<ForeignKey<RewardTermRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RewardTermRecord, ?>>asList(Keys.REWARD_TERM__FK_REWARD_TERMS_EXPERIMENT);
    }

    private transient Experiment _experiment;

    public Experiment experiment() {
        if (_experiment == null)
            _experiment = new Experiment(this, Keys.REWARD_TERM__FK_REWARD_TERMS_EXPERIMENT);

        return _experiment;
    }

    @Override
    public RewardTerm as(String alias) {
        return new RewardTerm(DSL.name(alias), this);
    }

    @Override
    public RewardTerm as(Name alias) {
        return new RewardTerm(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RewardTerm rename(String name) {
        return new RewardTerm(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RewardTerm rename(Name name) {
        return new RewardTerm(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Integer, Double, Integer, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
