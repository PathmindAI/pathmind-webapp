/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Guide;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class GuideRecord extends TableRecordImpl<GuideRecord> implements Record2<Long, Integer> {

    private static final long serialVersionUID = -718602504;

    /**
     * Setter for <code>public.guide.project_id</code>.
     */
    public void setProjectId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.guide.project_id</code>.
     */
    public Long getProjectId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.guide.step</code>.
     */
    public void setStep(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.guide.step</code>.
     */
    public Integer getStep() {
        return (Integer) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, Integer> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Guide.GUIDE.PROJECT_ID;
    }

    @Override
    public Field<Integer> field2() {
        return Guide.GUIDE.STEP;
    }

    @Override
    public Long component1() {
        return getProjectId();
    }

    @Override
    public Integer component2() {
        return getStep();
    }

    @Override
    public Long value1() {
        return getProjectId();
    }

    @Override
    public Integer value2() {
        return getStep();
    }

    @Override
    public GuideRecord value1(Long value) {
        setProjectId(value);
        return this;
    }

    @Override
    public GuideRecord value2(Integer value) {
        setStep(value);
        return this;
    }

    @Override
    public GuideRecord values(Long value1, Integer value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GuideRecord
     */
    public GuideRecord() {
        super(Guide.GUIDE);
    }

    /**
     * Create a detached, initialised GuideRecord
     */
    public GuideRecord(Long projectId, Integer step) {
        super(Guide.GUIDE);

        set(0, projectId);
        set(1, step);
    }
}
