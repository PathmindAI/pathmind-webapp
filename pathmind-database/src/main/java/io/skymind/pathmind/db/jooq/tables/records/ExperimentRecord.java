/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.Experiment;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ExperimentRecord extends UpdatableRecordImpl<ExperimentRecord> implements Record13<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean, String, Boolean, Boolean, Integer, Boolean, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.experiment.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.experiment.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.experiment.model_id</code>.
     */
    public void setModelId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.experiment.model_id</code>.
     */
    public Long getModelId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.experiment.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.experiment.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.experiment.reward_function</code>.
     */
    public void setRewardFunction(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.experiment.reward_function</code>.
     */
    public String getRewardFunction() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.experiment.date_created</code>.
     */
    public void setDateCreated(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.experiment.date_created</code>.
     */
    public LocalDateTime getDateCreated() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>public.experiment.last_activity_date</code>.
     */
    public void setLastActivityDate(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.experiment.last_activity_date</code>.
     */
    public LocalDateTime getLastActivityDate() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.experiment.archived</code>.
     */
    public void setArchived(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.experiment.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>public.experiment.user_notes</code>.
     */
    public void setUserNotes(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.experiment.user_notes</code>.
     */
    public String getUserNotes() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.experiment.is_favorite</code>.
     */
    public void setIsFavorite(Boolean value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.experiment.is_favorite</code>.
     */
    public Boolean getIsFavorite() {
        return (Boolean) get(8);
    }

    /**
     * Setter for <code>public.experiment.has_goals</code>.
     */
    public void setHasGoals(Boolean value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.experiment.has_goals</code>.
     */
    public Boolean getHasGoals() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>public.experiment.training_status</code>.
     */
    public void setTrainingStatus(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.experiment.training_status</code>.
     */
    public Integer getTrainingStatus() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>public.experiment.shared</code>.
     */
    public void setShared(Boolean value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.experiment.shared</code>.
     */
    public Boolean getShared() {
        return (Boolean) get(11);
    }

    /**
     * Setter for <code>public.experiment.deploy_policy_on_success</code>.
     */
    public void setDeployPolicyOnSuccess(Boolean value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.experiment.deploy_policy_on_success</code>.
     */
    public Boolean getDeployPolicyOnSuccess() {
        return (Boolean) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean, String, Boolean, Boolean, Integer, Boolean, Boolean> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<Long, Long, String, String, LocalDateTime, LocalDateTime, Boolean, String, Boolean, Boolean, Integer, Boolean, Boolean> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Experiment.EXPERIMENT.ID;
    }

    @Override
    public Field<Long> field2() {
        return Experiment.EXPERIMENT.MODEL_ID;
    }

    @Override
    public Field<String> field3() {
        return Experiment.EXPERIMENT.NAME;
    }

    @Override
    public Field<String> field4() {
        return Experiment.EXPERIMENT.REWARD_FUNCTION;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Experiment.EXPERIMENT.DATE_CREATED;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Experiment.EXPERIMENT.LAST_ACTIVITY_DATE;
    }

    @Override
    public Field<Boolean> field7() {
        return Experiment.EXPERIMENT.ARCHIVED;
    }

    @Override
    public Field<String> field8() {
        return Experiment.EXPERIMENT.USER_NOTES;
    }

    @Override
    public Field<Boolean> field9() {
        return Experiment.EXPERIMENT.IS_FAVORITE;
    }

    @Override
    public Field<Boolean> field10() {
        return Experiment.EXPERIMENT.HAS_GOALS;
    }

    @Override
    public Field<Integer> field11() {
        return Experiment.EXPERIMENT.TRAINING_STATUS;
    }

    @Override
    public Field<Boolean> field12() {
        return Experiment.EXPERIMENT.SHARED;
    }

    @Override
    public Field<Boolean> field13() {
        return Experiment.EXPERIMENT.DEPLOY_POLICY_ON_SUCCESS;
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
    public String component4() {
        return getRewardFunction();
    }

    @Override
    public LocalDateTime component5() {
        return getDateCreated();
    }

    @Override
    public LocalDateTime component6() {
        return getLastActivityDate();
    }

    @Override
    public Boolean component7() {
        return getArchived();
    }

    @Override
    public String component8() {
        return getUserNotes();
    }

    @Override
    public Boolean component9() {
        return getIsFavorite();
    }

    @Override
    public Boolean component10() {
        return getHasGoals();
    }

    @Override
    public Integer component11() {
        return getTrainingStatus();
    }

    @Override
    public Boolean component12() {
        return getShared();
    }

    @Override
    public Boolean component13() {
        return getDeployPolicyOnSuccess();
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
    public String value4() {
        return getRewardFunction();
    }

    @Override
    public LocalDateTime value5() {
        return getDateCreated();
    }

    @Override
    public LocalDateTime value6() {
        return getLastActivityDate();
    }

    @Override
    public Boolean value7() {
        return getArchived();
    }

    @Override
    public String value8() {
        return getUserNotes();
    }

    @Override
    public Boolean value9() {
        return getIsFavorite();
    }

    @Override
    public Boolean value10() {
        return getHasGoals();
    }

    @Override
    public Integer value11() {
        return getTrainingStatus();
    }

    @Override
    public Boolean value12() {
        return getShared();
    }

    @Override
    public Boolean value13() {
        return getDeployPolicyOnSuccess();
    }

    @Override
    public ExperimentRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ExperimentRecord value2(Long value) {
        setModelId(value);
        return this;
    }

    @Override
    public ExperimentRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public ExperimentRecord value4(String value) {
        setRewardFunction(value);
        return this;
    }

    @Override
    public ExperimentRecord value5(LocalDateTime value) {
        setDateCreated(value);
        return this;
    }

    @Override
    public ExperimentRecord value6(LocalDateTime value) {
        setLastActivityDate(value);
        return this;
    }

    @Override
    public ExperimentRecord value7(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public ExperimentRecord value8(String value) {
        setUserNotes(value);
        return this;
    }

    @Override
    public ExperimentRecord value9(Boolean value) {
        setIsFavorite(value);
        return this;
    }

    @Override
    public ExperimentRecord value10(Boolean value) {
        setHasGoals(value);
        return this;
    }

    @Override
    public ExperimentRecord value11(Integer value) {
        setTrainingStatus(value);
        return this;
    }

    @Override
    public ExperimentRecord value12(Boolean value) {
        setShared(value);
        return this;
    }

    @Override
    public ExperimentRecord value13(Boolean value) {
        setDeployPolicyOnSuccess(value);
        return this;
    }

    @Override
    public ExperimentRecord values(Long value1, Long value2, String value3, String value4, LocalDateTime value5, LocalDateTime value6, Boolean value7, String value8, Boolean value9, Boolean value10, Integer value11, Boolean value12, Boolean value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ExperimentRecord
     */
    public ExperimentRecord() {
        super(Experiment.EXPERIMENT);
    }

    /**
     * Create a detached, initialised ExperimentRecord
     */
    public ExperimentRecord(Long id, Long modelId, String name, String rewardFunction, LocalDateTime dateCreated, LocalDateTime lastActivityDate, Boolean archived, String userNotes, Boolean isFavorite, Boolean hasGoals, Integer trainingStatus, Boolean shared, Boolean deployPolicyOnSuccess) {
        super(Experiment.EXPERIMENT);

        setId(id);
        setModelId(modelId);
        setName(name);
        setRewardFunction(rewardFunction);
        setDateCreated(dateCreated);
        setLastActivityDate(lastActivityDate);
        setArchived(archived);
        setUserNotes(userNotes);
        setIsFavorite(isFavorite);
        setHasGoals(hasGoals);
        setTrainingStatus(trainingStatus);
        setShared(shared);
        setDeployPolicyOnSuccess(deployPolicyOnSuccess);
    }
}
