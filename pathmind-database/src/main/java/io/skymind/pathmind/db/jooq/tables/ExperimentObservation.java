/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.ExperimentObservationRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ExperimentObservation extends TableImpl<ExperimentObservationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.experiment_observation</code>
     */
    public static final ExperimentObservation EXPERIMENT_OBSERVATION = new ExperimentObservation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ExperimentObservationRecord> getRecordType() {
        return ExperimentObservationRecord.class;
    }

    /**
     * The column <code>public.experiment_observation.experiment_id</code>.
     */
    public final TableField<ExperimentObservationRecord, Long> EXPERIMENT_ID = createField(DSL.name("experiment_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.experiment_observation.observation_id</code>.
     */
    public final TableField<ExperimentObservationRecord, Long> OBSERVATION_ID = createField(DSL.name("observation_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private ExperimentObservation(Name alias, Table<ExperimentObservationRecord> aliased) {
        this(alias, aliased, null);
    }

    private ExperimentObservation(Name alias, Table<ExperimentObservationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.experiment_observation</code> table reference
     */
    public ExperimentObservation(String alias) {
        this(DSL.name(alias), EXPERIMENT_OBSERVATION);
    }

    /**
     * Create an aliased <code>public.experiment_observation</code> table reference
     */
    public ExperimentObservation(Name alias) {
        this(alias, EXPERIMENT_OBSERVATION);
    }

    /**
     * Create a <code>public.experiment_observation</code> table reference
     */
    public ExperimentObservation() {
        this(DSL.name("experiment_observation"), null);
    }

    public <O extends Record> ExperimentObservation(Table<O> child, ForeignKey<O, ExperimentObservationRecord> key) {
        super(child, key, EXPERIMENT_OBSERVATION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EXPERIMENT_OBSERVATION_EXPERIMENT_ID_INDEX, Indexes.EXPERIMENT_OBSERVATION_OBSERVATION_ID_INDEX);
    }

    @Override
    public UniqueKey<ExperimentObservationRecord> getPrimaryKey() {
        return Keys.PK_EXPERIMENT_OBSERVATION;
    }

    @Override
    public List<UniqueKey<ExperimentObservationRecord>> getKeys() {
        return Arrays.<UniqueKey<ExperimentObservationRecord>>asList(Keys.PK_EXPERIMENT_OBSERVATION);
    }

    @Override
    public List<ForeignKey<ExperimentObservationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ExperimentObservationRecord, ?>>asList(Keys.EXPERIMENT_OBSERVATION__PM_FK_EO_EXPERIMENT, Keys.EXPERIMENT_OBSERVATION__PM_FK_EO_OBSERVATION);
    }

    private transient Experiment _experiment;
    private transient Observation _observation;

    public Experiment experiment() {
        if (_experiment == null)
            _experiment = new Experiment(this, Keys.EXPERIMENT_OBSERVATION__PM_FK_EO_EXPERIMENT);

        return _experiment;
    }

    public Observation observation() {
        if (_observation == null)
            _observation = new Observation(this, Keys.EXPERIMENT_OBSERVATION__PM_FK_EO_OBSERVATION);

        return _observation;
    }

    @Override
    public ExperimentObservation as(String alias) {
        return new ExperimentObservation(DSL.name(alias), this);
    }

    @Override
    public ExperimentObservation as(Name alias) {
        return new ExperimentObservation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ExperimentObservation rename(String name) {
        return new ExperimentObservation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ExperimentObservation rename(Name name) {
        return new ExperimentObservation(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
