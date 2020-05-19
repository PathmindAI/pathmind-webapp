/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.Run;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RunRecord extends UpdatableRecordImpl<RunRecord> implements Record10<Long, Long, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, Long, String> {

    private static final long serialVersionUID = -2147172731;

    /**
     * Setter for <code>public.run.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.run.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.run.experiment_id</code>.
     */
    public void setExperimentId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.run.experiment_id</code>.
     */
    public Long getExperimentId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.run.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.run.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.run.run_type</code>.
     */
    public void setRunType(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.run.run_type</code>.
     */
    public Integer getRunType() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.run.started_at</code>.
     */
    public void setStartedAt(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.run.started_at</code>.
     */
    public LocalDateTime getStartedAt() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>public.run.stopped_at</code>.
     */
    public void setStoppedAt(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.run.stopped_at</code>.
     */
    public LocalDateTime getStoppedAt() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.run.status</code>.
     */
    public void setStatus(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.run.status</code>.
     */
    public Integer getStatus() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>public.run.notification_sent_at</code>.
     */
    public void setNotificationSentAt(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.run.notification_sent_at</code>.
     */
    public LocalDateTime getNotificationSentAt() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>public.run.training_error_id</code>.
     */
    public void setTrainingErrorId(Long value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.run.training_error_id</code>.
     */
    public Long getTrainingErrorId() {
        return (Long) get(8);
    }
    
    /**
     * Setter for <code>public.run.job_id</code>.
     */
    public void setJobId(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.run.job_id</code>.
     */
    public String getJobId() {
        return (String) get(9);
    }


    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row10<Long, Long, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, Long, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    @Override
    public Row10<Long, Long, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, Long, String> valuesRow() {
        return (Row10) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Run.RUN.ID;
    }

    @Override
    public Field<Long> field2() {
        return Run.RUN.EXPERIMENT_ID;
    }

    @Override
    public Field<String> field3() {
        return Run.RUN.NAME;
    }

    @Override
    public Field<Integer> field4() {
        return Run.RUN.RUN_TYPE;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Run.RUN.STARTED_AT;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Run.RUN.STOPPED_AT;
    }

    @Override
    public Field<Integer> field7() {
        return Run.RUN.STATUS;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return Run.RUN.NOTIFICATION_SENT_AT;
    }

    @Override
    public Field<Long> field9() {
        return Run.RUN.TRAINING_ERROR_ID;
    }
    
    @Override
    public Field<String> field10() {
        return Run.RUN.JOB_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getExperimentId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public Integer component4() {
        return getRunType();
    }

    @Override
    public LocalDateTime component5() {
        return getStartedAt();
    }

    @Override
    public LocalDateTime component6() {
        return getStoppedAt();
    }

    @Override
    public Integer component7() {
        return getStatus();
    }

    @Override
    public LocalDateTime component8() {
        return getNotificationSentAt();
    }

    @Override
    public Long component9() {
        return getTrainingErrorId();
    }
    
    @Override
    public String component10() {
        return getJobId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getExperimentId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public Integer value4() {
        return getRunType();
    }

    @Override
    public LocalDateTime value5() {
        return getStartedAt();
    }

    @Override
    public LocalDateTime value6() {
        return getStoppedAt();
    }

    @Override
    public Integer value7() {
        return getStatus();
    }

    @Override
    public LocalDateTime value8() {
        return getNotificationSentAt();
    }

    @Override
    public Long value9() {
        return getTrainingErrorId();
    }
    
    @Override
    public String value10() {
        return getJobId();
    }

    @Override
    public RunRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public RunRecord value2(Long value) {
        setExperimentId(value);
        return this;
    }

    @Override
    public RunRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public RunRecord value4(Integer value) {
        setRunType(value);
        return this;
    }

    @Override
    public RunRecord value5(LocalDateTime value) {
        setStartedAt(value);
        return this;
    }

    @Override
    public RunRecord value6(LocalDateTime value) {
        setStoppedAt(value);
        return this;
    }

    @Override
    public RunRecord value7(Integer value) {
        setStatus(value);
        return this;
    }

    @Override
    public RunRecord value8(LocalDateTime value) {
        setNotificationSentAt(value);
        return this;
    }

    @Override
    public RunRecord value9(Long value) {
        setTrainingErrorId(value);
        return this;
    }
    
    public RunRecord value10(String value) {
        setJobId(value);
        return this;
    }

    @Override
    public RunRecord values(Long value1, Long value2, String value3, Integer value4, LocalDateTime value5, LocalDateTime value6, Integer value7, LocalDateTime value8, Long value9, String value10) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RunRecord
     */
    public RunRecord() {
        super(Run.RUN);
    }

    /**
     * Create a detached, initialised RunRecord
     */
    public RunRecord(Long id, Long experimentId, String name, Integer runType, LocalDateTime startedAt, LocalDateTime stoppedAt, Integer status, LocalDateTime notificationSentAt, Long trainingErrorId, String jobId) {
        super(Run.RUN);

        set(0, id);
        set(1, experimentId);
        set(2, name);
        set(3, runType);
        set(4, startedAt);
        set(5, stoppedAt);
        set(6, status);
        set(7, notificationSentAt);
        set(8, trainingErrorId);
        set(9, jobId);
    }
}
