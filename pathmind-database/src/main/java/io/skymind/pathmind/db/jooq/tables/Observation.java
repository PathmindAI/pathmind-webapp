/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.ObservationRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row11;
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
public class Observation extends TableImpl<ObservationRecord> {

    private static final long serialVersionUID = 777311048;

    /**
     * The reference instance of <code>public.observation</code>
     */
    public static final Observation OBSERVATION = new Observation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ObservationRecord> getRecordType() {
        return ObservationRecord.class;
    }

    /**
     * The column <code>public.observation.id</code>.
     */
    public final TableField<ObservationRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('observation_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.observation.model_id</code>.
     */
    public final TableField<ObservationRecord, Long> MODEL_ID = createField(DSL.name("model_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.observation.array_index</code>.
     */
    public final TableField<ObservationRecord, Integer> ARRAY_INDEX = createField(DSL.name("array_index"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.observation.variable</code>.
     */
    public final TableField<ObservationRecord, String> VARIABLE = createField(DSL.name("variable"), org.jooq.impl.SQLDataType.VARCHAR(16).nullable(false), this, "");

    /**
     * The column <code>public.observation.data_type</code>.
     */
    public final TableField<ObservationRecord, String> DATA_TYPE = createField(DSL.name("data_type"), org.jooq.impl.SQLDataType.VARCHAR(16).nullable(false), this, "");

    /**
     * The column <code>public.observation.description</code>.
     */
    public final TableField<ObservationRecord, String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.observation.example</code>.
     */
    public final TableField<ObservationRecord, String> EXAMPLE = createField(DSL.name("example"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.observation.min</code>.
     */
    public final TableField<ObservationRecord, Double> MIN = createField(DSL.name("min"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.observation.max</code>.
     */
    public final TableField<ObservationRecord, Double> MAX = createField(DSL.name("max"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.observation.min_items</code>.
     */
    public final TableField<ObservationRecord, Double> MIN_ITEMS = createField(DSL.name("min_items"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.observation.max_items</code>.
     */
    public final TableField<ObservationRecord, Double> MAX_ITEMS = createField(DSL.name("max_items"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * Create a <code>public.observation</code> table reference
     */
    public Observation() {
        this(DSL.name("observation"), null);
    }

    /**
     * Create an aliased <code>public.observation</code> table reference
     */
    public Observation(String alias) {
        this(DSL.name(alias), OBSERVATION);
    }

    /**
     * Create an aliased <code>public.observation</code> table reference
     */
    public Observation(Name alias) {
        this(alias, OBSERVATION);
    }

    private Observation(Name alias, Table<ObservationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Observation(Name alias, Table<ObservationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Observation(Table<O> child, ForeignKey<O, ObservationRecord> key) {
        super(child, key, OBSERVATION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.OBSERVATION_MODEL_FK_INDEX, Indexes.OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY, Indexes.OBSERVATION_PKEY);
    }

    @Override
    public Identity<ObservationRecord, Long> getIdentity() {
        return Keys.IDENTITY_OBSERVATION;
    }

    @Override
    public UniqueKey<ObservationRecord> getPrimaryKey() {
        return Keys.OBSERVATION_PKEY;
    }

    @Override
    public List<UniqueKey<ObservationRecord>> getKeys() {
        return Arrays.<UniqueKey<ObservationRecord>>asList(Keys.OBSERVATION_PKEY, Keys.OBSERVATION_MODEL_ID_ARRAY_INDEX_KEY);
    }

    @Override
    public List<ForeignKey<ObservationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ObservationRecord, ?>>asList(Keys.OBSERVATION__PM_FK_OBSERVATION_MODEL);
    }

    public Model model() {
        return new Model(this, Keys.OBSERVATION__PM_FK_OBSERVATION_MODEL);
    }

    @Override
    public Observation as(String alias) {
        return new Observation(DSL.name(alias), this);
    }

    @Override
    public Observation as(Name alias) {
        return new Observation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Observation rename(String name) {
        return new Observation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Observation rename(Name name) {
        return new Observation(name, null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<Long, Long, Integer, String, String, String, String, Double, Double, Double, Double> fieldsRow() {
        return (Row11) super.fieldsRow();
    }
}
