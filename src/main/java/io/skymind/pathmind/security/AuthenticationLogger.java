package io.skymind.pathmind.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationLogger
{
	@EventListener
	public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {

		AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
		log.info(auditEvent.getType() + " - Principal " + auditEvent.getPrincipal());

		WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");
		log.info("  Remote IP address: " + details.getRemoteAddress());
		log.info("  Session Id: " + details.getSessionId());
	}

}
