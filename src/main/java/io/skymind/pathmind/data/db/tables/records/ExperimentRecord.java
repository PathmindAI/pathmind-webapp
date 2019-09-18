/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Experiment;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ExperimentRecord extends UpdatableRecordImpl<ExperimentRecord> implements Record7<Long, String, String, Long, String, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1003460523;

    /**
     * Setter for <code>public.experiment.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.experiment.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.experiment.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.experiment.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.experiment.reward_function</code>.
     */
    public void setRewardFunction(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.experiment.reward_function</code>.
     */
    public String getRewardFunction() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.experiment.model_id</code>.
     */
    public void setModelId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.experiment.model_id</code>.
     */
    public Long getModelId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.experiment.notes</code>.
     */
    public void setNotes(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.experiment.notes</code>.
     */
    public String getNotes() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.experiment.date_created</code>.
     */
    public void setDateCreated(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.experiment.date_created</code>.
     */
    public LocalDateTime getDateCreated() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.experiment.test_run</code>.
     */
    public void setTestRun(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.experiment.test_run</code>.
     */
    public Integer getTestRun() {
        return (Integer) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, String, Long, String, LocalDateTime, Integer> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, String, Long, String, LocalDateTime, Integer> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Experiment.EXPERIMENT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Experiment.EXPERIMENT.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Experiment.EXPERIMENT.REWARD_FUNCTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return Experiment.EXPERIMENT.MODEL_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Experiment.EXPERIMENT.NOTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field6() {
        return Experiment.EXPERIMENT.DATE_CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field7() {
        return Experiment.EXPERIMENT.TEST_RUN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getRewardFunction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getModelId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getNotes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component6() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component7() {
        return getTestRun();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getRewardFunction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getModelId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getNotes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value6() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value7() {
        return getTestRun();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value3(String value) {
        setRewardFunction(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value4(Long value) {
        setModelId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value5(String value) {
        setNotes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value6(LocalDateTime value) {
        setDateCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord value7(Integer value) {
        setTestRun(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExperimentRecord values(Long value1, String value2, String value3, Long value4, String value5, LocalDateTime value6, Integer value7) {
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
     * Create a detached ExperimentRecord
     */
    public ExperimentRecord() {
        super(Experiment.EXPERIMENT);
    }

    /**
     * Create a detached, initialised ExperimentRecord
     */
    public ExperimentRecord(Long id, String name, String rewardFunction, Long modelId, String notes, LocalDateTime dateCreated, Integer testRun) {
        super(Experiment.EXPERIMENT);

        set(0, id);
        set(1, name);
        set(2, rewardFunction);
        set(3, modelId);
        set(4, notes);
        set(5, dateCreated);
        set(6, testRun);
    }
}
