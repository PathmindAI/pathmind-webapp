/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.PolicyRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
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
public class Policy extends TableImpl<PolicyRecord> {

    private static final long serialVersionUID = -946787170;

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
    public final TableField<PolicyRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('policy_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

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
     * The column <code>public.policy.started_at</code>.
     */
    public final TableField<PolicyRecord, LocalDateTime> STARTED_AT = createField(DSL.name("started_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.policy.stopped_at</code>.
     */
    public final TableField<PolicyRecord, LocalDateTime> STOPPED_AT = createField(DSL.name("stopped_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.policy.exported_at</code>.
     */
    public final TableField<PolicyRecord, LocalDateTime> EXPORTED_AT = createField(DSL.name("exported_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.policy.has_file</code>.
     */
    public final TableField<PolicyRecord, Boolean> HAS_FILE = createField(DSL.name("has_file"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.policy.is_valid</code>.
     */
    public final TableField<PolicyRecord, Boolean> IS_VALID = createField(DSL.name("is_valid"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

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
        return Arrays.<Index>asList(Indexes.POLICY_PKEY, Indexes.POLICY_RUN_FK_INDEX, Indexes.POLICY_RUN_ID_EXTERNAL_ID_KEY);
    }

    @Override
    public Identity<PolicyRecord, Long> getIdentity() {
        return Keys.IDENTITY_POLICY;
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
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, Long, String, String, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Boolean> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
