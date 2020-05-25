package io.skymind.pathmind.webapp.security;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.db.testutils.UserTestUtils;
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
        PathmindUser pathmindUser = UserTestUtils.getNewPathmindUser();
        PathmindUser signedupUser = userService.signup(pathmindUser);

        assertNotNull(signedupUser.getEmailVerificationToken());
    }

    @Test
    public void verifyAccountFail() {
        PathmindUser pathmindUser = UserTestUtils.getNewPathmindUser();
        PathmindUser signedupUser = userService.signup(pathmindUser);

        boolean isVerified = userService.verifyAccount(signedupUser, UUID.randomUUID());

        assertFalse(isVerified);
    }

    @Test
    public void verifyAccountSuccess() {
        PathmindUser pathmindUser = UserTestUtils.getNewPathmindUser();
        PathmindUser signedupUser = userService.signup(pathmindUser);

        boolean isVerified = userService.verifyAccount(signedupUser, signedupUser.getEmailVerificationToken());

        assertTrue(isVerified);
    }

    @Test
    public void validatePassword() {
        assertTrue( userService.validatePassword("password", "password").getPasswordValidationErrors().contains(userService.UPPERCASE_MISSING));
        assertTrue( userService.validatePassword("password", "password1").getPasswordValidationErrors().contains(userService.NOT_MATCHING));
        assertTrue( userService.validatePassword("PASS", "PASS").getPasswordValidationErrors().contains(userService.LOWERCASE_MISSING));
        assertTrue( userService.validatePassword("Password", "").getPasswordValidationErrors().isEmpty());
        assertTrue( userService.validatePassword("Password", "").getConfirmPasswordValidationError().equals(userService.CONFIRMATION_REQUIRED));
        assertTrue( userService.validatePassword("PASS", "PASS").getPasswordValidationErrors().contains(userService.TOO_SHORT));
        assertTrue( userService.validatePassword("Password", "Password").getPasswordValidationErrors().isEmpty());
    }
}
