package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.Tables.PATHMIND_USER;

import java.util.UUID;

import org.jooq.DSLContext;

import io.skymind.pathmind.shared.data.PathmindUser;

class UserRepository
{
    protected static PathmindUser findByEmailIgnoreCase(DSLContext ctx, String email) {
        return ctx
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.EMAIL.equalIgnoreCase(email))
                .fetchOneInto(PathmindUser.class);
    }

    protected static PathmindUser findById(DSLContext ctx, long id) {
        return ctx
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.ID.eq(id))
                .fetchOneInto(PathmindUser.class);
    }

    protected static PathmindUser findByToken(DSLContext ctx, String token) {
        return ctx
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.EMAIL_VERIFICATION_TOKEN.eq(UUID.fromString(token)))
                .fetchOneInto(PathmindUser.class);
    }

    /**
     * Change a user's password.
     * @param id the id of the user whose password will be changed
     * @param newPassword the new password
     * @return whether the password was updated or not
     */
    protected static boolean changePassword(DSLContext ctx, long id, String newPassword) {
        return 1 == ctx.update(PATHMIND_USER)
                .set(PATHMIND_USER.PASSWORD, newPassword)
                .where(PATHMIND_USER.ID.eq(id))
                .execute();
    }

    protected static long insertUser(DSLContext ctx, PathmindUser pathmindUser, String password)
    {
        return ctx.insertInto(PATHMIND_USER)
                .set(PATHMIND_USER.EMAIL, pathmindUser.getEmail())
                .set(PATHMIND_USER.PASSWORD, password)
                .set(PATHMIND_USER.ACCOUNT_TYPE, pathmindUser.getAccountType().getId())
                .set(PATHMIND_USER.FIRSTNAME, pathmindUser.getFirstname())
                .set(PATHMIND_USER.LASTNAME, pathmindUser.getLastname())
                .set(PATHMIND_USER.DELETE_AT, pathmindUser.getDeleteAt())
                .set(PATHMIND_USER.EMAIL_VERIFICATION_TOKEN, pathmindUser.getEmailVerificationToken())
                .set(PATHMIND_USER.EMAIL_VERIFIED_AT, pathmindUser.getEmailVerifiedAt())
                .returning(PATHMIND_USER.ID)
                .fetchOne()
                .getValue(PATHMIND_USER.ID);
    }

    protected static void update(DSLContext ctx, PathmindUser pathmindUser)
    {
        ctx.update(PATHMIND_USER)
                .set(PATHMIND_USER.EMAIL, pathmindUser.getEmail())
                .set(PATHMIND_USER.ACCOUNT_TYPE, pathmindUser.getAccountType().getId())
                .set(PATHMIND_USER.FIRSTNAME, pathmindUser.getFirstname())
                .set(PATHMIND_USER.LASTNAME, pathmindUser.getLastname())
                .set(PATHMIND_USER.ADDRESS, pathmindUser.getAddress())
                .set(PATHMIND_USER.CITY, pathmindUser.getCity())
                .set(PATHMIND_USER.STATE, pathmindUser.getState())
                .set(PATHMIND_USER.COUNTRY, pathmindUser.getCountry())
                .set(PATHMIND_USER.ZIP, pathmindUser.getZip())
                .set(PATHMIND_USER.DELETE_AT, pathmindUser.getDeleteAt())
                .set(PATHMIND_USER.EMAIL_VERIFICATION_TOKEN, pathmindUser.getEmailVerificationToken())
                .set(PATHMIND_USER.EMAIL_VERIFIED_AT, pathmindUser.getEmailVerifiedAt())
                .set(PATHMIND_USER.PASSWORD_RESET_SEND_AT, pathmindUser.getPasswordResetSendAt())
                .set(PATHMIND_USER.STRIPE_CUSTOMER_ID, pathmindUser.getStripeCustomerId())
                .where(PATHMIND_USER.ID.eq(pathmindUser.getId()))
                .execute();
    }

    protected static void delete(DSLContext ctx, long id)
    {
        ctx.delete(PATHMIND_USER)
                .where(PATHMIND_USER.ID.eq(id))
                .execute();
    }
}
