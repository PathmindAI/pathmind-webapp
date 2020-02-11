package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.Tables.MODEL;
import static io.skymind.pathmind.db.jooq.Tables.PATHMIND_USER;
import static io.skymind.pathmind.db.jooq.Tables.POLICY;
import static io.skymind.pathmind.db.jooq.Tables.PROJECT;
import static io.skymind.pathmind.db.jooq.Tables.RUN;

import java.util.UUID;

import org.jooq.DSLContext;

import io.skymind.pathmind.db.data.PathmindUser;
import io.skymind.pathmind.shared.security.SecurityUtils;

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
                .set(PATHMIND_USER.ACCOUNT_TYPE, pathmindUser.getAccountType())
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
                .set(PATHMIND_USER.ACCOUNT_TYPE, pathmindUser.getAccountType())
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

    // STEPH -> This code didnt' actually check the data ID
    protected static boolean isUserAllowedAccessToProject(DSLContext ctx, long projectId) {
        int count = ctx.selectCount()
                .from(PROJECT)
                .where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
                    .and(PROJECT.ID.eq(projectId))
                .fetchOne(0, int.class);
        return count > 0;
    }

    protected static boolean isUserAllowedAccessToModel(DSLContext ctx, long modelId) {
        int count = ctx.selectCount()
                .from(MODEL)
                .leftJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
                    .and(MODEL.ID.eq(modelId))
                .fetchOne(0, int.class);
        return count > 0;
    }

    protected static boolean isUserAllowedAccessToExperiment(DSLContext ctx, long experimentId) {
        int count = ctx.selectCount()
                .from(EXPERIMENT)
                .leftJoin(MODEL).on(EXPERIMENT.MODEL_ID.eq(MODEL.ID))
                .leftJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
                    .and(EXPERIMENT.ID.eq(experimentId))
                .fetchOne(0, int.class);
        return count > 0;
    }

    protected static boolean isUserAllowedAccessToPolicy(DSLContext ctx, long policyId) {
        int count = ctx.selectCount()
                .from(POLICY)
                .leftJoin(RUN).on(POLICY.RUN_ID.eq(RUN.ID))
                .leftJoin(EXPERIMENT).on(RUN.EXPERIMENT_ID.eq(EXPERIMENT.ID))
                .leftJoin(MODEL).on(EXPERIMENT.MODEL_ID.eq(MODEL.ID))
                .leftJoin(PROJECT).on(MODEL.PROJECT_ID.eq(PROJECT.ID))
                .where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
                    .and(POLICY.ID.eq(policyId))
                .fetchOne(0, int.class);
        return count > 0;
    }
}
