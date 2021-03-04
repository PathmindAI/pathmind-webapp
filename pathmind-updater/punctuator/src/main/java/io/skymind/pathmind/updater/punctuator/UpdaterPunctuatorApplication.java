package io.skymind.pathmind.updater.punctuator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"classpath:application.properties", "classpath:shared.properties"})
@EnableScheduling
public class UpdaterPunctuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdaterPunctuatorApplication.class, args);
    }

    @Autowired
    private PendingJobsExtractor pendingJobsExtractor;

    @Scheduled(fixedRateString = "#{${pathmind.updater.interval_sec} * 1000}", initialDelay = 1_000)
    public void run() {
        pendingJobsExtractor.run();
    }
}
