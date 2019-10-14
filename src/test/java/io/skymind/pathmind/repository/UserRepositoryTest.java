package io.skymind.pathmind.repository;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserRepositoryTest extends PathmindApplicationTests
{

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	public void findEmailIgnoreCase()
	{
		PathmindUser pathmindUser = userRepository.findByEmailIgnoreCase("STEPH@followsteph.com");
		assertEquals("steph@followsteph.com", pathmindUser.getEmail());
	}

	@Test
	public void findById()
	{
		PathmindUser pathmindUser = userRepository.findByEmailIgnoreCase("steph@followsteph.com");
		PathmindUser byId = userRepository.findById(pathmindUser.getId());
		assertEquals(pathmindUser.getEmail(), byId.getEmail());
	}

	@Test
	public void changePassword()
	{
		PathmindUser pathmindUser = userRepository.findByEmailIgnoreCase("steph@followsteph.com");
		String myNewPassw0rd1 = "myNewPassw0rd1";
		boolean isPasswordChanged = userRepository.changePassword(pathmindUser.getId(), myNewPassw0rd1);
		Assert.assertTrue(isPasswordChanged);

		pathmindUser = userRepository.findByEmailIgnoreCase("steph@followsteph.com");
		assertNotEquals(pathmindUser.getPassword(), myNewPassw0rd1);
		passwordEncoder.matches(myNewPassw0rd1, pathmindUser.getPassword());
	}

}
