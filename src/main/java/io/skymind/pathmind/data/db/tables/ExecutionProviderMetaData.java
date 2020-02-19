/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables;


import io.skymind.pathmind.data.db.Indexes;
import io.skymind.pathmind.data.db.Keys;
import io.skymind.pathmind.data.db.Public;
import io.skymind.pathmind.data.db.tables.records.ExecutionProviderMetaDataRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
public class ExecutionProviderMetaData extends TableImpl<ExecutionProviderMetaDataRecord> {

    private static final long serialVersionUID = 1466524894;

    /**
     * The reference instance of <code>public.execution_provider_meta_data</code>
     */
    public static final ExecutionProviderMetaData EXECUTION_PROVIDER_META_DATA = new ExecutionProviderMetaData();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ExecutionProviderMetaDataRecord> getRecordType() {
        return ExecutionProviderMetaDataRecord.class;
    }

    /**
     * The column <code>public.execution_provider_meta_data.id</code>.
     */
    public final TableField<ExecutionProviderMetaDataRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.execution_provider_meta_data.provider_class</code>.
     */
    public final TableField<ExecutionProviderMetaDataRecord, Integer> PROVIDER_CLASS = createField(DSL.name("provider_class"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("1", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.execution_provider_meta_data.type</code>.
     */
    public final TableField<ExecutionProviderMetaDataRecord, Integer> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.execution_provider_meta_data.key</code>.
     */
    public final TableField<ExecutionProviderMetaDataRecord, String> KEY = createField(DSL.name("key"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>public.execution_provider_meta_data.value</code>.
     */
    public final TableField<ExecutionProviderMetaDataRecord, String> VALUE = createField(DSL.name("value"), org.jooq.impl.SQLDataType.VARCHAR(36), this, "");

    /**
     * Create a <code>public.execution_provider_meta_data</code> table reference
     */
    public ExecutionProviderMetaData() {
        this(DSL.name("execution_provider_meta_data"), null);
    }

    /**
     * Create an aliased <code>public.execution_provider_meta_data</code> table reference
     */
    public ExecutionProviderMetaData(String alias) {
        this(DSL.name(alias), EXECUTION_PROVIDER_META_DATA);
    }

    /**
     * Create an aliased <code>public.execution_provider_meta_data</code> table reference
     */
    public ExecutionProviderMetaData(Name alias) {
        this(alias, EXECUTION_PROVIDER_META_DATA);
    }

    private ExecutionProviderMetaData(Name alias, Table<ExecutionProviderMetaDataRecord> aliased) {
        this(alias, aliased, null);
    }

    private ExecutionProviderMetaData(Name alias, Table<ExecutionProviderMetaDataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> ExecutionProviderMetaData(Table<O> child, ForeignKey<O, ExecutionProviderMetaDataRecord> key) {
        super(child, key, EXECUTION_PROVIDER_META_DATA);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EXECUTION_PROVIDER_META_DATA_PKEY, Indexes.UNIQUE_PROVIDER_CLASS_TYPE_KEY);
    }

    @Override
    public UniqueKey<ExecutionProviderMetaDataRecord> getPrimaryKey() {
        return Keys.EXECUTION_PROVIDER_META_DATA_PKEY;
    }

    @Override
    public List<UniqueKey<ExecutionProviderMetaDataRecord>> getKeys() {
        return Arrays.<UniqueKey<ExecutionProviderMetaDataRecord>>asList(Keys.EXECUTION_PROVIDER_META_DATA_PKEY, Keys.UNIQUE_PROVIDER_CLASS_TYPE_KEY);
    }

    @Override
    public ExecutionProviderMetaData as(String alias) {
        return new ExecutionProviderMetaData(DSL.name(alias), this);
    }

    @Override
    public ExecutionProviderMetaData as(Name alias) {
        return new ExecutionProviderMetaData(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ExecutionProviderMetaData rename(String name) {
        return new ExecutionProviderMetaData(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ExecutionProviderMetaData rename(Name name) {
        return new ExecutionProviderMetaData(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Integer, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
