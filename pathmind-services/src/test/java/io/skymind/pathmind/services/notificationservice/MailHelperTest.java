package io.skymind.pathmind.services.notificationservice;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.exception.PathMindException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MailHelperTest
{

	private MailHelper mailHelper;

	@Before
	public void setup()
	{
		mailHelper = new MailHelper();
	}

	@Test
	public void createVerificationEmail() throws PathMindException
	{
		PathmindUser pathmindUser = new PathmindUser();
		final String test_email = "test email";
		pathmindUser.setEmail(test_email);
		final UUID emailVerificationToken = UUID.randomUUID();
		String emailLink = "http://testurl/verify" + emailVerificationToken.toString();
		pathmindUser.setEmailVerificationToken(emailVerificationToken);
		pathmindUser.setFirstname("Test");
		pathmindUser.setLastname("User");

		final Mail verificationEmail = mailHelper.createVerificationEmail(pathmindUser.getEmail(), pathmindUser.getName(), emailLink);
		final Personalization personalization = verificationEmail.getPersonalization().get(0);
		final String name = (String) personalization.getDynamicTemplateData().get("name");
		final String emailVerificationLink = (String) personalization.getDynamicTemplateData().get("emailVerificationLink");

		assertEquals("Test User", name);
		assertEquals(emailLink, emailVerificationLink);
	}


	@Test(expected = PathMindException.class)
	public void createVerificationEmail_Fail() throws PathMindException
	{
		PathmindUser pathmindUser = new PathmindUser();
		final UUID emailVerificationToken = UUID.randomUUID();
		String emailLink = "http://testurl/verify" + emailVerificationToken.toString();
		pathmindUser.setEmailVerificationToken(emailVerificationToken);

		mailHelper.createVerificationEmail(pathmindUser.getEmail(), pathmindUser.getName(), emailLink);
	}

	@Test(expected = PathMindException.class)
	public void createVerificationEmail_Fail2() throws PathMindException
	{
		mailHelper.createVerificationEmail(null, null, null);
	}

	@Test
	public void createResetPasswordEmail() throws PathMindException
	{
		final String test_email = "test email";
		final UUID emailVerificationToken = UUID.randomUUID();
		String emailLink = "http://testurl/verify" + emailVerificationToken.toString();
		final String test_user = "Test User";
		final String hours = "48";

		final Mail verificationEmail = mailHelper.createResetPasswordEmail(test_email, test_user, emailLink, hours);
		final Personalization personalization = verificationEmail.getPersonalization().get(0);
		final String name = (String) personalization.getDynamicTemplateData().get("name");
		final String resetPasswordLink = (String) personalization.getDynamicTemplateData().get("resetPasswordLink");
		final String emailHours = (String) personalization.getDynamicTemplateData().get("hours");

		assertEquals(test_user, name);
		assertEquals(emailLink, resetPasswordLink);
		assertEquals(hours, emailHours);
	}

}
