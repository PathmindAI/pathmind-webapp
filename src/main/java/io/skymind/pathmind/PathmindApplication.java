package io.skymind.pathmind;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.local.InstallLibraries;
import io.skymind.pathmind.services.TrainingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class PathmindApplication
{
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PathmindApplication.class, args);
		TrainingService ts = context.getBean(TrainingService.class);
		context.getBean(InstallLibraries.class).CheckLibraries(ts.getExecutionEnvironment());
	}

	@Bean
	UnicastProcessor<PathmindBusEvent> publisher() {
		return UnicastProcessor.create();
	}
	@Bean
	Flux<PathmindBusEvent> consumer(UnicastProcessor<PathmindBusEvent> publisher) {
		return publisher.replay(30).autoConnect();
	}

	@Bean
	public ExecutorService checkerExecutorService(@Value("${pathmind.filecheck.poolsize}") int poolSize) {
			return Executors.newFixedThreadPool(poolSize);
	}
}
