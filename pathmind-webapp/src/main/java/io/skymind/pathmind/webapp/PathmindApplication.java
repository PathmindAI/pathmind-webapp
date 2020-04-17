package io.skymind.pathmind.webapp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"application.properties", "shared.properties"})
@EnableCaching
public class PathmindApplication
{
	public static void main(String[] args) {
		SpringApplication.run(PathmindApplication.class, args);
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

	@Bean
	ActiveSessionsRegistry activeSessionsRegistry() {
		return new ActiveSessionsRegistry();
	}

	@Bean
	public ServletListenerRegistrationBean<ActiveSessionsRegistry> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<>(activeSessionsRegistry());
	}

	@Primary
	@Bean
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
		objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

		return objectMapper;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void lookForTransactionalAnnotations() {
		try {
			ClassPathScanningCandidateComponentProvider provider
					= new ClassPathScanningCandidateComponentProvider(true);
			List<String> allProblems = new ArrayList<>();
			for (BeanDefinition beanDefinition : provider.findCandidateComponents("io.skymind.pathmind")) {
				Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
				if (clazz.getAnnotation(Transactional.class) != null) {
					allProblems.add(clazz.getName());
				}
				allProblems.addAll(
						Stream.of(clazz.getDeclaredMethods())
								.filter(m -> m.getAnnotation(Transactional.class) != null)
								.map(m ->
										String.format("%s.%s(...)", clazz.getName(), m.getName())
								)
								.collect(Collectors.toList())
				);
			}
			if (!allProblems.isEmpty()) {
				throw new RuntimeException(
						"WE DON'T SUPPORT THE @Transactional ANNOTATION. FOR MORE DETAILS, SEE: " +
								"https://github.com/SkymindIO/pathmind-webapp/issues/531.\n"
								+ "Places using @Transactional:\n"
								+ String.join("\n", allProblems)

				);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
