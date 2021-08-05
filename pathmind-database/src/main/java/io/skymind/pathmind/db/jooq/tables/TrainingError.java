/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import java.util.Arrays;
import java.util.List;

import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.TrainingErrorRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TrainingError extends TableImpl<TrainingErrorRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.training_error</code>
     */
    public static final TrainingError TRAINING_ERROR = new TrainingError();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TrainingErrorRecord> getRecordType() {
        return TrainingErrorRecord.class;
    }

    /**
     * The column <code>public.training_error.id</code>.
     */
    public final TableField<TrainingErrorRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.training_error.keyword</code>.
     */
    public final TableField<TrainingErrorRecord, String> KEYWORD = createField(DSL.name("keyword"), SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>public.training_error.description</code>.
     */
    public final TableField<TrainingErrorRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>public.training_error.restartable</code>.
     */
    public final TableField<TrainingErrorRecord, Boolean> RESTARTABLE = createField(DSL.name("restartable"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("false", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.training_error.support_article</code>.
     */
    public final TableField<TrainingErrorRecord, String> SUPPORT_ARTICLE = createField(DSL.name("support_article"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.training_error.target_file</code>.
     */
    public final TableField<TrainingErrorRecord, String> TARGET_FILE = createField(DSL.name("target_file"), SQLDataType.VARCHAR(255).defaultValue(DSL.field("'process_output.log'::character varying", SQLDataType.VARCHAR)), this, "");

    private TrainingError(Name alias, Table<TrainingErrorRecord> aliased) {
        this(alias, aliased, null);
    }

    private TrainingError(Name alias, Table<TrainingErrorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.training_error</code> table reference
     */
    public TrainingError(String alias) {
        this(DSL.name(alias), TRAINING_ERROR);
    }

    /**
     * Create an aliased <code>public.training_error</code> table reference
     */
    public TrainingError(Name alias) {
        this(alias, TRAINING_ERROR);
    }

    /**
     * Create a <code>public.training_error</code> table reference
     */
    public TrainingError() {
        this(DSL.name("training_error"), null);
    }

    public <O extends Record> TrainingError(Table<O> child, ForeignKey<O, TrainingErrorRecord> key) {
        super(child, key, TRAINING_ERROR);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<TrainingErrorRecord, Long> getIdentity() {
        return (Identity<TrainingErrorRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TrainingErrorRecord> getPrimaryKey() {
        return Keys.TRAINING_ERROR_PKEY;
    }

    @Override
    public List<UniqueKey<TrainingErrorRecord>> getKeys() {
        return Arrays.<UniqueKey<TrainingErrorRecord>>asList(Keys.TRAINING_ERROR_PKEY);
    }

    @Override
    public TrainingError as(String alias) {
        return new TrainingError(DSL.name(alias), this);
    }

    @Override
    public TrainingError as(Name alias) {
        return new TrainingError(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingError rename(String name) {
        return new TrainingError(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingError rename(Name name) {
        return new TrainingError(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, String, String, Boolean, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
