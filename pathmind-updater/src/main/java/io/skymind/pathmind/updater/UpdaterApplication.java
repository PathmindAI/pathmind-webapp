package io.skymind.pathmind.updater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"classpath:application.properties", "classpath:shared.properties"})
public class UpdaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdaterApplication.class, args);
    }
}
