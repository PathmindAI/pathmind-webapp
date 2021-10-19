/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import io.skymind.pathmind.db.jooq.tables.Model;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record22;
import org.jooq.Row22;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ModelRecord extends UpdatableRecordImpl<ModelRecord> implements Record22<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Boolean, String, Integer, Boolean, String, Integer, Integer, Boolean, Integer, Integer, String, String, String, String, Boolean, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.model.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.model.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.model.project_id</code>.
     */
    public void setProjectId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.model.project_id</code>.
     */
    public Long getProjectId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.model.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.model.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.model.date_created</code>.
     */
    public void setDateCreated(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.model.date_created</code>.
     */
    public LocalDateTime getDateCreated() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>public.model.last_activity_date</code>.
     */
    public void setLastActivityDate(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.model.last_activity_date</code>.
     */
    public LocalDateTime getLastActivityDate() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>public.model.number_of_observations</code>.
     */
    public void setNumberOfObservations(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.model.number_of_observations</code>.
     */
    public Integer getNumberOfObservations() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>public.model.archived</code>.
     */
    public void setArchived(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.model.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>public.model.user_notes</code>.
     */
    public void setUserNotes(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.model.user_notes</code>.
     */
    public String getUserNotes() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.model.reward_variables_count</code>.
     */
    public void setRewardVariablesCount(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.model.reward_variables_count</code>.
     */
    public Integer getRewardVariablesCount() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>public.model.draft</code>.
     */
    public void setDraft(Boolean value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.model.draft</code>.
     */
    public Boolean getDraft() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>public.model.package_name</code>.
     */
    public void setPackageName(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.model.package_name</code>.
     */
    public String getPackageName() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.model.action_tuple_size</code>.
     */
    public void setActionTupleSize(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.model.action_tuple_size</code>.
     */
    public Integer getActionTupleSize() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>public.model.invalid_model</code>.
     */
    public void setInvalidModel(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.model.invalid_model</code>.
     */
    public Integer getInvalidModel() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>public.model.has_goals</code>.
     */
    public void setHasGoals(Boolean value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.model.has_goals</code>.
     */
    public Boolean getHasGoals() {
        return (Boolean) get(13);
    }

    /**
     * Setter for <code>public.model.model_type</code>.
     */
    public void setModelType(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>public.model.model_type</code>.
     */
    public Integer getModelType() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>public.model.number_of_agents</code>.
     */
    public void setNumberOfAgents(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>public.model.number_of_agents</code>.
     */
    public Integer getNumberOfAgents() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>public.model.pathmind_helper</code>.
     */
    public void setPathmindHelper(String value) {
        set(16, value);
    }

    /**
     * Getter for <code>public.model.pathmind_helper</code>.
     */
    public String getPathmindHelper() {
        return (String) get(16);
    }

    /**
     * Setter for <code>public.model.main_agent</code>.
     */
    public void setMainAgent(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>public.model.main_agent</code>.
     */
    public String getMainAgent() {
        return (String) get(17);
    }

    /**
     * Setter for <code>public.model.experiment_class</code>.
     */
    public void setExperimentClass(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>public.model.experiment_class</code>.
     */
    public String getExperimentClass() {
        return (String) get(18);
    }

    /**
     * Setter for <code>public.model.experiment_type</code>.
     */
    public void setExperimentType(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>public.model.experiment_type</code>.
     */
    public String getExperimentType() {
        return (String) get(19);
    }

    /**
     * Setter for <code>public.model.actionmask</code>.
     */
    public void setActionmask(Boolean value) {
        set(20, value);
    }

    /**
     * Getter for <code>public.model.actionmask</code>.
     */
    public Boolean getActionmask() {
        return (Boolean) get(20);
    }

    /**
     * Setter for <code>public.model.project_changed_at</code>.
     */
    public void setProjectChangedAt(LocalDateTime value) {
        set(21, value);
    }

    /**
     * Getter for <code>public.model.project_changed_at</code>.
     */
    public LocalDateTime getProjectChangedAt() {
        return (LocalDateTime) get(21);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record22 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row22<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Boolean, String, Integer, Boolean, String, Integer, Integer, Boolean, Integer, Integer, String, String, String, String, Boolean, LocalDateTime> fieldsRow() {
        return (Row22) super.fieldsRow();
    }

    @Override
    public Row22<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Boolean, String, Integer, Boolean, String, Integer, Integer, Boolean, Integer, Integer, String, String, String, String, Boolean, LocalDateTime> valuesRow() {
        return (Row22) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Model.MODEL.ID;
    }

    @Override
    public Field<Long> field2() {
        return Model.MODEL.PROJECT_ID;
    }

    @Override
    public Field<String> field3() {
        return Model.MODEL.NAME;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Model.MODEL.DATE_CREATED;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Model.MODEL.LAST_ACTIVITY_DATE;
    }

    @Override
    public Field<Integer> field6() {
        return Model.MODEL.NUMBER_OF_OBSERVATIONS;
    }

    @Override
    public Field<Boolean> field7() {
        return Model.MODEL.ARCHIVED;
    }

    @Override
    public Field<String> field8() {
        return Model.MODEL.USER_NOTES;
    }

    @Override
    public Field<Integer> field9() {
        return Model.MODEL.REWARD_VARIABLES_COUNT;
    }

    @Override
    public Field<Boolean> field10() {
        return Model.MODEL.DRAFT;
    }

    @Override
    public Field<String> field11() {
        return Model.MODEL.PACKAGE_NAME;
    }

    @Override
    public Field<Integer> field12() {
        return Model.MODEL.ACTION_TUPLE_SIZE;
    }

    @Override
    public Field<Integer> field13() {
        return Model.MODEL.INVALID_MODEL;
    }

    @Override
    public Field<Boolean> field14() {
        return Model.MODEL.HAS_GOALS;
    }

    @Override
    public Field<Integer> field15() {
        return Model.MODEL.MODEL_TYPE;
    }

    @Override
    public Field<Integer> field16() {
        return Model.MODEL.NUMBER_OF_AGENTS;
    }

    @Override
    public Field<String> field17() {
        return Model.MODEL.PATHMIND_HELPER;
    }

    @Override
    public Field<String> field18() {
        return Model.MODEL.MAIN_AGENT;
    }

    @Override
    public Field<String> field19() {
        return Model.MODEL.EXPERIMENT_CLASS;
    }

    @Override
    public Field<String> field20() {
        return Model.MODEL.EXPERIMENT_TYPE;
    }

    @Override
    public Field<Boolean> field21() {
        return Model.MODEL.ACTIONMASK;
    }

    @Override
    public Field<LocalDateTime> field22() {
        return Model.MODEL.PROJECT_CHANGED_AT;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getProjectId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public LocalDateTime component4() {
        return getDateCreated();
    }

    @Override
    public LocalDateTime component5() {
        return getLastActivityDate();
    }

    @Override
    public Integer component6() {
        return getNumberOfObservations();
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
    public Integer component9() {
        return getRewardVariablesCount();
    }

    @Override
    public Boolean component10() {
        return getDraft();
    }

    @Override
    public String component11() {
        return getPackageName();
    }

    @Override
    public Integer component12() {
        return getActionTupleSize();
    }

    @Override
    public Integer component13() {
        return getInvalidModel();
    }

    @Override
    public Boolean component14() {
        return getHasGoals();
    }

    @Override
    public Integer component15() {
        return getModelType();
    }

    @Override
    public Integer component16() {
        return getNumberOfAgents();
    }

    @Override
    public String component17() {
        return getPathmindHelper();
    }

    @Override
    public String component18() {
        return getMainAgent();
    }

    @Override
    public String component19() {
        return getExperimentClass();
    }

    @Override
    public String component20() {
        return getExperimentType();
    }

    @Override
    public Boolean component21() {
        return getActionmask();
    }

    @Override
    public LocalDateTime component22() {
        return getProjectChangedAt();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getProjectId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public LocalDateTime value4() {
        return getDateCreated();
    }

    @Override
    public LocalDateTime value5() {
        return getLastActivityDate();
    }

    @Override
    public Integer value6() {
        return getNumberOfObservations();
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
    public Integer value9() {
        return getRewardVariablesCount();
    }

    @Override
    public Boolean value10() {
        return getDraft();
    }

    @Override
    public String value11() {
        return getPackageName();
    }

    @Override
    public Integer value12() {
        return getActionTupleSize();
    }

    @Override
    public Integer value13() {
        return getInvalidModel();
    }

    @Override
    public Boolean value14() {
        return getHasGoals();
    }

    @Override
    public Integer value15() {
        return getModelType();
    }

    @Override
    public Integer value16() {
        return getNumberOfAgents();
    }

    @Override
    public String value17() {
        return getPathmindHelper();
    }

    @Override
    public String value18() {
        return getMainAgent();
    }

    @Override
    public String value19() {
        return getExperimentClass();
    }

    @Override
    public String value20() {
        return getExperimentType();
    }

    @Override
    public Boolean value21() {
        return getActionmask();
    }

    @Override
    public LocalDateTime value22() {
        return getProjectChangedAt();
    }

    @Override
    public ModelRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ModelRecord value2(Long value) {
        setProjectId(value);
        return this;
    }

    @Override
    public ModelRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public ModelRecord value4(LocalDateTime value) {
        setDateCreated(value);
        return this;
    }

    @Override
    public ModelRecord value5(LocalDateTime value) {
        setLastActivityDate(value);
        return this;
    }

    @Override
    public ModelRecord value6(Integer value) {
        setNumberOfObservations(value);
        return this;
    }

    @Override
    public ModelRecord value7(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public ModelRecord value8(String value) {
        setUserNotes(value);
        return this;
    }

    @Override
    public ModelRecord value9(Integer value) {
        setRewardVariablesCount(value);
        return this;
    }

    @Override
    public ModelRecord value10(Boolean value) {
        setDraft(value);
        return this;
    }

    @Override
    public ModelRecord value11(String value) {
        setPackageName(value);
        return this;
    }

    @Override
    public ModelRecord value12(Integer value) {
        setActionTupleSize(value);
        return this;
    }

    @Override
    public ModelRecord value13(Integer value) {
        setInvalidModel(value);
        return this;
    }

    @Override
    public ModelRecord value14(Boolean value) {
        setHasGoals(value);
        return this;
    }

    @Override
    public ModelRecord value15(Integer value) {
        setModelType(value);
        return this;
    }

    @Override
    public ModelRecord value16(Integer value) {
        setNumberOfAgents(value);
        return this;
    }

    @Override
    public ModelRecord value17(String value) {
        setPathmindHelper(value);
        return this;
    }

    @Override
    public ModelRecord value18(String value) {
        setMainAgent(value);
        return this;
    }

    @Override
    public ModelRecord value19(String value) {
        setExperimentClass(value);
        return this;
    }

    @Override
    public ModelRecord value20(String value) {
        setExperimentType(value);
        return this;
    }

    @Override
    public ModelRecord value21(Boolean value) {
        setActionmask(value);
        return this;
    }

    @Override
    public ModelRecord value22(LocalDateTime value) {
        setProjectChangedAt(value);
        return this;
    }

    @Override
    public ModelRecord values(Long value1, Long value2, String value3, LocalDateTime value4, LocalDateTime value5, Integer value6, Boolean value7, String value8, Integer value9, Boolean value10, String value11, Integer value12, Integer value13, Boolean value14, Integer value15, Integer value16, String value17, String value18, String value19, String value20, Boolean value21, LocalDateTime value22) {
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
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        value19(value19);
        value20(value20);
        value21(value21);
        value22(value22);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ModelRecord
     */
    public ModelRecord() {
        super(Model.MODEL);
    }

    /**
     * Create a detached, initialised ModelRecord
     */
    public ModelRecord(Long id, Long projectId, String name, LocalDateTime dateCreated, LocalDateTime lastActivityDate, Integer numberOfObservations, Boolean archived, String userNotes, Integer rewardVariablesCount, Boolean draft, String packageName, Integer actionTupleSize, Integer invalidModel, Boolean hasGoals, Integer modelType, Integer numberOfAgents, String pathmindHelper, String mainAgent, String experimentClass, String experimentType, Boolean actionmask, LocalDateTime projectChangedAt) {
        super(Model.MODEL);

        setId(id);
        setProjectId(projectId);
        setName(name);
        setDateCreated(dateCreated);
        setLastActivityDate(lastActivityDate);
        setNumberOfObservations(numberOfObservations);
        setArchived(archived);
        setUserNotes(userNotes);
        setRewardVariablesCount(rewardVariablesCount);
        setDraft(draft);
        setPackageName(packageName);
        setActionTupleSize(actionTupleSize);
        setInvalidModel(invalidModel);
        setHasGoals(hasGoals);
        setModelType(modelType);
        setNumberOfAgents(numberOfAgents);
        setPathmindHelper(pathmindHelper);
        setMainAgent(mainAgent);
        setExperimentClass(experimentClass);
        setExperimentType(experimentType);
        setActionmask(actionmask);
        setProjectChangedAt(projectChangedAt);
    }
}
