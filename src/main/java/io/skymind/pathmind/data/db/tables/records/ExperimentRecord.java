/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Experiment;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class ExperimentRecord extends UpdatableRecordImpl<ExperimentRecord> implements Record7<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean> {

    private static final long serialVersionUID = -1495541529;

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
     * Setter for <code>public.experiment.model_id</code>.
     */
    public void setModelId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.experiment.model_id</code>.
     */
    public Long getModelId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.experiment.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.experiment.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.experiment.reward_function</code>.
     */
    public void setRewardFunction(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.experiment.reward_function</code>.
     */
    public String getRewardFunction() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.experiment.date_created</code>.
     */
    public void setDateCreated(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.experiment.date_created</code>.
     */
    public LocalDateTime getDateCreated() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>public.experiment.last_activity_date</code>.
     */
    public void setLastActivityDate(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.experiment.last_activity_date</code>.
     */
    public LocalDateTime getLastActivityDate() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.experiment.archived</code>.
     */
    public void setArchived(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.experiment.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Experiment.EXPERIMENT.ID;
    }

    @Override
    public Field<Long> field2() {
        return Experiment.EXPERIMENT.MODEL_ID;
    }

    @Override
    public Field<String> field3() {
        return Experiment.EXPERIMENT.NAME;
    }

    @Override
    public Field<String> field4() {
        return Experiment.EXPERIMENT.REWARD_FUNCTION;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Experiment.EXPERIMENT.DATE_CREATED;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Experiment.EXPERIMENT.LAST_ACTIVITY_DATE;
    }

    @Override
    public Field<Boolean> field7() {
        return Experiment.EXPERIMENT.ARCHIVED;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getModelId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public String component4() {
        return getRewardFunction();
    }

    @Override
    public LocalDateTime component5() {
        return getDateCreated();
    }

    @Override
    public LocalDateTime component6() {
        return getLastActivityDate();
    }

    @Override
    public Boolean component7() {
        return getArchived();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getModelId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public String value4() {
        return getRewardFunction();
    }

    @Override
    public LocalDateTime value5() {
        return getDateCreated();
    }

    @Override
    public LocalDateTime value6() {
        return getLastActivityDate();
    }

    @Override
    public Boolean value7() {
        return getArchived();
    }

    @Override
    public ExperimentRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ExperimentRecord value2(Long value) {
        setModelId(value);
        return this;
    }

    @Override
    public ExperimentRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public ExperimentRecord value4(String value) {
        setRewardFunction(value);
        return this;
    }

    @Override
    public ExperimentRecord value5(LocalDateTime value) {
        setDateCreated(value);
        return this;
    }

    @Override
    public ExperimentRecord value6(LocalDateTime value) {
        setLastActivityDate(value);
        return this;
    }

    @Override
    public ExperimentRecord value7(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public ExperimentRecord values(Long value1, Long value2, String value3, String value4, LocalDateTime value5, LocalDateTime value6, Boolean value7) {
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
    public ExperimentRecord(Long id, Long modelId, String name, String rewardFunction, LocalDateTime dateCreated, LocalDateTime lastActivityDate, Boolean archived) {
        super(Experiment.EXPERIMENT);

        set(0, id);
        set(1, modelId);
        set(2, name);
        set(3, rewardFunction);
        set(4, dateCreated);
        set(5, lastActivityDate);
        set(6, archived);
    }
}
