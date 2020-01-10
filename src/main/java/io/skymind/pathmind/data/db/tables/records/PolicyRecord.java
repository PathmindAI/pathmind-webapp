/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Policy;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record14;
import org.jooq.Row14;
import org.jooq.impl.UpdatableRecordImpl;


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
public class PolicyRecord extends UpdatableRecordImpl<PolicyRecord> implements Record14<Long, Long, String, String, JSONB, byte[], LocalDateTime, LocalDateTime, String, byte[], Double, Double, Integer, String> {

    private static final long serialVersionUID = 23690080;

    /**
     * Setter for <code>public.policy.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.policy.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.policy.run_id</code>.
     */
    public void setRunId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.policy.run_id</code>.
     */
    public Long getRunId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.policy.external_id</code>.
     */
    public void setExternalId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.policy.external_id</code>.
     */
    public String getExternalId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.policy.name</code>.
     */
    public void setName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.policy.name</code>.
     */
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.policy.progress</code>.
     */
    public void setProgress(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.policy.progress</code>.
     */
    public JSONB getProgress() {
        return (JSONB) get(4);
    }

    /**
     * Setter for <code>public.policy.file</code>.
     */
    public void setFile(byte... value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.policy.file</code>.
     */
    public byte[] getFile() {
        return (byte[]) get(5);
    }

    /**
     * Setter for <code>public.policy.started_at</code>.
     */
    public void setStartedAt(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.policy.started_at</code>.
     */
    public LocalDateTime getStartedAt() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>public.policy.stopped_at</code>.
     */
    public void setStoppedAt(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.policy.stopped_at</code>.
     */
    public LocalDateTime getStoppedAt() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>public.policy.algorithm</code>.
     */
    public void setAlgorithm(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.policy.algorithm</code>.
     */
    public String getAlgorithm() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.policy.snapshot</code>.
     */
    public void setSnapshot(byte... value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.policy.snapshot</code>.
     */
    public byte[] getSnapshot() {
        return (byte[]) get(9);
    }

    /**
     * Setter for <code>public.policy.learning_rate</code>.
     */
    public void setLearningRate(Double value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.policy.learning_rate</code>.
     */
    public Double getLearningRate() {
        return (Double) get(10);
    }

    /**
     * Setter for <code>public.policy.gamma</code>.
     */
    public void setGamma(Double value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.policy.gamma</code>.
     */
    public Double getGamma() {
        return (Double) get(11);
    }

    /**
     * Setter for <code>public.policy.batch_size</code>.
     */
    public void setBatchSize(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.policy.batch_size</code>.
     */
    public Integer getBatchSize() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>public.policy.notes</code>.
     */
    public void setNotes(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.policy.notes</code>.
     */
    public String getNotes() {
        return (String) get(13);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record14 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row14<Long, Long, String, String, JSONB, byte[], LocalDateTime, LocalDateTime, String, byte[], Double, Double, Integer, String> fieldsRow() {
        return (Row14) super.fieldsRow();
    }

    @Override
    public Row14<Long, Long, String, String, JSONB, byte[], LocalDateTime, LocalDateTime, String, byte[], Double, Double, Integer, String> valuesRow() {
        return (Row14) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Policy.POLICY.ID;
    }

    @Override
    public Field<Long> field2() {
        return Policy.POLICY.RUN_ID;
    }

    @Override
    public Field<String> field3() {
        return Policy.POLICY.EXTERNAL_ID;
    }

    @Override
    public Field<String> field4() {
        return Policy.POLICY.NAME;
    }

    @Override
    public Field<JSONB> field5() {
        return Policy.POLICY.PROGRESS;
    }

    @Override
    public Field<byte[]> field6() {
        return Policy.POLICY.FILE;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Policy.POLICY.STARTED_AT;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return Policy.POLICY.STOPPED_AT;
    }

    @Override
    public Field<String> field9() {
        return Policy.POLICY.ALGORITHM;
    }

    @Override
    public Field<byte[]> field10() {
        return Policy.POLICY.SNAPSHOT;
    }

    @Override
    public Field<Double> field11() {
        return Policy.POLICY.LEARNING_RATE;
    }

    @Override
    public Field<Double> field12() {
        return Policy.POLICY.GAMMA;
    }

    @Override
    public Field<Integer> field13() {
        return Policy.POLICY.BATCH_SIZE;
    }

    @Override
    public Field<String> field14() {
        return Policy.POLICY.NOTES;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getRunId();
    }

    @Override
    public String component3() {
        return getExternalId();
    }

    @Override
    public String component4() {
        return getName();
    }

    @Override
    public JSONB component5() {
        return getProgress();
    }

    @Override
    public byte[] component6() {
        return getFile();
    }

    @Override
    public LocalDateTime component7() {
        return getStartedAt();
    }

    @Override
    public LocalDateTime component8() {
        return getStoppedAt();
    }

    @Override
    public String component9() {
        return getAlgorithm();
    }

    @Override
    public byte[] component10() {
        return getSnapshot();
    }

    @Override
    public Double component11() {
        return getLearningRate();
    }

    @Override
    public Double component12() {
        return getGamma();
    }

    @Override
    public Integer component13() {
        return getBatchSize();
    }

    @Override
    public String component14() {
        return getNotes();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getRunId();
    }

    @Override
    public String value3() {
        return getExternalId();
    }

    @Override
    public String value4() {
        return getName();
    }

    @Override
    public JSONB value5() {
        return getProgress();
    }

    @Override
    public byte[] value6() {
        return getFile();
    }

    @Override
    public LocalDateTime value7() {
        return getStartedAt();
    }

    @Override
    public LocalDateTime value8() {
        return getStoppedAt();
    }

    @Override
    public String value9() {
        return getAlgorithm();
    }

    @Override
    public byte[] value10() {
        return getSnapshot();
    }

    @Override
    public Double value11() {
        return getLearningRate();
    }

    @Override
    public Double value12() {
        return getGamma();
    }

    @Override
    public Integer value13() {
        return getBatchSize();
    }

    @Override
    public String value14() {
        return getNotes();
    }

    @Override
    public PolicyRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PolicyRecord value2(Long value) {
        setRunId(value);
        return this;
    }

    @Override
    public PolicyRecord value3(String value) {
        setExternalId(value);
        return this;
    }

    @Override
    public PolicyRecord value4(String value) {
        setName(value);
        return this;
    }

    @Override
    public PolicyRecord value5(JSONB value) {
        setProgress(value);
        return this;
    }

    @Override
    public PolicyRecord value6(byte... value) {
        setFile(value);
        return this;
    }

    @Override
    public PolicyRecord value7(LocalDateTime value) {
        setStartedAt(value);
        return this;
    }

    @Override
    public PolicyRecord value8(LocalDateTime value) {
        setStoppedAt(value);
        return this;
    }

    @Override
    public PolicyRecord value9(String value) {
        setAlgorithm(value);
        return this;
    }

    @Override
    public PolicyRecord value10(byte... value) {
        setSnapshot(value);
        return this;
    }

    @Override
    public PolicyRecord value11(Double value) {
        setLearningRate(value);
        return this;
    }

    @Override
    public PolicyRecord value12(Double value) {
        setGamma(value);
        return this;
    }

    @Override
    public PolicyRecord value13(Integer value) {
        setBatchSize(value);
        return this;
    }

    @Override
    public PolicyRecord value14(String value) {
        setNotes(value);
        return this;
    }

    @Override
    public PolicyRecord values(Long value1, Long value2, String value3, String value4, JSONB value5, byte[] value6, LocalDateTime value7, LocalDateTime value8, String value9, byte[] value10, Double value11, Double value12, Integer value13, String value14) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PolicyRecord
     */
    public PolicyRecord() {
        super(Policy.POLICY);
    }

    /**
     * Create a detached, initialised PolicyRecord
     */
    public PolicyRecord(Long id, Long runId, String externalId, String name, JSONB progress, byte[] file, LocalDateTime startedAt, LocalDateTime stoppedAt, String algorithm, byte[] snapshot, Double learningRate, Double gamma, Integer batchSize, String notes) {
        super(Policy.POLICY);

        set(0, id);
        set(1, runId);
        set(2, externalId);
        set(3, name);
        set(4, progress);
        set(5, file);
        set(6, startedAt);
        set(7, stoppedAt);
        set(8, algorithm);
        set(9, snapshot);
        set(10, learningRate);
        set(11, gamma);
        set(12, batchSize);
        set(13, notes);
    }
}
