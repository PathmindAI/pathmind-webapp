package io.skymind.pathmind.services.notificationservice;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import io.skymind.pathmind.exception.PathMindException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailHelper
{
	public static final String PATHMIND_VERIFICATION_EMAIL_SUBJECT = "Pathmind verification email";
	public static final String PATHMIND_RESET_PASSWORD_EMAIL_SUBJECT = "Pathmind reset password email";
	public static final String PATHMIND_TRAINING_COMPLETED_EMAIL_SUBJECT = "Pathmind training completed successfully email";
	public static final String PATHMIND_TRAINING_FAILED_EMAIL_SUBJECT = "Pathmind training failed email";

	@Value("${sendgrid.verification-mail.id}")
	private String verificationEmailTemplateId;

	@Value("${sendgrid.resetpassword-mail.id}")
	private String resetPasswordTemplateId;

	@Value("${sendgrid.trainingcompleted-mail.id}")
	private String trainingCompletedTemplateId;

	@Value("${sendgrid.trainingfailed-mail.id}")
	private String trainingFailedTemplateId;
	
	

	@Value("${sendgrid.api.key}")
	private String apiKey;

	@Value("${pathmind.email.from}")
	private String from;

	/**
	 * Sends an email using SendGrid
	 *
	 * @param mail The mail object that is used to send the mail.
	 */
	public void sendMail(Mail mail)
	{
		Objects.requireNonNull(mail);
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

	/**
	 * Creates the verification email
	 *
	 * @param to                    The email address of the mail recipient (the user)
	 * @param name                  The name of the user
	 * @param emailVerificationLink The email verification link that can be used to verify the user account
	 * @return The ready made Mail object
	 * @throws PathMindException Exception is thrown if any of the arguments is null or empty
	 */
	public Mail createVerificationEmail(String to, String name, String emailVerificationLink) throws PathMindException
	{
		if (StringUtils.isAnyEmpty(to, name, emailVerificationLink)) {
			throw new PathMindException("Email fields are missing");
		}
		Mail mail = new Mail();
		mail.setFrom(new Email(from));
		mail.setTemplateId(verificationEmailTemplateId);

		Personalization personalization = new Personalization();
		personalization.addDynamicTemplateData("subject", PATHMIND_VERIFICATION_EMAIL_SUBJECT);
		personalization.addDynamicTemplateData("name", name);
		personalization.addDynamicTemplateData("emailVerificationLink", emailVerificationLink);
		personalization.addTo(new Email(to));
		mail.addPersonalization(personalization);
		return mail;
	}

	public Mail createResetPasswordEmail(String to, String name, String resetPasswordLink, String hours) throws PathMindException
	{
		if (StringUtils.isAnyEmpty(to, name, resetPasswordLink, hours)) {
			throw new PathMindException("Email fields are missing");
		}
		Mail mail = new Mail();
		mail.setFrom(new Email(from));
		mail.setTemplateId(resetPasswordTemplateId);

		Personalization personalization = new Personalization();
		personalization.addDynamicTemplateData("subject", PATHMIND_RESET_PASSWORD_EMAIL_SUBJECT);
		personalization.addDynamicTemplateData("name", name);
		personalization.addDynamicTemplateData("resetPasswordLink", resetPasswordLink);
		personalization.addDynamicTemplateData("hours", hours);
		personalization.addTo(new Email(to));
		mail.addPersonalization(personalization);
		return mail;
	}

	/**
	 * Creates training completed notification mail
	 *
	 * @param to                    The email address of the mail recipient (the user)
	 * @param name                  The name of the user
	 * @param projectName           The name of the project
	 * @param experimentPageLink 	The link to the experiments page
	 * @param isSuccessful 			The training is completed successfully or failed
	 * @return The ready made Mail object
	 * @throws PathMindException Exception is thrown if any of the arguments is null or empty
	 */
	public Mail createTrainingCompletedEmail(String to, String name, String projectName, String experimentPageLink, boolean isSuccessful) throws PathMindException
	{
		if (StringUtils.isAnyEmpty(to, name, projectName, experimentPageLink)) {
			throw new PathMindException("Email fields are missing");
		}
		Mail mail = new Mail();
		mail.setFrom(new Email(from));
		mail.setTemplateId(isSuccessful ? trainingCompletedTemplateId : trainingFailedTemplateId);
		
		Personalization personalization = new Personalization();
		personalization.addDynamicTemplateData("subject", isSuccessful ? PATHMIND_TRAINING_COMPLETED_EMAIL_SUBJECT : PATHMIND_TRAINING_FAILED_EMAIL_SUBJECT);
		personalization.addDynamicTemplateData("name", name);
		personalization.addDynamicTemplateData("projectName", projectName);
		personalization.addDynamicTemplateData("experimentPageLink", experimentPageLink);
		personalization.addTo(new Email(to));
		mail.addPersonalization(personalization);
		return mail;
	}
}
