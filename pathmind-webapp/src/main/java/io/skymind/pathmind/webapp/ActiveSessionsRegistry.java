package io.skymind.pathmind.webapp;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * Holds information regarding active HTTP Sessions on the application.
 */
public class ActiveSessionsRegistry implements HttpSessionListener {

	Map<String, HttpSession> activeSessions = new ConcurrentHashMap<>();

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession httpSession = se.getSession();
		activeSessions.put(httpSession.getId(), httpSession);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		String id = se.getSession().getId();
		activeSessions.remove(id);
	}

	public Collection<HttpSession> getActiveAuthenticatedSessions() {
		return activeSessions.values().stream()
			.filter(this::hasSpringSecurityContext)
			.collect(Collectors.toList());
	}

	private boolean hasSpringSecurityContext(HttpSession session) {
		try {
			return session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) != null;
		} catch (IllegalStateException  e) {
			return false;
		}
	}
}
