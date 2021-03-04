package io.skymind.pathmind.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"classpath:application.properties", "classpath:shared.properties"})
public class UpdaterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(UpdaterApplication.class, args);
    }

    @Autowired
    private ExecutionProgressUpdater updater;

    @Override
    public void run(String... args) {
        updater.run();
    }
}
