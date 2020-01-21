/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.RewardScore;

import java.math.BigDecimal;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RewardScoreRecord extends TableRecordImpl<RewardScoreRecord> implements Record5<Long, BigDecimal, BigDecimal, BigDecimal, Integer> {

    private static final long serialVersionUID = 215963636;

    /**
     * Setter for <code>public.reward_score.policy_id</code>.
     */
    public void setPolicyId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.reward_score.policy_id</code>.
     */
    public Long getPolicyId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.reward_score.min</code>.
     */
    public void setMin(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.reward_score.min</code>.
     */
    public BigDecimal getMin() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>public.reward_score.mean</code>.
     */
    public void setMean(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.reward_score.mean</code>.
     */
    public BigDecimal getMean() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>public.reward_score.max</code>.
     */
    public void setMax(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.reward_score.max</code>.
     */
    public BigDecimal getMax() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for <code>public.reward_score.iteration</code>.
     */
    public void setIteration(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.reward_score.iteration</code>.
     */
    public Integer getIteration() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, BigDecimal, BigDecimal, BigDecimal, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, BigDecimal, BigDecimal, BigDecimal, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return RewardScore.REWARD_SCORE.POLICY_ID;
    }

    @Override
    public Field<BigDecimal> field2() {
        return RewardScore.REWARD_SCORE.MIN;
    }

    @Override
    public Field<BigDecimal> field3() {
        return RewardScore.REWARD_SCORE.MEAN;
    }

    @Override
    public Field<BigDecimal> field4() {
        return RewardScore.REWARD_SCORE.MAX;
    }

    @Override
    public Field<Integer> field5() {
        return RewardScore.REWARD_SCORE.ITERATION;
    }

    @Override
    public Long component1() {
        return getPolicyId();
    }

    @Override
    public BigDecimal component2() {
        return getMin();
    }

    @Override
    public BigDecimal component3() {
        return getMean();
    }

    @Override
    public BigDecimal component4() {
        return getMax();
    }

    @Override
    public Integer component5() {
        return getIteration();
    }

    @Override
    public Long value1() {
        return getPolicyId();
    }

    @Override
    public BigDecimal value2() {
        return getMin();
    }

    @Override
    public BigDecimal value3() {
        return getMean();
    }

    @Override
    public BigDecimal value4() {
        return getMax();
    }

    @Override
    public Integer value5() {
        return getIteration();
    }

    @Override
    public RewardScoreRecord value1(Long value) {
        setPolicyId(value);
        return this;
    }

    @Override
    public RewardScoreRecord value2(BigDecimal value) {
        setMin(value);
        return this;
    }

    @Override
    public RewardScoreRecord value3(BigDecimal value) {
        setMean(value);
        return this;
    }

    @Override
    public RewardScoreRecord value4(BigDecimal value) {
        setMax(value);
        return this;
    }

    @Override
    public RewardScoreRecord value5(Integer value) {
        setIteration(value);
        return this;
    }

    @Override
    public RewardScoreRecord values(Long value1, BigDecimal value2, BigDecimal value3, BigDecimal value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RewardScoreRecord
     */
    public RewardScoreRecord() {
        super(RewardScore.REWARD_SCORE);
    }

    /**
     * Create a detached, initialised RewardScoreRecord
     */
    public RewardScoreRecord(Long policyId, BigDecimal min, BigDecimal mean, BigDecimal max, Integer iteration) {
        super(RewardScore.REWARD_SCORE);

        set(0, policyId);
        set(1, min);
        set(2, mean);
        set(3, max);
        set(4, iteration);
    }
}
