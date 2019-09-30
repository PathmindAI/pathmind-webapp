package io.skymind.pathmind.services.run;

import com.vaadin.flow.spring.annotation.UIScope;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.FakeDataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.UnicastProcessor;

import java.util.Random;

@UIScope
@Service
public class RunService
{
	private static final Logger log = LogManager.getLogger(RunService.class);

	private static final Random RANDOM = new Random();

	// TODO -> We probably want to inject (autowire) the publisher but I'll leave that to you since I'm not sure of the details of this service.
	// TODO -> Do we also want a StatusUpdater interface here? What happens if there is an error? Does an event get fired up to the UI?
	public static void run(Policy policy, UnicastProcessor<PathmindBusEvent> publisher) {
		new Thread(() -> {
				try {
					for(int x=0; x<20; x++) {
						// TODO -> Implement
						policy.getScores().add(RANDOM.nextInt(FakeDataUtils.EXPERIMENT_SCORE_MAX));
						publisher.onNext(new PolicyUpdateBusEvent(policy));
						log.info("Update policy score");
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				} finally {
					log.info("run completed");
				}
		}).start();
	}
}
