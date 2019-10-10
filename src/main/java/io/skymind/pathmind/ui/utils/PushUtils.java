package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PushUtils
{
	private static Logger log = LogManager.getLogger(PushUtils.class);

	public static void push(UI ui, Command command) {
		if(ui == null) {
			log.error("-------> PUSH FAILED");
			return;
		}
		log.info("About to push by using push mode {}", ui.getPushConfiguration().getPushMode());
		ui.access(command);
	}
}
