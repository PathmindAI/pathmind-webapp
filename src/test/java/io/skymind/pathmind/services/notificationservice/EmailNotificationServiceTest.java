package io.skymind.pathmind.services.notificationservice;

import io.skymind.pathmind.PathmindApplicationTests;
import io.skymind.pathmind.data.PathmindUser;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Ignore
public class EmailNotificationServiceTest extends PathmindApplicationTests
{

	@Value("${test.email.address}")
	private String testEmail;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Test
	public void sendVerificationEmail()
	{
		PathmindUser pathmindUser = new PathmindUser();
		pathmindUser.setEmail(testEmail);
		pathmindUser.setEmailVerificationToken(UUID.randomUUID());
		pathmindUser.setName("Test User");
		emailNotificationService.sendVerificationEmail(pathmindUser);
	}

	@Test(expected = NullPointerException.class)
	public void sendVerificationEmail_Fail()
	{
		emailNotificationService.sendVerificationEmail(null);
	}

	@Test
	public void sendResetPasswordEmail()
	{
		PathmindUser pathmindUser = new PathmindUser();
		pathmindUser.setEmail(testEmail);
		pathmindUser.setEmailVerificationToken(UUID.randomUUID());
		pathmindUser.setName("Test User");
		emailNotificationService.sendResetPasswordEmail(pathmindUser);
	}

	@Test(expected = NullPointerException.class)
	public void sendResetPasswordEmail_Fail()
	{
		emailNotificationService.sendResetPasswordEmail(null);
	}

}
