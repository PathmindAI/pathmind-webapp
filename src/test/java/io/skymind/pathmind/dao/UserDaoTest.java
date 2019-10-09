package io.skymind.pathmind.dao;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import static io.skymind.pathmind.data.db.Tables.PATHMIND_USER;
import static io.skymind.pathmind.testutils.UserUtils.*;
import static org.junit.Assert.*;

@Transactional
public class UserDaoTest extends PathmindApplicationTests
{

	@Autowired
	UserDAO userDAO;
	@Autowired
	DSLContext dslContext;

	@Test
	public void insert()
	{
		PathmindUser pathmindUser = getNewPathmindUser();
		long pathmindUserId = userDAO.insertUser(pathmindUser);
		PathmindUser savedPathmindUser = userDAO.findById(pathmindUserId);
		assertEquals(pathmindUser.getEmail(), savedPathmindUser.getEmail());
		assertEquals(pathmindUser.getName(), savedPathmindUser.getName());
		assertEquals(pathmindUser.getPassword(), savedPathmindUser.getPassword());
		assertEquals(pathmindUser.getAccountType(), savedPathmindUser.getAccountType());
		assertEquals(pathmindUser.getFirstname(), savedPathmindUser.getFirstname());
		assertEquals(pathmindUser.getLastname(), savedPathmindUser.getLastname());
		assertEquals(pathmindUser.getAddress(), savedPathmindUser.getAddress());
		assertEquals(pathmindUser.getCity(), savedPathmindUser.getCity());
		assertEquals(pathmindUser.getState(), savedPathmindUser.getState());
		assertEquals(pathmindUser.getCountry(), savedPathmindUser.getCountry());
		assertEquals(pathmindUser.getZip(), savedPathmindUser.getZip());
		assertEquals(pathmindUser.getDeleteAt(), savedPathmindUser.getDeleteAt());
		assertEquals(pathmindUser.getEmailVerifiedAt(), savedPathmindUser.getEmailVerifiedAt());
		assertEquals(pathmindUser.getEmailVerificationToken(), savedPathmindUser.getEmailVerificationToken());
		assertEquals(pathmindUser.getProjects(), savedPathmindUser.getProjects());
	}

	@Test
	public void update()
	{
		PathmindUser pathmindUser = getNewPathmindUser();
		long pathmindUserId = userDAO.insertUser(pathmindUser);
		PathmindUser savedPathmindUser = userDAO.findById(pathmindUserId);
		changePathmindUser(savedPathmindUser);
		userDAO.update(savedPathmindUser);
		PathmindUser updatedPathmindUser = userDAO.findById(pathmindUserId);

		assertNotEquals(pathmindUser.getEmail(), updatedPathmindUser.getEmail());
		assertNotEquals(pathmindUser.getName(), updatedPathmindUser.getName());
		assertNotEquals(pathmindUser.getPassword(), updatedPathmindUser.getPassword());
		assertNotEquals(pathmindUser.getAccountType(), updatedPathmindUser.getAccountType());
		assertNotEquals(pathmindUser.getFirstname(), updatedPathmindUser.getFirstname());
		assertNotEquals(pathmindUser.getLastname(), updatedPathmindUser.getLastname());
		assertNotEquals(pathmindUser.getAddress(), updatedPathmindUser.getAddress());
		assertNotEquals(pathmindUser.getCity(), updatedPathmindUser.getCity());
		assertNotEquals(pathmindUser.getState(), updatedPathmindUser.getState());
		assertNotEquals(pathmindUser.getCountry(), updatedPathmindUser.getCountry());
		assertNotEquals(pathmindUser.getZip(), updatedPathmindUser.getZip());
		assertNotEquals(pathmindUser.getDeleteAt(), updatedPathmindUser.getDeleteAt());
		assertNotEquals(pathmindUser.getEmailVerifiedAt(), updatedPathmindUser.getEmailVerifiedAt());
		assertNotEquals(pathmindUser.getEmailVerificationToken(), updatedPathmindUser.getEmailVerificationToken());
		assertEquals(pathmindUser.getProjects(), updatedPathmindUser.getProjects());

		assertEquals(savedPathmindUser.getEmail(), updatedPathmindUser.getEmail());
		assertEquals(savedPathmindUser.getName(), updatedPathmindUser.getName());
		assertEquals(savedPathmindUser.getPassword(), updatedPathmindUser.getPassword());
		assertEquals(savedPathmindUser.getAccountType(), updatedPathmindUser.getAccountType());
		assertEquals(savedPathmindUser.getFirstname(), updatedPathmindUser.getFirstname());
		assertEquals(savedPathmindUser.getLastname(), updatedPathmindUser.getLastname());
		assertEquals(savedPathmindUser.getAddress(), updatedPathmindUser.getAddress());
		assertEquals(savedPathmindUser.getCity(), updatedPathmindUser.getCity());
		assertEquals(savedPathmindUser.getState(), updatedPathmindUser.getState());
		assertEquals(savedPathmindUser.getCountry(), updatedPathmindUser.getCountry());
		assertEquals(savedPathmindUser.getZip(), updatedPathmindUser.getZip());
		assertEquals(savedPathmindUser.getDeleteAt(), updatedPathmindUser.getDeleteAt());
		assertEquals(savedPathmindUser.getEmailVerifiedAt(), updatedPathmindUser.getEmailVerifiedAt());
		assertEquals(savedPathmindUser.getEmailVerificationToken(), updatedPathmindUser.getEmailVerificationToken());
		assertEquals(savedPathmindUser.getProjects(), updatedPathmindUser.getProjects());
	}

	@Test
	public void testInsertTransactionRollback()
	{
		PathmindUser pathmindUser = getNewPathmindUser();
		userDAO.insertUser(pathmindUser);
		int count = dslContext.fetchCount(PATHMIND_USER);
		boolean rollback = false;

		try {
			// we try to add a user with the same email again which results in a constraint violation
			userDAO.insertUser(pathmindUser);
			Assert.fail();
		} catch (DataAccessException ignore) {
			rollback = true;
		}

		assertTrue(rollback);
	}

	@Test
	public void testUpdateTransactionRollback()
	{
		PathmindUser pathmindUser = getNewPathmindUser();
		userDAO.insertUser(pathmindUser);

		PathmindUser updatedPathmindUser = getNewPathmindUser();
		changePathmindUser(updatedPathmindUser);
		userDAO.insertUser(updatedPathmindUser);
		boolean rollback = false;

		try {
			updatedPathmindUser.setEmail(pathmindUser.getEmail());
			userDAO.update(updatedPathmindUser);
			Assert.fail();
		} catch (DataAccessException ignore) {
			rollback = true;
		}

		assertTrue(rollback);
	}

}
