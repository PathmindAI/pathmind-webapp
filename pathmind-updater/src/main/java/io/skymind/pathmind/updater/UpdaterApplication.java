package io.skymind.pathmind.updater;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
//@PropertySource({"application.properties", "shared.properties"})
public class UpdaterApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(UpdaterApplication.class, args);
//	}
//
//	@Bean
//	public ExecutorService checkerExecutorService(@Value("${pathmind.filecheck.poolsize}") int poolSize) {
//		return Executors.newFixedThreadPool(poolSize);
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Primary
//	@Bean
//	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//		objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
//
//		return objectMapper;
//	}
}
