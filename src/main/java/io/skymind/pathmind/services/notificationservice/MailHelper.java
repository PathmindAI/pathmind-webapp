package io.skymind.pathmind.services.notificationservice;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MailHelper
{

	private static Logger log = LogManager.getLogger(MailHelper.class);

	private final String verificationEmailTemplateId = "d-2200af3c4c2e4a8d861ea882958df7b4";
	public static final String pathmind_verification_email = "Pathmind verification email";

	@Value("${sendgrid.api.key}")
	private String apiKey;

	@Value("${pathmind.email.from}")
	private String from;

	public void sendMail(Mail mail)
	{
		SendGrid sg = new SendGrid(apiKey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			log.info(response.getStatusCode() + response.getBody() + response.getHeaders());
		} catch (IOException e) {
			log.warn(ExceptionUtils.getStackTrace(e));
		}
	}

	public Mail createVerificationEmail(String to, String name, String emailVerificationLink)
	{
		Mail mail = new Mail();
		mail.setFrom(new Email(from));
		mail.setTemplateId(verificationEmailTemplateId);

		Personalization personalization = new Personalization();
		personalization.addDynamicTemplateData("subject", pathmind_verification_email);
		personalization.addDynamicTemplateData("name", name);
		personalization.addDynamicTemplateData("emailVerificationLink", emailVerificationLink);
		personalization.addTo(new Email(to));
		mail.addPersonalization(personalization);
		return mail;
	}

}
