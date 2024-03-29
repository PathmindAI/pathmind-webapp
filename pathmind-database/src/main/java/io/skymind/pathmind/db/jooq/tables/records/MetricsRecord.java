/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.Metrics;

import java.math.BigDecimal;

import org.jooq.Field;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MetricsRecord extends TableRecordImpl<MetricsRecord> implements Record7<Long, Integer, BigDecimal, BigDecimal, BigDecimal, Integer, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.metrics.policy_id</code>.
     */
    public void setPolicyId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.metrics.policy_id</code>.
     */
    public Long getPolicyId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.metrics.index</code>.
     */
    public void setIndex(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.metrics.index</code>.
     */
    public Integer getIndex() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.metrics.min</code>.
     */
    public void setMin(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.metrics.min</code>.
     */
    public BigDecimal getMin() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>public.metrics.mean</code>.
     */
    public void setMean(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.metrics.mean</code>.
     */
    public BigDecimal getMean() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for <code>public.metrics.max</code>.
     */
    public void setMax(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.metrics.max</code>.
     */
    public BigDecimal getMax() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>public.metrics.iteration</code>.
     */
    public void setIteration(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.metrics.iteration</code>.
     */
    public Integer getIteration() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>public.metrics.agent</code>.
     */
    public void setAgent(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.metrics.agent</code>.
     */
    public Integer getAgent() {
        return (Integer) get(6);
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Integer, BigDecimal, BigDecimal, BigDecimal, Integer, Integer> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, Integer, BigDecimal, BigDecimal, BigDecimal, Integer, Integer> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Metrics.METRICS.POLICY_ID;
    }

    @Override
    public Field<Integer> field2() {
        return Metrics.METRICS.INDEX;
    }

    @Override
    public Field<BigDecimal> field3() {
        return Metrics.METRICS.MIN;
    }

    @Override
    public Field<BigDecimal> field4() {
        return Metrics.METRICS.MEAN;
    }

    @Override
    public Field<BigDecimal> field5() {
        return Metrics.METRICS.MAX;
    }

    @Override
    public Field<Integer> field6() {
        return Metrics.METRICS.ITERATION;
    }

    @Override
    public Field<Integer> field7() {
        return Metrics.METRICS.AGENT;
    }

    @Override
    public Long component1() {
        return getPolicyId();
    }

    @Override
    public Integer component2() {
        return getIndex();
    }

    @Override
    public BigDecimal component3() {
        return getMin();
    }

    @Override
    public BigDecimal component4() {
        return getMean();
    }

    @Override
    public BigDecimal component5() {
        return getMax();
    }

    @Override
    public Integer component6() {
        return getIteration();
    }

    @Override
    public Integer component7() {
        return getAgent();
    }

    @Override
    public Long value1() {
        return getPolicyId();
    }

    @Override
    public Integer value2() {
        return getIndex();
    }

    @Override
    public BigDecimal value3() {
        return getMin();
    }

    @Override
    public BigDecimal value4() {
        return getMean();
    }

    @Override
    public BigDecimal value5() {
        return getMax();
    }

    @Override
    public Integer value6() {
        return getIteration();
    }

    @Override
    public Integer value7() {
        return getAgent();
    }

    @Override
    public MetricsRecord value1(Long value) {
        setPolicyId(value);
        return this;
    }

    @Override
    public MetricsRecord value2(Integer value) {
        setIndex(value);
        return this;
    }

    @Override
    public MetricsRecord value3(BigDecimal value) {
        setMin(value);
        return this;
    }

    @Override
    public MetricsRecord value4(BigDecimal value) {
        setMean(value);
        return this;
    }

    @Override
    public MetricsRecord value5(BigDecimal value) {
        setMax(value);
        return this;
    }

    @Override
    public MetricsRecord value6(Integer value) {
        setIteration(value);
        return this;
    }

    @Override
    public MetricsRecord value7(Integer value) {
        setAgent(value);
        return this;
    }

    @Override
    public MetricsRecord values(Long value1, Integer value2, BigDecimal value3, BigDecimal value4, BigDecimal value5, Integer value6, Integer value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MetricsRecord
     */
    public MetricsRecord() {
        super(Metrics.METRICS);
    }

    /**
     * Create a detached, initialised MetricsRecord
     */
    public MetricsRecord(Long policyId, Integer index, BigDecimal min, BigDecimal mean, BigDecimal max, Integer iteration, Integer agent) {
        super(Metrics.METRICS);

        setPolicyId(policyId);
        setIndex(index);
        setMin(min);
        setMean(mean);
        setMax(max);
        setIteration(iteration);
        setAgent(agent);
    }
}
