/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.SimulationParameter;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SimulationParameterRecord extends TableRecordImpl<SimulationParameterRecord> implements Record6<Long, Long, Integer, String, String, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.simulation_parameter.model_id</code>.
     */
    public void setModelId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.simulation_parameter.model_id</code>.
     */
    public Long getModelId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.simulation_parameter.experiment_id</code>.
     */
    public void setExperimentId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.simulation_parameter.experiment_id</code>.
     */
    public Long getExperimentId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.simulation_parameter.index</code>.
     */
    public void setIndex(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.simulation_parameter.index</code>.
     */
    public Integer getIndex() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.simulation_parameter.key</code>.
     */
    public void setKey(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.simulation_parameter.key</code>.
     */
    public String getKey() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.simulation_parameter.value</code>.
     */
    public void setValue(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.simulation_parameter.value</code>.
     */
    public String getValue() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.simulation_parameter.type</code>.
     */
    public void setType(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.simulation_parameter.type</code>.
     */
    public Integer getType() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, Integer, String, String, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Long, Long, Integer, String, String, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return SimulationParameter.SIMULATION_PARAMETER.MODEL_ID;
    }

    @Override
    public Field<Long> field2() {
        return SimulationParameter.SIMULATION_PARAMETER.EXPERIMENT_ID;
    }

    @Override
    public Field<Integer> field3() {
        return SimulationParameter.SIMULATION_PARAMETER.INDEX;
    }

    @Override
    public Field<String> field4() {
        return SimulationParameter.SIMULATION_PARAMETER.KEY;
    }

    @Override
    public Field<String> field5() {
        return SimulationParameter.SIMULATION_PARAMETER.VALUE;
    }

    @Override
    public Field<Integer> field6() {
        return SimulationParameter.SIMULATION_PARAMETER.TYPE;
    }

    @Override
    public Long component1() {
        return getModelId();
    }

    @Override
    public Long component2() {
        return getExperimentId();
    }

    @Override
    public Integer component3() {
        return getIndex();
    }

    @Override
    public String component4() {
        return getKey();
    }

    @Override
    public String component5() {
        return getValue();
    }

    @Override
    public Integer component6() {
        return getType();
    }

    @Override
    public Long value1() {
        return getModelId();
    }

    @Override
    public Long value2() {
        return getExperimentId();
    }

    @Override
    public Integer value3() {
        return getIndex();
    }

    @Override
    public String value4() {
        return getKey();
    }

    @Override
    public String value5() {
        return getValue();
    }

    @Override
    public Integer value6() {
        return getType();
    }

    @Override
    public SimulationParameterRecord value1(Long value) {
        setModelId(value);
        return this;
    }

    @Override
    public SimulationParameterRecord value2(Long value) {
        setExperimentId(value);
        return this;
    }

    @Override
    public SimulationParameterRecord value3(Integer value) {
        setIndex(value);
        return this;
    }

    @Override
    public SimulationParameterRecord value4(String value) {
        setKey(value);
        return this;
    }

    @Override
    public SimulationParameterRecord value5(String value) {
        setValue(value);
        return this;
    }

    @Override
    public SimulationParameterRecord value6(Integer value) {
        setType(value);
        return this;
    }

    @Override
    public SimulationParameterRecord values(Long value1, Long value2, Integer value3, String value4, String value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SimulationParameterRecord
     */
    public SimulationParameterRecord() {
        super(SimulationParameter.SIMULATION_PARAMETER);
    }

    /**
     * Create a detached, initialised SimulationParameterRecord
     */
    public SimulationParameterRecord(Long modelId, Long experimentId, Integer index, String key, String value, Integer type) {
        super(SimulationParameter.SIMULATION_PARAMETER);

        setModelId(modelId);
        setExperimentId(experimentId);
        setIndex(index);
        setKey(key);
        setValue(value);
        setType(type);
    }
}
