package io.skymind.pathmind.repository;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends PathmindApplicationTests
{

	@Autowired
	UserRepository userRepository;

	@Test
	public void findEmailIgnoreCase()
	{
		PathmindUser pathmindUser = userRepository.findByEmailIgnoreCase("STEPH@followsteph.com");
		Assert.assertEquals("steph@followsteph.com", pathmindUser.getEmail());
	}

	@Test
	public void findById()
	{
		PathmindUser pathmindUser = userRepository.findByEmailIgnoreCase("steph@followsteph.com");
		PathmindUser byId = userRepository.findById(pathmindUser.getId());
		Assert.assertEquals(pathmindUser.getEmail(), byId.getEmail());
	}

}
