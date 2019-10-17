package io.skymind.pathmind.services.notificationservice;

import com.sendgrid.helpers.mail.Mail;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.PathMindException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationService
{

	private static Logger log = LogManager.getLogger(NotificationService.class);
	private final String verificationRoute = "/verify/";

	@Value("${pathmind.email-sending.enabled}")
	private boolean isEmailSendingEnabled;

	@Value("${pathmind.application.url}")
	private String applicationURL;

	private UserDAO userDAO;
	private MailHelper mailHelper;

	@Autowired
	public NotificationService(UserDAO userDAO, MailHelper mailHelper)
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
		return applicationURL + verificationRoute + pathmindUser.getEmailVerificationToken();
	}

}
