/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Model;

import java.time.LocalDate;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


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
public class ModelRecord extends UpdatableRecordImpl<ModelRecord> implements Record5<Long, Long, String, LocalDate, LocalDate> {

    private static final long serialVersionUID = 859857848;

    /**
     * Setter for <code>public.model.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.model.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.model.project_id</code>.
     */
    public void setProjectId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.model.project_id</code>.
     */
    public Long getProjectId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.model.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.model.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.model.date_created</code>.
     */
    public void setDateCreated(LocalDate value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.model.date_created</code>.
     */
    public LocalDate getDateCreated() {
        return (LocalDate) get(3);
    }

    /**
     * Setter for <code>public.model.last_activity_date</code>.
     */
    public void setLastActivityDate(LocalDate value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.model.last_activity_date</code>.
     */
    public LocalDate getLastActivityDate() {
        return (LocalDate) get(4);
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
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Long, Long, String, LocalDate, LocalDate> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Long, Long, String, LocalDate, LocalDate> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Model.MODEL.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Model.MODEL.PROJECT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Model.MODEL.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDate> field4() {
        return Model.MODEL.DATE_CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDate> field5() {
        return Model.MODEL.LAST_ACTIVITY_DATE;
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
    public Long component2() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate component4() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate component5() {
        return getLastActivityDate();
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
    public Long value2() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate value4() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate value5() {
        return getLastActivityDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRecord value2(Long value) {
        setProjectId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRecord value4(LocalDate value) {
        setDateCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRecord value5(LocalDate value) {
        setLastActivityDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRecord values(Long value1, Long value2, String value3, LocalDate value4, LocalDate value5) {
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
     * Create a detached ModelRecord
     */
    public ModelRecord() {
        super(Model.MODEL);
    }

    /**
     * Create a detached, initialised ModelRecord
     */
    public ModelRecord(Long id, Long projectId, String name, LocalDate dateCreated, LocalDate lastActivityDate) {
        super(Model.MODEL);

        set(0, id);
        set(1, projectId);
        set(2, name);
        set(3, dateCreated);
        set(4, lastActivityDate);
    }
}
