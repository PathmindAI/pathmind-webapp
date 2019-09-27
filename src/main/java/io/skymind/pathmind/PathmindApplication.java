package io.skymind.pathmind;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

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

	@Bean
	public ExecutorService checkerExecutorService(@Value("${pathmind.filecheck.poolsize}") int poolSize) {
			return Executors.newFixedThreadPool(poolSize);
	}

	@Bean
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		return ObjectMapperHolder.getJsonMapper();
	}
}
