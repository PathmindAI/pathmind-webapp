package io.skymind.pathmind.services.notificationservice;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.skymind.pathmind.data.PathmindUser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
	public void createVerificationEmail() throws IOException
	{
		PathmindUser pathmindUser = new PathmindUser();
		final String test_email = "test email";
		pathmindUser.setEmail(test_email);
		final UUID emailVerificationToken = UUID.randomUUID();
		String emailLink = "http://testurl/verify" + emailVerificationToken.toString();
		pathmindUser.setEmailVerificationToken(emailVerificationToken);
		final String test_user = "Test User";
		pathmindUser.setName(test_user);

		final Mail verificationEmail = mailHelper.createVerificationEmail(pathmindUser.getEmail(), pathmindUser.getName(), emailLink);
		final Personalization personalization = verificationEmail.getPersonalization().get(0);
		final String subject = (String) personalization.getDynamicTemplateData().get("subject");
		final String name = (String) personalization.getDynamicTemplateData().get("name");
		final String emailVerificationLink = (String) personalization.getDynamicTemplateData().get("emailVerificationLink");

		assertEquals(MailHelper.pathmind_verification_email, subject);
		assertEquals(test_user, name);
		assertEquals(emailLink, emailVerificationLink);
	}
}
