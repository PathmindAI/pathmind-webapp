package io.skymind.pathmind;

import io.skymind.pathmind.bus.PathmindBusEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	/**
	 * The password encoder to use when encrypting passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
