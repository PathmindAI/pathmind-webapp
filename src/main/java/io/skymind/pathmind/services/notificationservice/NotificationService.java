package io.skymind.pathmind.services.notificationservice;

import com.sendgrid.helpers.mail.Mail;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	public void sendVerificationEmail(PathmindUser pathmindUser)
	{
		if (!isEmailSendingEnabled) {
			return;
		}
		if (pathmindUser.getEmailVerifiedAt() != null) {
			return;
		}
		final String emailVerificationLink = createEmailVerificationLink(pathmindUser);
		final Mail verificationEmail = mailHelper.createVerificationEmail(pathmindUser.getEmail(), pathmindUser.getName(), emailVerificationLink);
		mailHelper.sendMail(verificationEmail);
	}

	private String createEmailVerificationLink(PathmindUser pathmindUser)
	{
		return applicationURL + verificationRoute + pathmindUser.getEmailVerificationToken();
	}

}
