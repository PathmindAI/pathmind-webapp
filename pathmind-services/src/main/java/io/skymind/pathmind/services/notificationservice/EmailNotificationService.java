package io.skymind.pathmind.services.notificationservice;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.helpers.mail.Mail;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.shared.exception.PathMindException;
import io.skymind.pathmind.shared.security.Routes;
import lombok.extern.slf4j.Slf4j;

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

	/**
	 * Sends training completed email to a Pathmind user.
	 *
	 * @param pathmindUser
	 */
	public void sendTrainingCompletedEmail(PathmindUser pathmindUser, Experiment experiment, Project project, boolean isSuccessful)
	{
		Objects.requireNonNull(pathmindUser);
		if (!isEmailSendingEnabled) {
			log.info("Email sending has been disabled, not sending the email to: " + pathmindUser.getEmail());
			return;
		}
		
		final String experimentPageLink = createExperimentPageLink(experiment);
		Mail trainingCompletedMail;
		try {
			String username = StringUtils.isBlank(pathmindUser.getName()) ? pathmindUser.getEmail() : StringUtils.capitalize(pathmindUser.getName());
			trainingCompletedMail = mailHelper.createTrainingCompletedEmail(pathmindUser.getEmail(), username, project.getName(), experimentPageLink, isSuccessful);
		} catch (PathMindException e) {
			log.warn("Could not create email due to missing data in the PathmindUser object");
			return;
		}
		mailHelper.sendMail(trainingCompletedMail);
	}

	

	private String createExperimentPageLink(Experiment experiment) {
		return applicationURL + "/" + Routes.EXPERIMENT_URL + "/" + experiment.getId();
	}

	// DH -> Should we decide to add email notifications for special exceptions then we just need to replace
	// the log entries below with mailHelper and set it up according to our preferences.
	public void sendEmailExceptionNotification(Throwable t) {
		// Example
		log.error("Subject: Exception: " + t.getMessage());
		log.error("to: " + "default email address setup in application.properties");
		log.error("Message:" + ExceptionUtils.getStackTrace(t));
	}

	public void sendEmailExceptionNotification(String title, Throwable t) {
		// Example
		log.error("Subject: Exception: " + title);
		log.error("to: " + "default email address setup in application.properties");
		log.error("Message:" + ExceptionUtils.getStackTrace(t));
	}

	public void sendEmailErrorNotification(String title, String message) {
		// Example
		log.error("Subject: Exception: " + title);
		log.error("to: " + "default email address setup in application.properties");
		log.error("Message:" + message);
	}
}
