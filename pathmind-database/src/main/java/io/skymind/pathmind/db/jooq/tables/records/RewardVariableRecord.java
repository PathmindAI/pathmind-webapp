/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import javax.annotation.processing.Generated;

import io.skymind.pathmind.db.jooq.tables.RewardVariable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


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
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class RewardVariableRecord extends UpdatableRecordImpl<RewardVariableRecord> implements Record7<Long, Long, String, Integer, String, String, Double> {

    private static final long serialVersionUID = 1432271025;

    /**
     * Setter for <code>public.reward_variable.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.reward_variable.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.reward_variable.model_id</code>.
     */
    public void setModelId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.reward_variable.model_id</code>.
     */
    public Long getModelId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.reward_variable.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.reward_variable.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.reward_variable.array_index</code>.
     */
    public void setArrayIndex(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.reward_variable.array_index</code>.
     */
    public Integer getArrayIndex() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.reward_variable.data_type</code>.
     */
    public void setDataType(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.reward_variable.data_type</code>.
     */
    public String getDataType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.reward_variable.goal_condition_type</code>.
     */
    public void setGoalConditionType(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.reward_variable.goal_condition_type</code>.
     */
    public String getGoalConditionType() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.reward_variable.goal_value</code>.
     */
    public void setGoalValue(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.reward_variable.goal_value</code>.
     */
    public Double getGoalValue() {
        return (Double) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, String, Integer, String, String, Double> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, Long, String, Integer, String, String, Double> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return RewardVariable.REWARD_VARIABLE.ID;
    }

    @Override
    public Field<Long> field2() {
        return RewardVariable.REWARD_VARIABLE.MODEL_ID;
    }

    @Override
    public Field<String> field3() {
        return RewardVariable.REWARD_VARIABLE.NAME;
    }

    @Override
    public Field<Integer> field4() {
        return RewardVariable.REWARD_VARIABLE.ARRAY_INDEX;
    }

    @Override
    public Field<String> field5() {
        return RewardVariable.REWARD_VARIABLE.DATA_TYPE;
    }

    @Override
    public Field<String> field6() {
        return RewardVariable.REWARD_VARIABLE.GOAL_CONDITION_TYPE;
    }

    @Override
    public Field<Double> field7() {
        return RewardVariable.REWARD_VARIABLE.GOAL_VALUE;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getModelId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public Integer component4() {
        return getArrayIndex();
    }

    @Override
    public String component5() {
        return getDataType();
    }

    @Override
    public String component6() {
        return getGoalConditionType();
    }

    @Override
    public Double component7() {
        return getGoalValue();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getModelId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public Integer value4() {
        return getArrayIndex();
    }

    @Override
    public String value5() {
        return getDataType();
    }

    @Override
    public String value6() {
        return getGoalConditionType();
    }

    @Override
    public Double value7() {
        return getGoalValue();
    }

    @Override
    public RewardVariableRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public RewardVariableRecord value2(Long value) {
        setModelId(value);
        return this;
    }

    @Override
    public RewardVariableRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public RewardVariableRecord value4(Integer value) {
        setArrayIndex(value);
        return this;
    }

    @Override
    public RewardVariableRecord value5(String value) {
        setDataType(value);
        return this;
    }

    @Override
    public RewardVariableRecord value6(String value) {
        setGoalConditionType(value);
        return this;
    }

    @Override
    public RewardVariableRecord value7(Double value) {
        setGoalValue(value);
        return this;
    }

    @Override
    public RewardVariableRecord values(Long value1, Long value2, String value3, Integer value4, String value5, String value6, Double value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RewardVariableRecord
     */
    public RewardVariableRecord() {
        super(RewardVariable.REWARD_VARIABLE);
    }

    /**
     * Create a detached, initialised RewardVariableRecord
     */
    public RewardVariableRecord(Long id, Long modelId, String name, Integer arrayIndex, String dataType, String goalConditionType, Double goalValue) {
        super(RewardVariable.REWARD_VARIABLE);

        set(0, id);
        set(1, modelId);
        set(2, name);
        set(3, arrayIndex);
        set(4, dataType);
        set(5, goalConditionType);
        set(6, goalValue);
    }
}
