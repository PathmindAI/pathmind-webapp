/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.PathmindUserRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row18;
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
public class PathmindUser extends TableImpl<PathmindUserRecord> {

    private static final long serialVersionUID = 1576754698;

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
    public final TableField<PathmindUserRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('pathmind_user_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.pathmind_user.email</code>.
     */
    public final TableField<PathmindUserRecord, String> EMAIL = createField(DSL.name("email"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.pathmind_user.password</code>.
     */
    public final TableField<PathmindUserRecord, String> PASSWORD = createField(DSL.name("password"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.pathmind_user.account_type</code>.
     */
    public final TableField<PathmindUserRecord, Integer> ACCOUNT_TYPE = createField(DSL.name("account_type"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.pathmind_user.firstname</code>.
     */
    public final TableField<PathmindUserRecord, String> FIRSTNAME = createField(DSL.name("firstname"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.lastname</code>.
     */
    public final TableField<PathmindUserRecord, String> LASTNAME = createField(DSL.name("lastname"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.address</code>.
     */
    public final TableField<PathmindUserRecord, String> ADDRESS = createField(DSL.name("address"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.city</code>.
     */
    public final TableField<PathmindUserRecord, String> CITY = createField(DSL.name("city"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.state</code>.
     */
    public final TableField<PathmindUserRecord, String> STATE = createField(DSL.name("state"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.country</code>.
     */
    public final TableField<PathmindUserRecord, String> COUNTRY = createField(DSL.name("country"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.zip</code>.
     */
    public final TableField<PathmindUserRecord, String> ZIP = createField(DSL.name("zip"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.pathmind_user.delete_at</code>.
     */
    public final TableField<PathmindUserRecord, LocalDateTime> DELETE_AT = createField(DSL.name("delete_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.pathmind_user.email_verified_at</code>.
     */
    public final TableField<PathmindUserRecord, LocalDateTime> EMAIL_VERIFIED_AT = createField(DSL.name("email_verified_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.pathmind_user.email_verification_token</code>.
     */
    public final TableField<PathmindUserRecord, UUID> EMAIL_VERIFICATION_TOKEN = createField(DSL.name("email_verification_token"), org.jooq.impl.SQLDataType.UUID, this, "");

    /**
     * The column <code>public.pathmind_user.password_reset_send_at</code>.
     */
    public final TableField<PathmindUserRecord, LocalDateTime> PASSWORD_RESET_SEND_AT = createField(DSL.name("password_reset_send_at"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.pathmind_user.stripe_customer_id</code>.
     */
    public final TableField<PathmindUserRecord, String> STRIPE_CUSTOMER_ID = createField(DSL.name("stripe_customer_id"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.pathmind_user.new_email_to_verify</code>.
     */
    public final TableField<PathmindUserRecord, String> NEW_EMAIL_TO_VERIFY = createField(DSL.name("new_email_to_verify"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.pathmind_user.api_key</code>.
     */
    public final TableField<PathmindUserRecord, String> API_KEY = createField(DSL.name("api_key"), org.jooq.impl.SQLDataType.CLOB.nullable(false).defaultValue(org.jooq.impl.DSL.field("uuid_generate_v4()", org.jooq.impl.SQLDataType.CLOB)), this, "");

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

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PATHMIND_USER_API_KEY_KEY, Indexes.PATHMIND_USER_EMAIL_KEY, Indexes.PATHMIND_USER_PKEY);
    }

    @Override
    public Identity<PathmindUserRecord, Long> getIdentity() {
        return Keys.IDENTITY_PATHMIND_USER;
    }

    @Override
    public UniqueKey<PathmindUserRecord> getPrimaryKey() {
        return Keys.PATHMIND_USER_PKEY;
    }

    @Override
    public List<UniqueKey<PathmindUserRecord>> getKeys() {
        return Arrays.<UniqueKey<PathmindUserRecord>>asList(Keys.PATHMIND_USER_PKEY, Keys.PATHMIND_USER_EMAIL_KEY, Keys.PATHMIND_USER_API_KEY_KEY);
    }

    @Override
    public PathmindUser as(String alias) {
        return new PathmindUser(DSL.name(alias), this);
    }

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

    // -------------------------------------------------------------------------
    // Row18 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row18<Long, String, String, Integer, String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, UUID, LocalDateTime, String, String, String> fieldsRow() {
        return (Row18) super.fieldsRow();
    }
}
