/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables;


import io.skymind.pathmind.data.db.Indexes;
import io.skymind.pathmind.data.db.Keys;
import io.skymind.pathmind.data.db.Public;
import io.skymind.pathmind.data.db.tables.records.RewardScoreRecord;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
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
public class RewardScore extends TableImpl<RewardScoreRecord> {

    private static final long serialVersionUID = 540202254;

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
    public final TableField<RewardScoreRecord, Long> POLICY_ID = createField(DSL.name("policy_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.reward_score.min</code>.
     */
    public final TableField<RewardScoreRecord, BigDecimal> MIN = createField(DSL.name("min"), org.jooq.impl.SQLDataType.NUMERIC(27, 17), this, "");

    /**
     * The column <code>public.reward_score.mean</code>.
     */
    public final TableField<RewardScoreRecord, BigDecimal> MEAN = createField(DSL.name("mean"), org.jooq.impl.SQLDataType.NUMERIC(27, 17), this, "");

    /**
     * The column <code>public.reward_score.max</code>.
     */
    public final TableField<RewardScoreRecord, BigDecimal> MAX = createField(DSL.name("max"), org.jooq.impl.SQLDataType.NUMERIC(27, 17), this, "");

    /**
     * The column <code>public.reward_score.iteration</code>.
     */
    public final TableField<RewardScoreRecord, Integer> ITERATION = createField(DSL.name("iteration"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>public.reward_score</code> table reference
     */
    public RewardScore() {
        this(DSL.name("reward_score"), null);
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

    private RewardScore(Name alias, Table<RewardScoreRecord> aliased) {
        this(alias, aliased, null);
    }

    private RewardScore(Name alias, Table<RewardScoreRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
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

    public Policy policy() {
        return new Policy(this, Keys.REWARD_SCORE__PM_FK_REWARD_SCORE_POLICY);
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
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, BigDecimal, BigDecimal, BigDecimal, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
