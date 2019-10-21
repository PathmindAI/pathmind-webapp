package io.skymind.pathmind.services;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.testutils.UserUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.*;

@Ignore
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


	@Test
	public void validatePassword() {
		assertTrue( userService.validatePassword("password", "password").contains(userService.UPPERCASE_MISSING));
		assertTrue( userService.validatePassword("password", "password1").contains(userService.NOT_MATCHING));
		assertTrue( userService.validatePassword("PASS", "PASS").contains(userService.LOWERCASE_MISSING));
		assertTrue( userService.validatePassword("PASS", "PASS").contains(userService.TOO_SHORT));
		assertTrue( userService.validatePassword("Password", "Password").size() == 0);
	}
}
