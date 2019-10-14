package io.skymind.pathmind.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationLogger
{

	private static Logger log = LogManager.getLogger(AuthenticationLogger.class);

	@EventListener
	public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {

		AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
		log.info(auditEvent.getType() + " - Principal " + auditEvent.getPrincipal());

		WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");
		log.info("  Remote IP address: " + details.getRemoteAddress());
		log.info("  Session Id: " + details.getSessionId());
	}

}
