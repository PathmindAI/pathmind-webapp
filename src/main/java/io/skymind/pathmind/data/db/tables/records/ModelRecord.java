/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Model;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ModelRecord extends UpdatableRecordImpl<ModelRecord> implements Record10<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Integer, String, Boolean, String> {

    private static final long serialVersionUID = 214177358;

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
     * Setter for <code>public.model.number_of_possible_actions</code>.
     */
    public void setNumberOfPossibleActions(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.model.number_of_possible_actions</code>.
     */
    public Integer getNumberOfPossibleActions() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>public.model.get_observation_for_reward_function</code>.
     */
    public void setGetObservationForRewardFunction(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.model.get_observation_for_reward_function</code>.
     */
    public String getGetObservationForRewardFunction() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.model.archived</code>.
     */
    public void setArchived(Boolean value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.model.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(8);
    }

    /**
     * Setter for <code>public.model.user_notes</code>.
     */
    public void setUserNotes(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.model.user_notes</code>.
     */
    public String getUserNotes() {
        return (String) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row10<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Integer, String, Boolean, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    @Override
    public Row10<Long, Long, String, LocalDateTime, LocalDateTime, Integer, Integer, String, Boolean, String> valuesRow() {
        return (Row10) super.valuesRow();
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
    public Field<Integer> field7() {
        return Model.MODEL.NUMBER_OF_POSSIBLE_ACTIONS;
    }

    @Override
    public Field<String> field8() {
        return Model.MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION;
    }

    @Override
    public Field<Boolean> field9() {
        return Model.MODEL.ARCHIVED;
    }

    @Override
    public Field<String> field10() {
        return Model.MODEL.USER_NOTES;
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
    public Integer component7() {
        return getNumberOfPossibleActions();
    }

    @Override
    public String component8() {
        return getGetObservationForRewardFunction();
    }

    @Override
    public Boolean component9() {
        return getArchived();
    }

    @Override
    public String component10() {
        return getUserNotes();
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
    public Integer value7() {
        return getNumberOfPossibleActions();
    }

    @Override
    public String value8() {
        return getGetObservationForRewardFunction();
    }

    @Override
    public Boolean value9() {
        return getArchived();
    }

    @Override
    public String value10() {
        return getUserNotes();
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
    public ModelRecord value7(Integer value) {
        setNumberOfPossibleActions(value);
        return this;
    }

    @Override
    public ModelRecord value8(String value) {
        setGetObservationForRewardFunction(value);
        return this;
    }

    @Override
    public ModelRecord value9(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public ModelRecord value10(String value) {
        setUserNotes(value);
        return this;
    }

    @Override
    public ModelRecord values(Long value1, Long value2, String value3, LocalDateTime value4, LocalDateTime value5, Integer value6, Integer value7, String value8, Boolean value9, String value10) {
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
    public ModelRecord(Long id, Long projectId, String name, LocalDateTime dateCreated, LocalDateTime lastActivityDate, Integer numberOfObservations, Integer numberOfPossibleActions, String getObservationForRewardFunction, Boolean archived, String userNotes) {
        super(Model.MODEL);

        set(0, id);
        set(1, projectId);
        set(2, name);
        set(3, dateCreated);
        set(4, lastActivityDate);
        set(5, numberOfObservations);
        set(6, numberOfPossibleActions);
        set(7, getObservationForRewardFunction);
        set(8, archived);
        set(9, userNotes);
    }
}
