/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.SimulationParameterRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SimulationParameter extends TableImpl<SimulationParameterRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.simulation_parameter</code>
     */
    public static final SimulationParameter SIMULATION_PARAMETER = new SimulationParameter();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SimulationParameterRecord> getRecordType() {
        return SimulationParameterRecord.class;
    }

    /**
     * The column <code>public.simulation_parameter.model_id</code>.
     */
    public final TableField<SimulationParameterRecord, Long> MODEL_ID = createField(DSL.name("model_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.simulation_parameter.experiment_id</code>.
     */
    public final TableField<SimulationParameterRecord, Long> EXPERIMENT_ID = createField(DSL.name("experiment_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.simulation_parameter.index</code>.
     */
    public final TableField<SimulationParameterRecord, Integer> INDEX = createField(DSL.name("index"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.simulation_parameter.key</code>.
     */
    public final TableField<SimulationParameterRecord, String> KEY = createField(DSL.name("key"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.simulation_parameter.value</code>.
     */
    public final TableField<SimulationParameterRecord, String> VALUE = createField(DSL.name("value"), SQLDataType.VARCHAR(1024).nullable(false), this, "");

    /**
     * The column <code>public.simulation_parameter.type</code>.
     */
    public final TableField<SimulationParameterRecord, Integer> TYPE = createField(DSL.name("type"), SQLDataType.INTEGER.nullable(false), this, "");

    private SimulationParameter(Name alias, Table<SimulationParameterRecord> aliased) {
        this(alias, aliased, null);
    }

    private SimulationParameter(Name alias, Table<SimulationParameterRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.simulation_parameter</code> table reference
     */
    public SimulationParameter(String alias) {
        this(DSL.name(alias), SIMULATION_PARAMETER);
    }

    /**
     * Create an aliased <code>public.simulation_parameter</code> table reference
     */
    public SimulationParameter(Name alias) {
        this(alias, SIMULATION_PARAMETER);
    }

    /**
     * Create a <code>public.simulation_parameter</code> table reference
     */
    public SimulationParameter() {
        this(DSL.name("simulation_parameter"), null);
    }

    public <O extends Record> SimulationParameter(Table<O> child, ForeignKey<O, SimulationParameterRecord> key) {
        super(child, key, SIMULATION_PARAMETER);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SIMULATION_PARAMETER_MODEL_ID_EXP_ID_INDEX);
    }

    @Override
    public List<ForeignKey<SimulationParameterRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SimulationParameterRecord, ?>>asList(Keys.SIMULATION_PARAMETER__FK_SIMULATION_PARAMETER_MODEL, Keys.SIMULATION_PARAMETER__FK_SIMULATION_PARAMETER_EXPERIMENT);
    }

    private transient Model _model;
    private transient Experiment _experiment;

    public Model model() {
        if (_model == null)
            _model = new Model(this, Keys.SIMULATION_PARAMETER__FK_SIMULATION_PARAMETER_MODEL);

        return _model;
    }

    public Experiment experiment() {
        if (_experiment == null)
            _experiment = new Experiment(this, Keys.SIMULATION_PARAMETER__FK_SIMULATION_PARAMETER_EXPERIMENT);

        return _experiment;
    }

    @Override
    public SimulationParameter as(String alias) {
        return new SimulationParameter(DSL.name(alias), this);
    }

    @Override
    public SimulationParameter as(Name alias) {
        return new SimulationParameter(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SimulationParameter rename(String name) {
        return new SimulationParameter(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SimulationParameter rename(Name name) {
        return new SimulationParameter(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, Integer, String, String, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
