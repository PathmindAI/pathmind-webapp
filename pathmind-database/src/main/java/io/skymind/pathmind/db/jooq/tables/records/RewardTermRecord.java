/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.RewardTerm;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RewardTermRecord extends TableRecordImpl<RewardTermRecord> implements Record6<Long, Integer, Double, Integer, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.reward_term.experiment_id</code>.
     */
    public void setExperimentId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.reward_term.experiment_id</code>.
     */
    public Long getExperimentId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.reward_term.index</code>.
     */
    public void setIndex(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.reward_term.index</code>.
     */
    public Integer getIndex() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.reward_term.weight</code>.
     */
    public void setWeight(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.reward_term.weight</code>.
     */
    public Double getWeight() {
        return (Double) get(2);
    }

    /**
     * Setter for <code>public.reward_term.reward_variable_array_index</code>.
     */
    public void setRewardVariableArrayIndex(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.reward_term.reward_variable_array_index</code>.
     */
    public Integer getRewardVariableArrayIndex() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.reward_term.goal_condition_type</code>.
     */
    public void setGoalConditionType(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.reward_term.goal_condition_type</code>.
     */
    public String getGoalConditionType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.reward_term.snippet</code>.
     */
    public void setSnippet(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.reward_term.snippet</code>.
     */
    public String getSnippet() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Integer, Double, Integer, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Long, Integer, Double, Integer, String, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return RewardTerm.REWARD_TERM.EXPERIMENT_ID;
    }

    @Override
    public Field<Integer> field2() {
        return RewardTerm.REWARD_TERM.INDEX;
    }

    @Override
    public Field<Double> field3() {
        return RewardTerm.REWARD_TERM.WEIGHT;
    }

    @Override
    public Field<Integer> field4() {
        return RewardTerm.REWARD_TERM.REWARD_VARIABLE_ARRAY_INDEX;
    }

    @Override
    public Field<String> field5() {
        return RewardTerm.REWARD_TERM.GOAL_CONDITION_TYPE;
    }

    @Override
    public Field<String> field6() {
        return RewardTerm.REWARD_TERM.SNIPPET;
    }

    @Override
    public Long component1() {
        return getExperimentId();
    }

    @Override
    public Integer component2() {
        return getIndex();
    }

    @Override
    public Double component3() {
        return getWeight();
    }

    @Override
    public Integer component4() {
        return getRewardVariableArrayIndex();
    }

    @Override
    public String component5() {
        return getGoalConditionType();
    }

    @Override
    public String component6() {
        return getSnippet();
    }

    @Override
    public Long value1() {
        return getExperimentId();
    }

    @Override
    public Integer value2() {
        return getIndex();
    }

    @Override
    public Double value3() {
        return getWeight();
    }

    @Override
    public Integer value4() {
        return getRewardVariableArrayIndex();
    }

    @Override
    public String value5() {
        return getGoalConditionType();
    }

    @Override
    public String value6() {
        return getSnippet();
    }

    @Override
    public RewardTermRecord value1(Long value) {
        setExperimentId(value);
        return this;
    }

    @Override
    public RewardTermRecord value2(Integer value) {
        setIndex(value);
        return this;
    }

    @Override
    public RewardTermRecord value3(Double value) {
        setWeight(value);
        return this;
    }

    @Override
    public RewardTermRecord value4(Integer value) {
        setRewardVariableArrayIndex(value);
        return this;
    }

    @Override
    public RewardTermRecord value5(String value) {
        setGoalConditionType(value);
        return this;
    }

    @Override
    public RewardTermRecord value6(String value) {
        setSnippet(value);
        return this;
    }

    @Override
    public RewardTermRecord values(Long value1, Integer value2, Double value3, Integer value4, String value5, String value6) {
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
     * Create a detached RewardTermRecord
     */
    public RewardTermRecord() {
        super(RewardTerm.REWARD_TERM);
    }

    /**
     * Create a detached, initialised RewardTermRecord
     */
    public RewardTermRecord(Long experimentId, Integer index, Double weight, Integer rewardVariableArrayIndex, String goalConditionType, String snippet) {
        super(RewardTerm.REWARD_TERM);

        setExperimentId(experimentId);
        setIndex(index);
        setWeight(weight);
        setRewardVariableArrayIndex(rewardVariableArrayIndex);
        setGoalConditionType(goalConditionType);
        setSnippet(snippet);
    }
}