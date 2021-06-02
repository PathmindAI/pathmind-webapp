/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.MetricsRaw;

import java.math.BigDecimal;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MetricsRawRecord extends TableRecordImpl<MetricsRawRecord> implements Record6<Long, Integer, Integer, Integer, BigDecimal, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.metrics_raw.policy_id</code>.
     */
    public void setPolicyId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.metrics_raw.policy_id</code>.
     */
    public Long getPolicyId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.metrics_raw.iteration</code>.
     */
    public void setIteration(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.metrics_raw.iteration</code>.
     */
    public Integer getIteration() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.metrics_raw.episode</code>.
     */
    public void setEpisode(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.metrics_raw.episode</code>.
     */
    public Integer getEpisode() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.metrics_raw.index</code>.
     */
    public void setIndex(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.metrics_raw.index</code>.
     */
    public Integer getIndex() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.metrics_raw.value</code>.
     */
    public void setValue(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.metrics_raw.value</code>.
     */
    public BigDecimal getValue() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>public.metrics_raw.agent</code>.
     */
    public void setAgent(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.metrics_raw.agent</code>.
     */
    public Integer getAgent() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Integer, Integer, Integer, BigDecimal, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Long, Integer, Integer, Integer, BigDecimal, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return MetricsRaw.METRICS_RAW.POLICY_ID;
    }

    @Override
    public Field<Integer> field2() {
        return MetricsRaw.METRICS_RAW.ITERATION;
    }

    @Override
    public Field<Integer> field3() {
        return MetricsRaw.METRICS_RAW.EPISODE;
    }

    @Override
    public Field<Integer> field4() {
        return MetricsRaw.METRICS_RAW.INDEX;
    }

    @Override
    public Field<BigDecimal> field5() {
        return MetricsRaw.METRICS_RAW.VALUE;
    }

    @Override
    public Field<Integer> field6() {
        return MetricsRaw.METRICS_RAW.AGENT;
    }

    @Override
    public Long component1() {
        return getPolicyId();
    }

    @Override
    public Integer component2() {
        return getIteration();
    }

    @Override
    public Integer component3() {
        return getEpisode();
    }

    @Override
    public Integer component4() {
        return getIndex();
    }

    @Override
    public BigDecimal component5() {
        return getValue();
    }

    @Override
    public Integer component6() {
        return getAgent();
    }

    @Override
    public Long value1() {
        return getPolicyId();
    }

    @Override
    public Integer value2() {
        return getIteration();
    }

    @Override
    public Integer value3() {
        return getEpisode();
    }

    @Override
    public Integer value4() {
        return getIndex();
    }

    @Override
    public BigDecimal value5() {
        return getValue();
    }

    @Override
    public Integer value6() {
        return getAgent();
    }

    @Override
    public MetricsRawRecord value1(Long value) {
        setPolicyId(value);
        return this;
    }

    @Override
    public MetricsRawRecord value2(Integer value) {
        setIteration(value);
        return this;
    }

    @Override
    public MetricsRawRecord value3(Integer value) {
        setEpisode(value);
        return this;
    }

    @Override
    public MetricsRawRecord value4(Integer value) {
        setIndex(value);
        return this;
    }

    @Override
    public MetricsRawRecord value5(BigDecimal value) {
        setValue(value);
        return this;
    }

    @Override
    public MetricsRawRecord value6(Integer value) {
        setAgent(value);
        return this;
    }

    @Override
    public MetricsRawRecord values(Long value1, Integer value2, Integer value3, Integer value4, BigDecimal value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MetricsRawRecord
     */
    public MetricsRawRecord() {
        super(MetricsRaw.METRICS_RAW);
    }

    /**
     * Create a detached, initialised MetricsRawRecord
     */
    public MetricsRawRecord(Long policyId, Integer iteration, Integer episode, Integer index, BigDecimal value, Integer agent) {
        super(MetricsRaw.METRICS_RAW);

        setPolicyId(policyId);
        setIteration(iteration);
        setEpisode(episode);
        setIndex(index);
        setValue(value);
        setAgent(agent);
    }
}
