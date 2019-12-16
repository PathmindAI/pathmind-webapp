package io.skymind.pathmind.repository;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Ignore
public class UserSQLTest extends PathmindApplicationTests
{
	@Autowired
	UserDAO userDAO;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	public void findEmailIgnoreCase()
	{
		PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase("STEPH@followsteph.com");
		assertEquals("steph@followsteph.com", pathmindUser.getEmail());
	}

	@Test
	public void findById()
	{
		PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase("steph@followsteph.com");
		PathmindUser byId = userDAO.findById(pathmindUser.getId());
		assertEquals(pathmindUser.getEmail(), byId.getEmail());
	}

	@Test
	public void changePassword()
	{
		PathmindUser pathmindUser = userDAO.findByEmailIgnoreCase("steph@followsteph.com");
		String myNewPassw0rd1 = "myNewPassw0rd1";
		boolean isPasswordChanged = userDAO.changePassword(pathmindUser.getId(), myNewPassw0rd1);
		Assert.assertTrue(isPasswordChanged);

		pathmindUser = userDAO.findByEmailIgnoreCase("steph@followsteph.com");
		assertNotEquals(pathmindUser.getPassword(), myNewPassw0rd1);
		passwordEncoder.matches(myNewPassw0rd1, pathmindUser.getPassword());
	}

}
