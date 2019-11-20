package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.UserUpdateBusEvent;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import io.skymind.pathmind.security.SecurityUtils;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static io.skymind.pathmind.data.db.Tables.*;

@Repository
public class UserDAO extends UserRepository
{

	private final DSLContext ctx;

	public UserDAO(DSLContext ctx)
	{
		this.ctx = ctx;
	}

	@Transactional
	public long insertUser(PathmindUser pathmindUser)
	{
		long pathmindUserId = ctx.insertInto(PATHMIND_USER)
				.set(PATHMIND_USER.NAME, pathmindUser.getName())
				.set(PATHMIND_USER.EMAIL, pathmindUser.getEmail())
				.set(PATHMIND_USER.PASSWORD, passwordEncoder.encode(pathmindUser.getPassword()))
				.set(PATHMIND_USER.ACCOUNT_TYPE, pathmindUser.getAccountType())
				.set(PATHMIND_USER.FIRSTNAME, pathmindUser.getFirstname())
				.set(PATHMIND_USER.LASTNAME, pathmindUser.getLastname())
				.set(PATHMIND_USER.DELETE_AT, pathmindUser.getDeleteAt())
				.set(PATHMIND_USER.EMAIL_VERIFICATION_TOKEN, pathmindUser.getEmailVerificationToken())
				.set(PATHMIND_USER.EMAIL_VERIFIED_AT, pathmindUser.getEmailVerifiedAt())
				.returning(PATHMIND_USER.ID)
				.fetchOne()
				.getValue(PATHMIND_USER.ID);
		pathmindUser.setId(pathmindUserId);
		return pathmindUserId;
	}

	@Transactional
	public void update(PathmindUser pathmindUser)
	{
		ctx.update(PATHMIND_USER)
				.set(PATHMIND_USER.NAME, pathmindUser.getName())
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

		EventBus.post(new UserUpdateBusEvent(pathmindUser));
	}

	@Transactional
	public void delete(long id)
	{
		ctx.delete(PATHMIND_USER)
				.where(PATHMIND_USER.ID.eq(id))
				.execute();
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToProject(long projectId) {
		int count = ctx.selectCount()
			.from(PROJECT)
			.where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
			.fetchOne(0, int.class);
		return count > 0;
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToModel(long modelId) {
		int count = ctx.selectCount()
				.from(MODEL)
				.leftJoin(PROJECT)
					.on(MODEL.PROJECT_ID.eq(PROJECT.ID))
				.where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
				.fetchOne(0, int.class);
		return count > 0;
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToExperiment(long experimentId) {
		int count = ctx.selectCount()
				.from(EXPERIMENT)
				.leftJoin(MODEL)
					.on(EXPERIMENT.MODEL_ID.eq(MODEL.ID))
				.leftJoin(PROJECT)
					.on(MODEL.PROJECT_ID.eq(PROJECT.ID))
				.where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
				.fetchOne(0, int.class);
		return count > 0;
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToPolicy(long policyId) {
		int count = ctx.selectCount()
				.from(POLICY)
				.leftJoin(RUN)
					.on(POLICY.RUN_ID.eq(RUN.ID))
				.leftJoin(EXPERIMENT)
					.on(RUN.EXPERIMENT_ID.eq(EXPERIMENT.ID))
				.leftJoin(MODEL)
					.on(EXPERIMENT.MODEL_ID.eq(MODEL.ID))
				.leftJoin(PROJECT)
					.on(MODEL.PROJECT_ID.eq(PROJECT.ID))
				.where(PROJECT.PATHMIND_USER_ID.eq(SecurityUtils.getUserId()))
				.fetchOne(0, int.class);
		return count > 0;
	}
}
