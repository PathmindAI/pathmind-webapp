/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.TrainerJob;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record14;
import org.jooq.Record2;
import org.jooq.Row14;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TrainerJobRecord extends UpdatableRecordImpl<TrainerJobRecord> implements Record14<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, Integer, String, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.trainer_job.job_id</code>.
     */
    public void setJobId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.trainer_job.job_id</code>.
     */
    public String getJobId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.trainer_job.sqs_url</code>.
     */
    public void setSqsUrl(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.trainer_job.sqs_url</code>.
     */
    public String getSqsUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.trainer_job.s3path</code>.
     */
    public void setS3path(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.trainer_job.s3path</code>.
     */
    public String getS3path() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.trainer_job.s3bucket</code>.
     */
    public void setS3bucket(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.trainer_job.s3bucket</code>.
     */
    public String getS3bucket() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.trainer_job.receipthandle</code>.
     */
    public void setReceipthandle(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.trainer_job.receipthandle</code>.
     */
    public String getReceipthandle() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.trainer_job.ec2_instance_type</code>.
     */
    public void setEc2InstanceType(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.trainer_job.ec2_instance_type</code>.
     */
    public String getEc2InstanceType() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.trainer_job.ec2_max_price</code>.
     */
    public void setEc2MaxPrice(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.trainer_job.ec2_max_price</code>.
     */
    public String getEc2MaxPrice() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.trainer_job.create_date</code>.
     */
    public void setCreateDate(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.trainer_job.create_date</code>.
     */
    public LocalDateTime getCreateDate() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>public.trainer_job.ec2_create_date</code>.
     */
    public void setEc2CreateDate(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.trainer_job.ec2_create_date</code>.
     */
    public LocalDateTime getEc2CreateDate() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>public.trainer_job.ec2_end_date</code>.
     */
    public void setEc2EndDate(LocalDateTime value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.trainer_job.ec2_end_date</code>.
     */
    public LocalDateTime getEc2EndDate() {
        return (LocalDateTime) get(9);
    }

    /**
     * Setter for <code>public.trainer_job.status</code>.
     */
    public void setStatus(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.trainer_job.status</code>.
     */
    public Integer getStatus() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>public.trainer_job.description</code>.
     */
    public void setDescription(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.trainer_job.description</code>.
     */
    public String getDescription() {
        return (String) get(11);
    }

    /**
     * Setter for <code>public.trainer_job.update_date</code>.
     */
    public void setUpdateDate(LocalDateTime value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.trainer_job.update_date</code>.
     */
    public LocalDateTime getUpdateDate() {
        return (LocalDateTime) get(12);
    }

    /**
     * Setter for <code>public.trainer_job.restarts</code>.
     */
    public void setRestarts(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.trainer_job.restarts</code>.
     */
    public Integer getRestarts() {
        return (Integer) get(13);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record14 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row14<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, Integer, String, LocalDateTime, Integer> fieldsRow() {
        return (Row14) super.fieldsRow();
    }

    @Override
    public Row14<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, Integer, String, LocalDateTime, Integer> valuesRow() {
        return (Row14) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TrainerJob.TRAINER_JOB.JOB_ID;
    }

    @Override
    public Field<String> field2() {
        return TrainerJob.TRAINER_JOB.SQS_URL;
    }

    @Override
    public Field<String> field3() {
        return TrainerJob.TRAINER_JOB.S3PATH;
    }

    @Override
    public Field<String> field4() {
        return TrainerJob.TRAINER_JOB.S3BUCKET;
    }

    @Override
    public Field<String> field5() {
        return TrainerJob.TRAINER_JOB.RECEIPTHANDLE;
    }

    @Override
    public Field<String> field6() {
        return TrainerJob.TRAINER_JOB.EC2_INSTANCE_TYPE;
    }

    @Override
    public Field<String> field7() {
        return TrainerJob.TRAINER_JOB.EC2_MAX_PRICE;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return TrainerJob.TRAINER_JOB.CREATE_DATE;
    }

    @Override
    public Field<LocalDateTime> field9() {
        return TrainerJob.TRAINER_JOB.EC2_CREATE_DATE;
    }

    @Override
    public Field<LocalDateTime> field10() {
        return TrainerJob.TRAINER_JOB.EC2_END_DATE;
    }

    @Override
    public Field<Integer> field11() {
        return TrainerJob.TRAINER_JOB.STATUS;
    }

    @Override
    public Field<String> field12() {
        return TrainerJob.TRAINER_JOB.DESCRIPTION;
    }

    @Override
    public Field<LocalDateTime> field13() {
        return TrainerJob.TRAINER_JOB.UPDATE_DATE;
    }

    @Override
    public Field<Integer> field14() {
        return TrainerJob.TRAINER_JOB.RESTARTS;
    }

    @Override
    public String component1() {
        return getJobId();
    }

    @Override
    public String component2() {
        return getSqsUrl();
    }

    @Override
    public String component3() {
        return getS3path();
    }

    @Override
    public String component4() {
        return getS3bucket();
    }

    @Override
    public String component5() {
        return getReceipthandle();
    }

    @Override
    public String component6() {
        return getEc2InstanceType();
    }

    @Override
    public String component7() {
        return getEc2MaxPrice();
    }

    @Override
    public LocalDateTime component8() {
        return getCreateDate();
    }

    @Override
    public LocalDateTime component9() {
        return getEc2CreateDate();
    }

    @Override
    public LocalDateTime component10() {
        return getEc2EndDate();
    }

    @Override
    public Integer component11() {
        return getStatus();
    }

    @Override
    public String component12() {
        return getDescription();
    }

    @Override
    public LocalDateTime component13() {
        return getUpdateDate();
    }

    @Override
    public Integer component14() {
        return getRestarts();
    }

    @Override
    public String value1() {
        return getJobId();
    }

    @Override
    public String value2() {
        return getSqsUrl();
    }

    @Override
    public String value3() {
        return getS3path();
    }

    @Override
    public String value4() {
        return getS3bucket();
    }

    @Override
    public String value5() {
        return getReceipthandle();
    }

    @Override
    public String value6() {
        return getEc2InstanceType();
    }

    @Override
    public String value7() {
        return getEc2MaxPrice();
    }

    @Override
    public LocalDateTime value8() {
        return getCreateDate();
    }

    @Override
    public LocalDateTime value9() {
        return getEc2CreateDate();
    }

    @Override
    public LocalDateTime value10() {
        return getEc2EndDate();
    }

    @Override
    public Integer value11() {
        return getStatus();
    }

    @Override
    public String value12() {
        return getDescription();
    }

    @Override
    public LocalDateTime value13() {
        return getUpdateDate();
    }

    @Override
    public Integer value14() {
        return getRestarts();
    }

    @Override
    public TrainerJobRecord value1(String value) {
        setJobId(value);
        return this;
    }

    @Override
    public TrainerJobRecord value2(String value) {
        setSqsUrl(value);
        return this;
    }

    @Override
    public TrainerJobRecord value3(String value) {
        setS3path(value);
        return this;
    }

    @Override
    public TrainerJobRecord value4(String value) {
        setS3bucket(value);
        return this;
    }

    @Override
    public TrainerJobRecord value5(String value) {
        setReceipthandle(value);
        return this;
    }

    @Override
    public TrainerJobRecord value6(String value) {
        setEc2InstanceType(value);
        return this;
    }

    @Override
    public TrainerJobRecord value7(String value) {
        setEc2MaxPrice(value);
        return this;
    }

    @Override
    public TrainerJobRecord value8(LocalDateTime value) {
        setCreateDate(value);
        return this;
    }

    @Override
    public TrainerJobRecord value9(LocalDateTime value) {
        setEc2CreateDate(value);
        return this;
    }

    @Override
    public TrainerJobRecord value10(LocalDateTime value) {
        setEc2EndDate(value);
        return this;
    }

    @Override
    public TrainerJobRecord value11(Integer value) {
        setStatus(value);
        return this;
    }

    @Override
    public TrainerJobRecord value12(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public TrainerJobRecord value13(LocalDateTime value) {
        setUpdateDate(value);
        return this;
    }

    @Override
    public TrainerJobRecord value14(Integer value) {
        setRestarts(value);
        return this;
    }

    @Override
    public TrainerJobRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, LocalDateTime value8, LocalDateTime value9, LocalDateTime value10, Integer value11, String value12, LocalDateTime value13, Integer value14) {
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
     * Create a detached TrainerJobRecord
     */
    public TrainerJobRecord() {
        super(TrainerJob.TRAINER_JOB);
    }

    /**
     * Create a detached, initialised TrainerJobRecord
     */
    public TrainerJobRecord(String jobId, String sqsUrl, String s3path, String s3bucket, String receipthandle, String ec2InstanceType, String ec2MaxPrice, LocalDateTime createDate, LocalDateTime ec2CreateDate, LocalDateTime ec2EndDate, Integer status, String description, LocalDateTime updateDate, Integer restarts) {
        super(TrainerJob.TRAINER_JOB);

        setJobId(jobId);
        setSqsUrl(sqsUrl);
        setS3path(s3path);
        setS3bucket(s3bucket);
        setReceipthandle(receipthandle);
        setEc2InstanceType(ec2InstanceType);
        setEc2MaxPrice(ec2MaxPrice);
        setCreateDate(createDate);
        setEc2CreateDate(ec2CreateDate);
        setEc2EndDate(ec2EndDate);
        setStatus(status);
        setDescription(description);
        setUpdateDate(updateDate);
        setRestarts(restarts);
    }
}
