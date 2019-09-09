/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db.tables.records;


import io.skymind.pathmind.data.db.tables.Project;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ProjectRecord extends UpdatableRecordImpl<ProjectRecord> implements Record8<Long, Long, String, LocalDate, LocalDate, Long, BigDecimal, String> {

    private static final long serialVersionUID = -622824073;

    /**
     * Setter for <code>public.project.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.project.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.project.pathmind_user_id</code>.
     */
    public void setPathmindUserId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.project.pathmind_user_id</code>.
     */
    public Long getPathmindUserId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.project.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.project.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.project.date_created</code>.
     */
    public void setDateCreated(LocalDate value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.project.date_created</code>.
     */
    public LocalDate getDateCreated() {
        return (LocalDate) get(3);
    }

    /**
     * Setter for <code>public.project.last_activity_date</code>.
     */
    public void setLastActivityDate(LocalDate value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.project.last_activity_date</code>.
     */
    public LocalDate getLastActivityDate() {
        return (LocalDate) get(4);
    }

    /**
     * Setter for <code>public.project.number_of_observations</code>.
     */
    public void setNumberOfObservations(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.project.number_of_observations</code>.
     */
    public Long getNumberOfObservations() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>public.project.number_of_possible_actions</code>.
     */
    public void setNumberOfPossibleActions(BigDecimal value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.project.number_of_possible_actions</code>.
     */
    public BigDecimal getNumberOfPossibleActions() {
        return (BigDecimal) get(6);
    }

    /**
     * Setter for <code>public.project.get_observation_for_reward_function</code>.
     */
    public void setGetObservationForRewardFunction(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.project.get_observation_for_reward_function</code>.
     */
    public String getGetObservationForRewardFunction() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, String, LocalDate, LocalDate, Long, BigDecimal, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, String, LocalDate, LocalDate, Long, BigDecimal, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Project.PROJECT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Project.PROJECT.PATHMIND_USER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Project.PROJECT.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDate> field4() {
        return Project.PROJECT.DATE_CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDate> field5() {
        return Project.PROJECT.LAST_ACTIVITY_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return Project.PROJECT.NUMBER_OF_OBSERVATIONS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field7() {
        return Project.PROJECT.NUMBER_OF_POSSIBLE_ACTIONS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Project.PROJECT.GET_OBSERVATION_FOR_REWARD_FUNCTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component2() {
        return getPathmindUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate component4() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate component5() {
        return getLastActivityDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component6() {
        return getNumberOfObservations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component7() {
        return getNumberOfPossibleActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getGetObservationForRewardFunction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getPathmindUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate value4() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate value5() {
        return getLastActivityDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getNumberOfObservations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value7() {
        return getNumberOfPossibleActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getGetObservationForRewardFunction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value2(Long value) {
        setPathmindUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value4(LocalDate value) {
        setDateCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value5(LocalDate value) {
        setLastActivityDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value6(Long value) {
        setNumberOfObservations(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value7(BigDecimal value) {
        setNumberOfPossibleActions(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord value8(String value) {
        setGetObservationForRewardFunction(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectRecord values(Long value1, Long value2, String value3, LocalDate value4, LocalDate value5, Long value6, BigDecimal value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProjectRecord
     */
    public ProjectRecord() {
        super(Project.PROJECT);
    }

    /**
     * Create a detached, initialised ProjectRecord
     */
    public ProjectRecord(Long id, Long pathmindUserId, String name, LocalDate dateCreated, LocalDate lastActivityDate, Long numberOfObservations, BigDecimal numberOfPossibleActions, String getObservationForRewardFunction) {
        super(Project.PROJECT);

        set(0, id);
        set(1, pathmindUserId);
        set(2, name);
        set(3, dateCreated);
        set(4, lastActivityDate);
        set(5, numberOfObservations);
        set(6, numberOfPossibleActions);
        set(7, getObservationForRewardFunction);
    }
}
