/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.TrainingError;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class TrainingErrorRecord extends UpdatableRecordImpl<TrainingErrorRecord> implements Record4<Long, String, String, Boolean> {

    private static final long serialVersionUID = -1274908351;

    /**
     * Setter for <code>public.training_error.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.training_error.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.training_error.description</code>.
     */
    public void setDescription(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.training_error.description</code>.
     */
    public String getDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.training_error.advice</code>.
     */
    public void setAdvice(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.training_error.advice</code>.
     */
    public String getAdvice() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.training_error.repeatable</code>.
     */
    public void setRepeatable(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.training_error.repeatable</code>.
     */
    public Boolean getRepeatable() {
        return (Boolean) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, String, String, Boolean> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TrainingError.TRAINING_ERROR.ID;
    }

    @Override
    public Field<String> field2() {
        return TrainingError.TRAINING_ERROR.DESCRIPTION;
    }

    @Override
    public Field<String> field3() {
        return TrainingError.TRAINING_ERROR.ADVICE;
    }

    @Override
    public Field<Boolean> field4() {
        return TrainingError.TRAINING_ERROR.REPEATABLE;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getDescription();
    }

    @Override
    public String component3() {
        return getAdvice();
    }

    @Override
    public Boolean component4() {
        return getRepeatable();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getDescription();
    }

    @Override
    public String value3() {
        return getAdvice();
    }

    @Override
    public Boolean value4() {
        return getRepeatable();
    }

    @Override
    public TrainingErrorRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TrainingErrorRecord value2(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public TrainingErrorRecord value3(String value) {
        setAdvice(value);
        return this;
    }

    @Override
    public TrainingErrorRecord value4(Boolean value) {
        setRepeatable(value);
        return this;
    }

    @Override
    public TrainingErrorRecord values(Long value1, String value2, String value3, Boolean value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TrainingErrorRecord
     */
    public TrainingErrorRecord() {
        super(TrainingError.TRAINING_ERROR);
    }

    /**
     * Create a detached, initialised TrainingErrorRecord
     */
    public TrainingErrorRecord(Long id, String description, String advice, Boolean repeatable) {
        super(TrainingError.TRAINING_ERROR);

        set(0, id);
        set(1, description);
        set(2, advice);
        set(3, repeatable);
    }
}
