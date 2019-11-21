package io.skymind.pathmind.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

	private static final Logger log = LoggerFactory.getLogger(VersionController.class);

	/**
	 * This REST API can be used to notify current active users of the application that a newer version
	 * of the app is available. E.g. a banner or a notification can be shown for those active users
	 * to tell to sign out and sign in to use the latest version.
	 */
	@PostMapping(value = "/api/newVersionAvailable")
	public String newVersionAvailable() {
		log.info("/api/newVersionAvailable has been called successfully");
		return "{}";
	}
}
