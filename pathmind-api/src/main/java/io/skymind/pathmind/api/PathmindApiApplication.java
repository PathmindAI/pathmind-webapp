package io.skymind.pathmind.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"application.properties", "shared.properties"})
public class PathmindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PathmindApiApplication.class, args);
    }

}
