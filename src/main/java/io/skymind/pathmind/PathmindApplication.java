package io.skymind.pathmind;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.services.project.ProjectFileCheckService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	@Value("${pathmind.filecheck.poolsize}")
	public String poolSize;

	@Bean
	public ExecutorService checkerExecutorService() {
		int threadPoolSize = Integer.parseInt(poolSize);
		return Executors.newFixedThreadPool(threadPoolSize);
	}
}
