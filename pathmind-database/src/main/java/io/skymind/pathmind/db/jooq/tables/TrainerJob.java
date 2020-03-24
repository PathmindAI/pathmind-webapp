/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.TrainerJobRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row13;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class TrainerJob extends TableImpl<TrainerJobRecord> {

    private static final long serialVersionUID = -1084242373;

    /**
     * The reference instance of <code>public.trainer_job</code>
     */
    public static final TrainerJob TRAINER_JOB = new TrainerJob();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TrainerJobRecord> getRecordType() {
        return TrainerJobRecord.class;
    }

    /**
     * The column <code>public.trainer_job.job_id</code>.
     */
    public final TableField<TrainerJobRecord, String> JOB_ID = createField(DSL.name("job_id"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.sqs_url</code>.
     */
    public final TableField<TrainerJobRecord, String> SQS_URL = createField(DSL.name("sqs_url"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.s3path</code>.
     */
    public final TableField<TrainerJobRecord, String> S3PATH = createField(DSL.name("s3path"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.s3bucket</code>.
     */
    public final TableField<TrainerJobRecord, String> S3BUCKET = createField(DSL.name("s3bucket"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.receipthandle</code>.
     */
    public final TableField<TrainerJobRecord, String> RECEIPTHANDLE = createField(DSL.name("receipthandle"), org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.ec2_instance_type</code>.
     */
    public final TableField<TrainerJobRecord, String> EC2_INSTANCE_TYPE = createField(DSL.name("ec2_instance_type"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.ec2_max_price</code>.
     */
    public final TableField<TrainerJobRecord, String> EC2_MAX_PRICE = createField(DSL.name("ec2_max_price"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.create_date</code>.
     */
    public final TableField<TrainerJobRecord, LocalDateTime> CREATE_DATE = createField(DSL.name("create_date"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.trainer_job.ec2_create_date</code>.
     */
    public final TableField<TrainerJobRecord, LocalDateTime> EC2_CREATE_DATE = createField(DSL.name("ec2_create_date"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.trainer_job.ec2_end_date</code>.
     */
    public final TableField<TrainerJobRecord, LocalDateTime> EC2_END_DATE = createField(DSL.name("ec2_end_date"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.trainer_job.status</code>.
     */
    public final TableField<TrainerJobRecord, Integer> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.description</code>.
     */
    public final TableField<TrainerJobRecord, String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.trainer_job.update_date</code>.
     */
    public final TableField<TrainerJobRecord, LocalDateTime> UPDATE_DATE = createField(DSL.name("update_date"), org.jooq.impl.SQLDataType.LOCALDATETIME.defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * Create a <code>public.trainer_job</code> table reference
     */
    public TrainerJob() {
        this(DSL.name("trainer_job"), null);
    }

    /**
     * Create an aliased <code>public.trainer_job</code> table reference
     */
    public TrainerJob(String alias) {
        this(DSL.name(alias), TRAINER_JOB);
    }

    /**
     * Create an aliased <code>public.trainer_job</code> table reference
     */
    public TrainerJob(Name alias) {
        this(alias, TRAINER_JOB);
    }

    private TrainerJob(Name alias, Table<TrainerJobRecord> aliased) {
        this(alias, aliased, null);
    }

    private TrainerJob(Name alias, Table<TrainerJobRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TrainerJob(Table<O> child, ForeignKey<O, TrainerJobRecord> key) {
        super(child, key, TRAINER_JOB);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TRAINER_JOB_PKEY);
    }

    @Override
    public UniqueKey<TrainerJobRecord> getPrimaryKey() {
        return Keys.TRAINER_JOB_PKEY;
    }

    @Override
    public List<UniqueKey<TrainerJobRecord>> getKeys() {
        return Arrays.<UniqueKey<TrainerJobRecord>>asList(Keys.TRAINER_JOB_PKEY);
    }

    @Override
    public TrainerJob as(String alias) {
        return new TrainerJob(DSL.name(alias), this);
    }

    @Override
    public TrainerJob as(Name alias) {
        return new TrainerJob(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainerJob rename(String name) {
        return new TrainerJob(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainerJob rename(Name name) {
        return new TrainerJob(name, null);
    }

    // -------------------------------------------------------------------------
    // Row13 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row13<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, Integer, String, LocalDateTime> fieldsRow() {
        return (Row13) super.fieldsRow();
    }
}
