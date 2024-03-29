/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.RewardScoreRecord;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RewardScore extends TableImpl<RewardScoreRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.reward_score</code>
     */
    public static final RewardScore REWARD_SCORE = new RewardScore();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RewardScoreRecord> getRecordType() {
        return RewardScoreRecord.class;
    }

    /**
     * The column <code>public.reward_score.policy_id</code>.
     */
    public final TableField<RewardScoreRecord, Long> POLICY_ID = createField(DSL.name("policy_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.reward_score.mean</code>.
     */
    public final TableField<RewardScoreRecord, BigDecimal> MEAN = createField(DSL.name("mean"), SQLDataType.NUMERIC(32, 17), this, "");

    /**
     * The column <code>public.reward_score.iteration</code>.
     */
    public final TableField<RewardScoreRecord, Integer> ITERATION = createField(DSL.name("iteration"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.reward_score.episode_count</code>.
     */
    public final TableField<RewardScoreRecord, Integer> EPISODE_COUNT = createField(DSL.name("episode_count"), SQLDataType.INTEGER, this, "");

    private RewardScore(Name alias, Table<RewardScoreRecord> aliased) {
        this(alias, aliased, null);
    }

    private RewardScore(Name alias, Table<RewardScoreRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.reward_score</code> table reference
     */
    public RewardScore(String alias) {
        this(DSL.name(alias), REWARD_SCORE);
    }

    /**
     * Create an aliased <code>public.reward_score</code> table reference
     */
    public RewardScore(Name alias) {
        this(alias, REWARD_SCORE);
    }

    /**
     * Create a <code>public.reward_score</code> table reference
     */
    public RewardScore() {
        this(DSL.name("reward_score"), null);
    }

    public <O extends Record> RewardScore(Table<O> child, ForeignKey<O, RewardScoreRecord> key) {
        super(child, key, REWARD_SCORE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.REWARD_SCORE_POLICY_ID_INDEX);
    }

    @Override
    public List<ForeignKey<RewardScoreRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RewardScoreRecord, ?>>asList(Keys.REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY);
    }

    private transient Policy _policy;

    public Policy policy() {
        if (_policy == null)
            _policy = new Policy(this, Keys.REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY);

        return _policy;
    }

    @Override
    public RewardScore as(String alias) {
        return new RewardScore(DSL.name(alias), this);
    }

    @Override
    public RewardScore as(Name alias) {
        return new RewardScore(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RewardScore rename(String name) {
        return new RewardScore(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RewardScore rename(Name name) {
        return new RewardScore(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, BigDecimal, Integer, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
