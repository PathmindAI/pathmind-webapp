/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Policy;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
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
public class PolicyRecord extends UpdatableRecordImpl<PolicyRecord> implements Record13<Long, Long, String, String, byte[], LocalDateTime, LocalDateTime, String, byte[], Double, Double, Integer, String> {

    private static final long serialVersionUID = 634519276;

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
     * Setter for <code>public.policy.file</code>.
     */
    public void setFile(byte... value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.policy.file</code>.
     */
    public byte[] getFile() {
        return (byte[]) get(4);
    }

    /**
     * Setter for <code>public.policy.startedat</code>.
     */
    public void setStartedat(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.policy.startedat</code>.
     */
    public LocalDateTime getStartedat() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.policy.stoppedat</code>.
     */
    public void setStoppedat(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.policy.stoppedat</code>.
     */
    public LocalDateTime getStoppedat() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>public.policy.algorithm</code>.
     */
    public void setAlgorithm(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.policy.algorithm</code>.
     */
    public String getAlgorithm() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.policy.snapshot</code>.
     */
    public void setSnapshot(byte... value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.policy.snapshot</code>.
     */
    public byte[] getSnapshot() {
        return (byte[]) get(8);
    }

    /**
     * Setter for <code>public.policy.learning_rate</code>.
     */
    public void setLearningRate(Double value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.policy.learning_rate</code>.
     */
    public Double getLearningRate() {
        return (Double) get(9);
    }

    /**
     * Setter for <code>public.policy.gamma</code>.
     */
    public void setGamma(Double value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.policy.gamma</code>.
     */
    public Double getGamma() {
        return (Double) get(10);
    }

    /**
     * Setter for <code>public.policy.batch_size</code>.
     */
    public void setBatchSize(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.policy.batch_size</code>.
     */
    public Integer getBatchSize() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>public.policy.notes</code>.
     */
    public void setNotes(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.policy.notes</code>.
     */
    public String getNotes() {
        return (String) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Long, String, String, byte[], LocalDateTime, LocalDateTime, String, byte[], Double, Double, Integer, String> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<Long, Long, String, String, byte[], LocalDateTime, LocalDateTime, String, byte[], Double, Double, Integer, String> valuesRow() {
        return (Row13) super.valuesRow();
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
    public Field<byte[]> field5() {
        return Policy.POLICY.FILE;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Policy.POLICY.STARTEDAT;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Policy.POLICY.STOPPEDAT;
    }

    @Override
    public Field<String> field8() {
        return Policy.POLICY.ALGORITHM;
    }

    @Override
    public Field<byte[]> field9() {
        return Policy.POLICY.SNAPSHOT;
    }

    @Override
    public Field<Double> field10() {
        return Policy.POLICY.LEARNING_RATE;
    }

    @Override
    public Field<Double> field11() {
        return Policy.POLICY.GAMMA;
    }

    @Override
    public Field<Integer> field12() {
        return Policy.POLICY.BATCH_SIZE;
    }

    @Override
    public Field<String> field13() {
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
    public byte[] component5() {
        return getFile();
    }

    @Override
    public LocalDateTime component6() {
        return getStartedat();
    }

    @Override
    public LocalDateTime component7() {
        return getStoppedat();
    }

    @Override
    public String component8() {
        return getAlgorithm();
    }

    @Override
    public byte[] component9() {
        return getSnapshot();
    }

    @Override
    public Double component10() {
        return getLearningRate();
    }

    @Override
    public Double component11() {
        return getGamma();
    }

    @Override
    public Integer component12() {
        return getBatchSize();
    }

    @Override
    public String component13() {
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
    public byte[] value5() {
        return getFile();
    }

    @Override
    public LocalDateTime value6() {
        return getStartedat();
    }

    @Override
    public LocalDateTime value7() {
        return getStoppedat();
    }

    @Override
    public String value8() {
        return getAlgorithm();
    }

    @Override
    public byte[] value9() {
        return getSnapshot();
    }

    @Override
    public Double value10() {
        return getLearningRate();
    }

    @Override
    public Double value11() {
        return getGamma();
    }

    @Override
    public Integer value12() {
        return getBatchSize();
    }

    @Override
    public String value13() {
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
    public PolicyRecord value5(byte... value) {
        setFile(value);
        return this;
    }

    @Override
    public PolicyRecord value6(LocalDateTime value) {
        setStartedat(value);
        return this;
    }

    @Override
    public PolicyRecord value7(LocalDateTime value) {
        setStoppedat(value);
        return this;
    }

    @Override
    public PolicyRecord value8(String value) {
        setAlgorithm(value);
        return this;
    }

    @Override
    public PolicyRecord value9(byte... value) {
        setSnapshot(value);
        return this;
    }

    @Override
    public PolicyRecord value10(Double value) {
        setLearningRate(value);
        return this;
    }

    @Override
    public PolicyRecord value11(Double value) {
        setGamma(value);
        return this;
    }

    @Override
    public PolicyRecord value12(Integer value) {
        setBatchSize(value);
        return this;
    }

    @Override
    public PolicyRecord value13(String value) {
        setNotes(value);
        return this;
    }

    @Override
    public PolicyRecord values(Long value1, Long value2, String value3, String value4, byte[] value5, LocalDateTime value6, LocalDateTime value7, String value8, byte[] value9, Double value10, Double value11, Integer value12, String value13) {
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
    public PolicyRecord(Long id, Long runId, String externalId, String name, byte[] file, LocalDateTime startedat, LocalDateTime stoppedat, String algorithm, byte[] snapshot, Double learningRate, Double gamma, Integer batchSize, String notes) {
        super(Policy.POLICY);

        set(0, id);
        set(1, runId);
        set(2, externalId);
        set(3, name);
        set(4, file);
        set(5, startedat);
        set(6, stoppedat);
        set(7, algorithm);
        set(8, snapshot);
        set(9, learningRate);
        set(10, gamma);
        set(11, batchSize);
        set(12, notes);
    }
}
