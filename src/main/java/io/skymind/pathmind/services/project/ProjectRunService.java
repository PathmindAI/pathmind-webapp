package io.skymind.pathmind.services.project;

import io.skymind.pathmind.bus.data.ProjectUpdateStatus;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.UnicastProcessor;

import java.util.Random;

public class ProjectRunService
{
	private static final Logger log = LogManager.getLogger(ProjectRunService.class);

	private static final Random RANDOM = new Random();

	// TODO -> We probably want to inject (autowire) the publisher but I'll leave that to you since I'm not sure of the details of this service.
	// TODO -> Do we also want a StatusUpdater here? What hapepns if there is an error?
	// TODO -> Do we want to be sent error notifications to an email account, etc?
	public static void fullRun(long projectId, UnicastProcessor<ProjectUpdateStatus> publisher) {
		new Thread(() -> {
				try {
					for(int x=0; x<100; x++) {
						publisher.onNext(new ProjectUpdateStatus(projectId, RANDOM.nextInt(1000)));
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				} finally {
					log.info("fullRun : completed");
				}
		}).start();
	}
}
