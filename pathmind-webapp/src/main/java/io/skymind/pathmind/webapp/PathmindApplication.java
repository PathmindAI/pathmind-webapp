package io.skymind.pathmind.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication(scanBasePackages = "io.skymind.pathmind", exclude = ErrorMvcAutoConfiguration.class)
@PropertySource({"classpath:application.properties", "classpath:shared.properties"})
@EnableCaching
@EnableScheduling
public class PathmindApplication {

    public static void main(String[] args) {
        SpringApplication.run(PathmindApplication.class, args);
    }

    @Bean
    ActiveSessionsRegistry activeSessionsRegistry() {
        return new ActiveSessionsRegistry();
    }

    @Bean
    public ServletListenerRegistrationBean<ActiveSessionsRegistry> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(activeSessionsRegistry());
    }
}
