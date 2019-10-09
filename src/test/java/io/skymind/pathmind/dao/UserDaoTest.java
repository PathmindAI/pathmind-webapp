package io.skymind.pathmind.dao;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.skymind.pathmind.data.db.Tables.PATHMIND_USER;
import static org.junit.Assert.*;

public class UserDaoTest extends PathmindApplicationTests
{

	@Autowired
	UserDAO userDAO;
	@Autowired
	DSLContext dslContext;

	private final String email1 = "email@email.com";
	private final String email2 = "email2@email.com";

	@Test
	public void insertAndDeleteUser()
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

		// delete the test entity and check it was deleted
		userDAO.delete(pathmindUserId);
		PathmindUser deletedPathmindUser = userDAO.findById(pathmindUserId);
		assertNull(deletedPathmindUser);
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

		// delete the test entity and check it was deleted
		userDAO.delete(pathmindUserId);
		PathmindUser deletedPathmindUser = userDAO.findById(pathmindUserId);
		assertNull(deletedPathmindUser);
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

		assertEquals(count, dslContext.fetchCount(PATHMIND_USER));
		assertTrue(rollback);

		// cleanup
		userDAO.delete(pathmindUser.getId());
	}

	@Test
	public void testUpdateTransactionRollback()
	{
		PathmindUser pathmindUser = getNewPathmindUser();
		userDAO.insertUser(pathmindUser);

		PathmindUser updatedPathmindUser = getNewPathmindUser();
		changePathmindUser(updatedPathmindUser);
		userDAO.insertUser(updatedPathmindUser);
		int count = dslContext.fetchCount(PATHMIND_USER);
		boolean rollback = false;

		try {
			updatedPathmindUser.setEmail(pathmindUser.getEmail());
			userDAO.update(updatedPathmindUser);
			Assert.fail();
		} catch (DataAccessException ignore) {
			rollback = true;
		}

		updatedPathmindUser = userDAO.findById(updatedPathmindUser.getId());
		assertEquals(email2, updatedPathmindUser.getEmail());
		assertTrue(rollback);

		// cleanup
		userDAO.delete(pathmindUser.getId());
	}

	private PathmindUser getNewPathmindUser()
	{
		PathmindUser pathmindUser = new PathmindUser();
		pathmindUser.setName("Name");
		pathmindUser.setEmail(email1);
		pathmindUser.setPassword("Password");
		pathmindUser.setAccountType(1);
		pathmindUser.setFirstname("Firstname");
		pathmindUser.setLastname("Lastname");
		pathmindUser.setAddress("Address");
		pathmindUser.setCity("City");
		pathmindUser.setState("State");
		pathmindUser.setCountry("Country");
		pathmindUser.setZip("Zip");
		pathmindUser.setDeleteAt(LocalDateTime.now());
		pathmindUser.setEmailVerifiedAt(LocalDateTime.now());
		pathmindUser.setEmailVerificationToken(UUID.randomUUID());
		return pathmindUser;
	}

	private void changePathmindUser(PathmindUser pathmindUser)
	{
		pathmindUser.setName("Name2");
		pathmindUser.setEmail(email2);
		pathmindUser.setPassword("Password2");
		pathmindUser.setAccountType(2);
		pathmindUser.setFirstname("Firstname2");
		pathmindUser.setLastname("Lastname2");
		pathmindUser.setAddress("Address2");
		pathmindUser.setCity("City2");
		pathmindUser.setState("State2");
		pathmindUser.setCountry("Country2");
		pathmindUser.setZip("Zip2");
		pathmindUser.setDeleteAt(LocalDateTime.now());
		pathmindUser.setEmailVerifiedAt(LocalDateTime.now());
		pathmindUser.setEmailVerificationToken(UUID.randomUUID());
	}

}
