/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables;


import io.skymind.pathmind.data.db.Indexes;
import io.skymind.pathmind.data.db.Keys;
import io.skymind.pathmind.data.db.Public;
import io.skymind.pathmind.data.db.tables.records.PathmindUserRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
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
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PathmindUser extends TableImpl<PathmindUserRecord> {

    private static final long serialVersionUID = -779923524;

    /**
     * The reference instance of <code>public.pathmind_user</code>
     */
    public static final PathmindUser PATHMIND_USER = new PathmindUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PathmindUserRecord> getRecordType() {
        return PathmindUserRecord.class;
    }

    /**
     * The column <code>public.pathmind_user.id</code>.
     */
    public final TableField<PathmindUserRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.pathmind_user.name</code>.
     */
    public final TableField<PathmindUserRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.pathmind_user.email</code>.
     */
    public final TableField<PathmindUserRecord, String> EMAIL = createField("email", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.pathmind_user.password</code>.
     */
    public final TableField<PathmindUserRecord, String> PASSWORD = createField("password", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.pathmind_user.account_type</code>.
     */
    public final TableField<PathmindUserRecord, Integer> ACCOUNT_TYPE = createField("account_type", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.pathmind_user.firstname</code>.
     */
    public final TableField<PathmindUserRecord, String> FIRSTNAME = createField("firstname", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.lastname</code>.
     */
    public final TableField<PathmindUserRecord, String> LASTNAME = createField("lastname", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.address</code>.
     */
    public final TableField<PathmindUserRecord, String> ADDRESS = createField("address", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.city</code>.
     */
    public final TableField<PathmindUserRecord, String> CITY = createField("city", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.state</code>.
     */
    public final TableField<PathmindUserRecord, String> STATE = createField("state", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.country</code>.
     */
    public final TableField<PathmindUserRecord, String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.zip</code>.
     */
    public final TableField<PathmindUserRecord, String> ZIP = createField("zip", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * Create a <code>public.pathmind_user</code> table reference
     */
    public PathmindUser() {
        this(DSL.name("pathmind_user"), null);
    }

    /**
     * Create an aliased <code>public.pathmind_user</code> table reference
     */
    public PathmindUser(String alias) {
        this(DSL.name(alias), PATHMIND_USER);
    }

    /**
     * Create an aliased <code>public.pathmind_user</code> table reference
     */
    public PathmindUser(Name alias) {
        this(alias, PATHMIND_USER);
    }

    private PathmindUser(Name alias, Table<PathmindUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private PathmindUser(Name alias, Table<PathmindUserRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> PathmindUser(Table<O> child, ForeignKey<O, PathmindUserRecord> key) {
        super(child, key, PATHMIND_USER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PATHMIND_USER_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PathmindUserRecord> getPrimaryKey() {
        return Keys.PATHMIND_USER_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PathmindUserRecord>> getKeys() {
        return Arrays.<UniqueKey<PathmindUserRecord>>asList(Keys.PATHMIND_USER_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PathmindUser as(String alias) {
        return new PathmindUser(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PathmindUser as(Name alias) {
        return new PathmindUser(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PathmindUser rename(String name) {
        return new PathmindUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PathmindUser rename(Name name) {
        return new PathmindUser(name, null);
    }
}
