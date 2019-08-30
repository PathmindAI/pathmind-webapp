package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProjectFileCheckService
{
	private static final Logger log = LogManager.getLogger(ProjectFileCheckService.class);

	// TODO -> Remove the showError flag, it's only for testing.
	public static void checkFile(StatusUpdater statusUpdater, boolean isShowError) {
		new Thread(() -> {
				try {
					for(int x=0; x<10; x++) {
						log.info("Checking : " + x*10 + "% done");
						statusUpdater.updateStatus(x/10D);
						if(isShowError && (x == 5 || x == 8)) {
							log.info("Error : " + x);
							statusUpdater.updateError("Error : " + x);
						}
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
					statusUpdater.updateError("File check interrupted.");
				} finally {
					log.info("Checking : completed");
					statusUpdater.done();
				}
		}).start();
	}
}
