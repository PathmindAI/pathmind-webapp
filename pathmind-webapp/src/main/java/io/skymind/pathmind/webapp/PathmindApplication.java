package io.skymind.pathmind.webapp;

import io.skymind.pathmind.services.ExperimentGoalsUpdateAsyncBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication(scanBasePackages = "io.skymind.pathmind", exclude = ErrorMvcAutoConfiguration.class)
@PropertySource({"classpath:application.properties", "classpath:shared.properties"})
@EnableCaching
@EnableScheduling
@EnableAsync
public class PathmindApplication implements CommandLineRunner {

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

    @Autowired
    private ExperimentGoalsUpdateAsyncBatchService goalsUpdateAsyncBatchService;

    @Override
    public void run(String... args) throws Exception {
        log.warn("Starting migration for experiments goals");
        goalsUpdateAsyncBatchService.migrateExperimentGoalsCalculation();

        log.warn("End of Run method");
    }
}
