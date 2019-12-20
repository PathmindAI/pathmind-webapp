package io.skymind.pathmind.services.notificationservice;

import com.sendgrid.helpers.mail.Mail;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.PathMindException;
import io.skymind.pathmind.security.Routes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class EmailNotificationService
{

    @Value("${pathmind.reset.password.link.valid}")
    private int resetTokenValidHours;

	@Value("${pathmind.email-sending.enabled}")
	private boolean isEmailSendingEnabled;

	@Value("${pathmind.application.url}")
	private String applicationURL;

	@Value("${pathmind.server-issues-notifications.enabled}")
	private boolean isErrorEmailSendingEnabled;

	@Value("${pathmind.server-issues-notifications.email}")
	private String errorEmailAddress;

	private UserDAO userDAO;
	private MailHelper mailHelper;

	@Autowired
	public EmailNotificationService(UserDAO userDAO, MailHelper mailHelper)
	{
		this.userDAO = userDAO;
		this.mailHelper = mailHelper;
	}

	/**
	 * Sends a verification email to a Pathmind user.
	 * The email is only sent if email verification hasn't been yet been approved
	 *
	 * @param pathmindUser
	 */
	public void sendVerificationEmail(PathmindUser pathmindUser)
	{
		Objects.requireNonNull(pathmindUser);
		if (!isEmailSendingEnabled) {
			log.info("Email sending has been disabled, not sending the email to: " + pathmindUser.getEmail());
			return;
		}
		if (pathmindUser.getEmailVerifiedAt() != null) {
			log.info("Canceling verification email sending, user: " + pathmindUser.getEmail() + ", has already been verified");
			return;
		}
		if (pathmindUser.getEmailVerificationToken() == null) {
			pathmindUser.setEmailVerificationToken(UUID.randomUUID());
			userDAO.update(pathmindUser);
		}
		final String emailVerificationLink = createEmailVerificationLink(pathmindUser);
		Mail verificationEmail;
		try {
			verificationEmail = mailHelper.createVerificationEmail(pathmindUser.getEmail(), pathmindUser.getName(), emailVerificationLink);
		} catch (PathMindException e) {
			log.warn("Could not create email due to missing data in the PathmindUser object");
			return;
		}
		mailHelper.sendMail(verificationEmail);
	}

	private String createEmailVerificationLink(PathmindUser pathmindUser)
	{
		return applicationURL + "/" + Routes.EMAIL_VERIFICATION_URL + "/" + pathmindUser.getEmailVerificationToken();
	}

	/**
	 * Sends a reset password email to a Pathmind user.
	 *
	 * @param pathmindUser
	 */
	public void sendResetPasswordEmail(PathmindUser pathmindUser)
	{
		Objects.requireNonNull(pathmindUser);
		if (!isEmailSendingEnabled) {
			log.info("Email sending has been disabled, not sending the email to: " + pathmindUser.getEmail());
			return;
		}

		if (pathmindUser.getEmailVerifiedAt() != null || pathmindUser.getEmailVerificationToken() == null ) {
			pathmindUser.setEmailVerificationToken(UUID.randomUUID());
		}

		pathmindUser.setPasswordResetSendAt(LocalDateTime.now());
		userDAO.update(pathmindUser);


		final String resetPasswordLink = createResetPasswordLink(pathmindUser);
		Mail verificationEmail;
		try {
			String username = StringUtils.isBlank(pathmindUser.getName()) ? pathmindUser.getEmail() : pathmindUser.getName();
			verificationEmail = mailHelper.createResetPasswordEmail(pathmindUser.getEmail(), username, resetPasswordLink, "" + resetTokenValidHours);
		} catch (PathMindException e) {
			log.warn("Could not create email due to missing data in the PathmindUser object");
			return;
		}
		mailHelper.sendMail(verificationEmail);
	}

	private String createResetPasswordLink(PathmindUser pathmindUser)
	{
		return applicationURL + "/" + Routes.RESET_PASSWORD_URL + "/" + pathmindUser.getEmailVerificationToken();
	}

	// TODO - once we migrate to AWS and have EFK we should remove this
	// https://github.com/SkymindIO/pathmind-webapp/issues/594
	public void sendEmailExceptionNotification(Throwable t) {

		if (!isErrorEmailSendingEnabled) {
			log.info("Error Email sending has been disabled, not sending the email to: " + errorEmailAddress);
			log.info("Subject: Exception: " + t.getMessage());
			log.info("Message:" + ExceptionUtils.getStackTrace(t));
			return;
		}

		Mail errorEmail;
		try {
			String subject = "Exception: " + t.getMessage();
			String message =  ExceptionUtils.getStackTrace(t);
			errorEmail = mailHelper.createErrorEmail(errorEmailAddress, subject, message);
		} catch (PathMindException e) {
			log.warn("Could not send error notification email");
			return;
		}
		mailHelper.sendMail(errorEmail);
	}

	public void sendEmailExceptionNotification(String title, Throwable t) {

		if (!isErrorEmailSendingEnabled) {
			log.info("Error Email sending has been disabled, not sending the email to: " + errorEmailAddress);
			log.info("Subject: Exception: " + title);
			log.info("Message:" + ExceptionUtils.getStackTrace(t));
			return;
		}

		Mail errorEmail;
		try {
			String subject = "Exception: " + title;
			String message =  ExceptionUtils.getStackTrace(t);
			errorEmail = mailHelper.createErrorEmail(errorEmailAddress, subject, message);
		} catch (PathMindException e) {
			log.warn("Could not send error notification email");
			return;
		}
		mailHelper.sendMail(errorEmail);
	}

	public void sendEmailExceptionNotification(String title, String message) {


		if (!isErrorEmailSendingEnabled) {
			log.info("Error Email sending has been disabled, not sending the email to: " + errorEmailAddress);
			log.info("Subject: Exception: " + title);
			log.info("Message:" + message);
			return;
		}

		Mail errorEmail;
		try {
			String subject = "Exception: " + title;
			errorEmail = mailHelper.createErrorEmail(errorEmailAddress, subject, message);
		} catch (PathMindException e) {
			log.warn("Could not send error notification email");
			return;
		}
		mailHelper.sendMail(errorEmail);
	}
}
