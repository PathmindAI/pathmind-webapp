package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static io.skymind.pathmind.data.db.Tables.PATHMIND_USER;

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
				.set(PATHMIND_USER.PASSWORD, pathmindUser.getPassword())
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
				.set(PATHMIND_USER.PASSWORD, pathmindUser.getPassword())
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
				.where(PATHMIND_USER.ID.eq(pathmindUser.getId()))
				.execute();
	}

	@Transactional
	public void delete(long id)
	{
		ctx.delete(PATHMIND_USER)
				.where(PATHMIND_USER.ID.eq(id))
				.execute();
	}

}
