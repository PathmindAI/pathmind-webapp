/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables;


import io.skymind.pathmind.data.db.Indexes;
import io.skymind.pathmind.data.db.Keys;
import io.skymind.pathmind.data.db.Public;
import io.skymind.pathmind.data.db.tables.records.PolicyRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row12;
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
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Policy extends TableImpl<PolicyRecord> {

    private static final long serialVersionUID = -1371520121;

    /**
     * The reference instance of <code>public.policy</code>
     */
    public static final Policy POLICY = new Policy();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PolicyRecord> getRecordType() {
        return PolicyRecord.class;
    }

    /**
     * The column <code>public.policy.id</code>.
     */
    public final TableField<PolicyRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.policy.run_id</code>.
     */
    public final TableField<PolicyRecord, Long> RUN_ID = createField(DSL.name("run_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.policy.external_id</code>.
     */
    public final TableField<PolicyRecord, String> EXTERNAL_ID = createField(DSL.name("external_id"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>public.policy.name</code>.
     */
    public final TableField<PolicyRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.policy.progress</code>.
     */
    public final TableField<PolicyRecord, JSONB> PROGRESS = createField(DSL.name("progress"), org.jooq.impl.SQLDataType.JSONB, this, "");

    /**
     * The column <code>public.policy.file</code>.
     */
    public final TableField<PolicyRecord, byte[]> FILE = createField(DSL.name("file"), org.jooq.impl.SQLDataType.BLOB, this, "");

    /**
     * The column <code>public.policy.startedat</code>.
     */
    public final TableField<PolicyRecord, LocalDateTime> STARTEDAT = createField(DSL.name("startedat"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.policy.stoppedat</code>.
     */
    public final TableField<PolicyRecord, LocalDateTime> STOPPEDAT = createField(DSL.name("stoppedat"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.policy.algorithm</code>.
     */
    public final TableField<PolicyRecord, String> ALGORITHM = createField(DSL.name("algorithm"), org.jooq.impl.SQLDataType.VARCHAR(3), this, "");

    /**
     * The column <code>public.policy.learning_rate</code>.
     */
    public final TableField<PolicyRecord, Double> LEARNING_RATE = createField(DSL.name("learning_rate"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.policy.gamma</code>.
     */
    public final TableField<PolicyRecord, Double> GAMMA = createField(DSL.name("gamma"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.policy.batch_size</code>.
     */
    public final TableField<PolicyRecord, Integer> BATCH_SIZE = createField(DSL.name("batch_size"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>public.policy</code> table reference
     */
    public Policy() {
        this(DSL.name("policy"), null);
    }

    /**
     * Create an aliased <code>public.policy</code> table reference
     */
    public Policy(String alias) {
        this(DSL.name(alias), POLICY);
    }

    /**
     * Create an aliased <code>public.policy</code> table reference
     */
    public Policy(Name alias) {
        this(alias, POLICY);
    }

    private Policy(Name alias, Table<PolicyRecord> aliased) {
        this(alias, aliased, null);
    }

    private Policy(Name alias, Table<PolicyRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Policy(Table<O> child, ForeignKey<O, PolicyRecord> key) {
        super(child, key, POLICY);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.POLICY_PKEY, Indexes.POLICY_RUN_ID_EXTERNAL_ID_KEY);
    }

    @Override
    public UniqueKey<PolicyRecord> getPrimaryKey() {
        return Keys.POLICY_PKEY;
    }

    @Override
    public List<UniqueKey<PolicyRecord>> getKeys() {
        return Arrays.<UniqueKey<PolicyRecord>>asList(Keys.POLICY_PKEY, Keys.POLICY_RUN_ID_EXTERNAL_ID_KEY);
    }

    @Override
    public List<ForeignKey<PolicyRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PolicyRecord, ?>>asList(Keys.POLICY__PM_FK_POLICY_RUN);
    }

    public Run run() {
        return new Run(this, Keys.POLICY__PM_FK_POLICY_RUN);
    }

    @Override
    public Policy as(String alias) {
        return new Policy(DSL.name(alias), this);
    }

    @Override
    public Policy as(Name alias) {
        return new Policy(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Policy rename(String name) {
        return new Policy(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Policy rename(Name name) {
        return new Policy(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<Long, Long, String, String, JSONB, byte[], LocalDateTime, LocalDateTime, String, Double, Double, Integer> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}
