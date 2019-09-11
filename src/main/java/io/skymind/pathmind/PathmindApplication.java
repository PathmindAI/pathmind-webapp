package io.skymind.pathmind;

import io.skymind.pathmind.bus.PathmindBusEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@SpringBootApplication
public class PathmindApplication
{
	public static void main(String[] args) {
		SpringApplication.run(PathmindApplication.class, args);
	}

	@Bean
	UnicastProcessor<PathmindBusEvent> publisher() {
		return UnicastProcessor.create();
	}
	@Bean
	Flux<PathmindBusEvent> consumer(UnicastProcessor<PathmindBusEvent> publisher) {
		return publisher.replay(30).autoConnect();
	}
}
