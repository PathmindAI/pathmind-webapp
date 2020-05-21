package io.skymind.pathmind.webapp.api;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringVaadinSession;
import io.skymind.pathmind.webapp.ActiveSessionsRegistry;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VersionController {

	public static final String ATTRIBUTE_VAADIN_SPRING_SERVLET = "com.vaadin.flow.server.VaadinSession.springServlet";

	private final ActiveSessionsRegistry activeSessionsRegistry;

	public VersionController(ActiveSessionsRegistry activeSessionsRegistry) {
		this.activeSessionsRegistry = activeSessionsRegistry;
	}

	/**
	 * This REST API is used to notify current active users of the application that a newer version
	 * of the app is available.  A notification is shown for those active users to tell to sign out
	 * and sign in to use the latest version.
	 */
	@PostMapping(value = "/api/newVersionAvailable")
	public Response newVersionAvailable() {
		Collection<HttpSession> activeAuthenticatedSessions = activeSessionsRegistry.getActiveAuthenticatedSessions();
		AtomicInteger count = new AtomicInteger(0);
		for (HttpSession activeSession : activeAuthenticatedSessions) {

			SpringVaadinSession springVaadinSession = (SpringVaadinSession) activeSession.getAttribute(ATTRIBUTE_VAADIN_SPRING_SERVLET);
			if (springVaadinSession != null) {
				springVaadinSession.access(() -> {
					for (UI ui : springVaadinSession.getUIs()) {
						try {
							UI.setCurrent(ui);
							showNotification();
							count.incrementAndGet();
						} finally {
							UI.setCurrent(null);
						}
					}
				});
			}
		}

		String message = String.format("Sent a new version available message to %s HTTP sessions and %s Vaadin UIs", activeAuthenticatedSessions.size(), count.get());
		log.info(message);
		return new Response(message);
	}

	private void showNotification() {
		String text = "Pathmind has been updated. Please log in again to get the latest improvements.";
		NotificationUtils.showPersistentNotification(text);
	}
}
