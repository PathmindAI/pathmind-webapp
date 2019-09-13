package io.skymind.pathmind.services.experiment;

import com.vaadin.flow.spring.annotation.UIScope;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.ExperimentUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.FakeDataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.UnicastProcessor;

import java.util.Random;

@UIScope
@Service
public class ExperimentRunService
{
	private static final Logger log = LogManager.getLogger(ExperimentRunService.class);

	private static final Random RANDOM = new Random();

	// TODO -> We probably want to inject (autowire) the publisher but I'll leave that to you since I'm not sure of the details of this service.
	// TODO -> Do we also want a StatusUpdater here? What happens if there is an error?
	// TODO -> Do we want to be sent error notifications to an email account, etc?
	// TODO -> Service needs to check if an experiment is already being currently run.
	public static void fullRun(Experiment experiment, UnicastProcessor<PathmindBusEvent> publisher) {
		new Thread(() -> {
				try {
					for(int x=0; x<20; x++) {
						// TODO -> Implement
						experiment.getScores().add(RANDOM.nextInt(FakeDataUtils.EXPERIMENT_SCORE_MAX));
						publisher.onNext(new ExperimentUpdateBusEvent(experiment));
						log.info("Update experiment score");
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
