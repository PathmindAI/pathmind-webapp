package io.skymind.pathmind.services;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.testutils.UserUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.*;

@Transactional
public class UserServiceTest extends PathmindApplicationTests
{

	@Autowired
	UserService userService;

	@Test
	public void signup() {
		PathmindUser pathmindUser = UserUtils.getNewPathmindUser();
		PathmindUser signedupUser = userService.signup(pathmindUser);

		assertNotNull(signedupUser.getEmailVerificationToken());
	}

	@Test
	public void verifyAccountFail() {
		PathmindUser pathmindUser = UserUtils.getNewPathmindUser();
		PathmindUser signedupUser = userService.signup(pathmindUser);

		boolean isVerified = userService.verifyAccount(signedupUser, UUID.randomUUID());

		assertFalse(isVerified);
	}

	@Test
	public void verifyAccountSuccess() {
		PathmindUser pathmindUser = UserUtils.getNewPathmindUser();
		PathmindUser signedupUser = userService.signup(pathmindUser);

		boolean isVerified = userService.verifyAccount(signedupUser, signedupUser.getEmailVerificationToken());

		assertTrue(isVerified);
	}

}
