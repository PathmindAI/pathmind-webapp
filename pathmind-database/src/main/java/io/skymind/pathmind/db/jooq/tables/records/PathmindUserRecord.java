/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables.records;


import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.processing.Generated;

import io.skymind.pathmind.db.jooq.tables.PathmindUser;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record17;
import org.jooq.Row17;
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
public class PathmindUserRecord extends UpdatableRecordImpl<PathmindUserRecord> implements Record17<Long, String, String, Integer, String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, UUID, LocalDateTime, String, String> {

    private static final long serialVersionUID = -981236657;

    /**
     * Setter for <code>public.pathmind_user.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pathmind_user.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pathmind_user.email</code>.
     */
    public void setEmail(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pathmind_user.email</code>.
     */
    public String getEmail() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.pathmind_user.password</code>.
     */
    public void setPassword(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pathmind_user.password</code>.
     */
    public String getPassword() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.pathmind_user.account_type</code>.
     */
    public void setAccountType(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.pathmind_user.account_type</code>.
     */
    public Integer getAccountType() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.pathmind_user.firstname</code>.
     */
    public void setFirstname(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.pathmind_user.firstname</code>.
     */
    public String getFirstname() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.pathmind_user.lastname</code>.
     */
    public void setLastname(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.pathmind_user.lastname</code>.
     */
    public String getLastname() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.pathmind_user.address</code>.
     */
    public void setAddress(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.pathmind_user.address</code>.
     */
    public String getAddress() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.pathmind_user.city</code>.
     */
    public void setCity(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.pathmind_user.city</code>.
     */
    public String getCity() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.pathmind_user.state</code>.
     */
    public void setState(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.pathmind_user.state</code>.
     */
    public String getState() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.pathmind_user.country</code>.
     */
    public void setCountry(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.pathmind_user.country</code>.
     */
    public String getCountry() {
        return (String) get(9);
    }

    /**
     * Setter for <code>public.pathmind_user.zip</code>.
     */
    public void setZip(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.pathmind_user.zip</code>.
     */
    public String getZip() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.pathmind_user.delete_at</code>.
     */
    public void setDeleteAt(LocalDateTime value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.pathmind_user.delete_at</code>.
     */
    public LocalDateTime getDeleteAt() {
        return (LocalDateTime) get(11);
    }

    /**
     * Setter for <code>public.pathmind_user.email_verified_at</code>.
     */
    public void setEmailVerifiedAt(LocalDateTime value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.pathmind_user.email_verified_at</code>.
     */
    public LocalDateTime getEmailVerifiedAt() {
        return (LocalDateTime) get(12);
    }

    /**
     * Setter for <code>public.pathmind_user.email_verification_token</code>.
     */
    public void setEmailVerificationToken(UUID value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.pathmind_user.email_verification_token</code>.
     */
    public UUID getEmailVerificationToken() {
        return (UUID) get(13);
    }

    /**
     * Setter for <code>public.pathmind_user.password_reset_send_at</code>.
     */
    public void setPasswordResetSendAt(LocalDateTime value) {
        set(14, value);
    }

    /**
     * Getter for <code>public.pathmind_user.password_reset_send_at</code>.
     */
    public LocalDateTime getPasswordResetSendAt() {
        return (LocalDateTime) get(14);
    }

    /**
     * Setter for <code>public.pathmind_user.stripe_customer_id</code>.
     */
    public void setStripeCustomerId(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>public.pathmind_user.stripe_customer_id</code>.
     */
    public String getStripeCustomerId() {
        return (String) get(15);
    }

    /**
     * Setter for <code>public.pathmind_user.new_email_to_verify</code>.
     */
    public void setNewEmailToVerify(String value) {
        set(16, value);
    }

    /**
     * Getter for <code>public.pathmind_user.new_email_to_verify</code>.
     */
    public String getNewEmailToVerify() {
        return (String) get(16);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record17 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row17<Long, String, String, Integer, String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, UUID, LocalDateTime, String, String> fieldsRow() {
        return (Row17) super.fieldsRow();
    }

    @Override
    public Row17<Long, String, String, Integer, String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, UUID, LocalDateTime, String, String> valuesRow() {
        return (Row17) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PathmindUser.PATHMIND_USER.ID;
    }

    @Override
    public Field<String> field2() {
        return PathmindUser.PATHMIND_USER.EMAIL;
    }

    @Override
    public Field<String> field3() {
        return PathmindUser.PATHMIND_USER.PASSWORD;
    }

    @Override
    public Field<Integer> field4() {
        return PathmindUser.PATHMIND_USER.ACCOUNT_TYPE;
    }

    @Override
    public Field<String> field5() {
        return PathmindUser.PATHMIND_USER.FIRSTNAME;
    }

    @Override
    public Field<String> field6() {
        return PathmindUser.PATHMIND_USER.LASTNAME;
    }

    @Override
    public Field<String> field7() {
        return PathmindUser.PATHMIND_USER.ADDRESS;
    }

    @Override
    public Field<String> field8() {
        return PathmindUser.PATHMIND_USER.CITY;
    }

    @Override
    public Field<String> field9() {
        return PathmindUser.PATHMIND_USER.STATE;
    }

    @Override
    public Field<String> field10() {
        return PathmindUser.PATHMIND_USER.COUNTRY;
    }

    @Override
    public Field<String> field11() {
        return PathmindUser.PATHMIND_USER.ZIP;
    }

    @Override
    public Field<LocalDateTime> field12() {
        return PathmindUser.PATHMIND_USER.DELETE_AT;
    }

    @Override
    public Field<LocalDateTime> field13() {
        return PathmindUser.PATHMIND_USER.EMAIL_VERIFIED_AT;
    }

    @Override
    public Field<UUID> field14() {
        return PathmindUser.PATHMIND_USER.EMAIL_VERIFICATION_TOKEN;
    }

    @Override
    public Field<LocalDateTime> field15() {
        return PathmindUser.PATHMIND_USER.PASSWORD_RESET_SEND_AT;
    }

    @Override
    public Field<String> field16() {
        return PathmindUser.PATHMIND_USER.STRIPE_CUSTOMER_ID;
    }

    @Override
    public Field<String> field17() {
        return PathmindUser.PATHMIND_USER.NEW_EMAIL_TO_VERIFY;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getEmail();
    }

    @Override
    public String component3() {
        return getPassword();
    }

    @Override
    public Integer component4() {
        return getAccountType();
    }

    @Override
    public String component5() {
        return getFirstname();
    }

    @Override
    public String component6() {
        return getLastname();
    }

    @Override
    public String component7() {
        return getAddress();
    }

    @Override
    public String component8() {
        return getCity();
    }

    @Override
    public String component9() {
        return getState();
    }

    @Override
    public String component10() {
        return getCountry();
    }

    @Override
    public String component11() {
        return getZip();
    }

    @Override
    public LocalDateTime component12() {
        return getDeleteAt();
    }

    @Override
    public LocalDateTime component13() {
        return getEmailVerifiedAt();
    }

    @Override
    public UUID component14() {
        return getEmailVerificationToken();
    }

    @Override
    public LocalDateTime component15() {
        return getPasswordResetSendAt();
    }

    @Override
    public String component16() {
        return getStripeCustomerId();
    }

    @Override
    public String component17() {
        return getNewEmailToVerify();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getEmail();
    }

    @Override
    public String value3() {
        return getPassword();
    }

    @Override
    public Integer value4() {
        return getAccountType();
    }

    @Override
    public String value5() {
        return getFirstname();
    }

    @Override
    public String value6() {
        return getLastname();
    }

    @Override
    public String value7() {
        return getAddress();
    }

    @Override
    public String value8() {
        return getCity();
    }

    @Override
    public String value9() {
        return getState();
    }

    @Override
    public String value10() {
        return getCountry();
    }

    @Override
    public String value11() {
        return getZip();
    }

    @Override
    public LocalDateTime value12() {
        return getDeleteAt();
    }

    @Override
    public LocalDateTime value13() {
        return getEmailVerifiedAt();
    }

    @Override
    public UUID value14() {
        return getEmailVerificationToken();
    }

    @Override
    public LocalDateTime value15() {
        return getPasswordResetSendAt();
    }

    @Override
    public String value16() {
        return getStripeCustomerId();
    }

    @Override
    public String value17() {
        return getNewEmailToVerify();
    }

    @Override
    public PathmindUserRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PathmindUserRecord value2(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public PathmindUserRecord value3(String value) {
        setPassword(value);
        return this;
    }

    @Override
    public PathmindUserRecord value4(Integer value) {
        setAccountType(value);
        return this;
    }

    @Override
    public PathmindUserRecord value5(String value) {
        setFirstname(value);
        return this;
    }

    @Override
    public PathmindUserRecord value6(String value) {
        setLastname(value);
        return this;
    }

    @Override
    public PathmindUserRecord value7(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public PathmindUserRecord value8(String value) {
        setCity(value);
        return this;
    }

    @Override
    public PathmindUserRecord value9(String value) {
        setState(value);
        return this;
    }

    @Override
    public PathmindUserRecord value10(String value) {
        setCountry(value);
        return this;
    }

    @Override
    public PathmindUserRecord value11(String value) {
        setZip(value);
        return this;
    }

    @Override
    public PathmindUserRecord value12(LocalDateTime value) {
        setDeleteAt(value);
        return this;
    }

    @Override
    public PathmindUserRecord value13(LocalDateTime value) {
        setEmailVerifiedAt(value);
        return this;
    }

    @Override
    public PathmindUserRecord value14(UUID value) {
        setEmailVerificationToken(value);
        return this;
    }

    @Override
    public PathmindUserRecord value15(LocalDateTime value) {
        setPasswordResetSendAt(value);
        return this;
    }

    @Override
    public PathmindUserRecord value16(String value) {
        setStripeCustomerId(value);
        return this;
    }

    @Override
    public PathmindUserRecord value17(String value) {
        setNewEmailToVerify(value);
        return this;
    }

    @Override
    public PathmindUserRecord values(Long value1, String value2, String value3, Integer value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, LocalDateTime value12, LocalDateTime value13, UUID value14, LocalDateTime value15, String value16, String value17) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PathmindUserRecord
     */
    public PathmindUserRecord() {
        super(PathmindUser.PATHMIND_USER);
    }

    /**
     * Create a detached, initialised PathmindUserRecord
     */
    public PathmindUserRecord(Long id, String email, String password, Integer accountType, String firstname, String lastname, String address, String city, String state, String country, String zip, LocalDateTime deleteAt, LocalDateTime emailVerifiedAt, UUID emailVerificationToken, LocalDateTime passwordResetSendAt, String stripeCustomerId, String newEmailToVerify) {
        super(PathmindUser.PATHMIND_USER);

        set(0, id);
        set(1, email);
        set(2, password);
        set(3, accountType);
        set(4, firstname);
        set(5, lastname);
        set(6, address);
        set(7, city);
        set(8, state);
        set(9, country);
        set(10, zip);
        set(11, deleteAt);
        set(12, emailVerifiedAt);
        set(13, emailVerificationToken);
        set(14, passwordResetSendAt);
        set(15, stripeCustomerId);
        set(16, newEmailToVerify);
    }
}
